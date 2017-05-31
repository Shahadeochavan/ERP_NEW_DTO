package com.nextech.erp.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nextech.erp.constants.ERPConstants;
import com.nextech.erp.dto.BomDTO;
import com.nextech.erp.dto.BomModelPart;
import com.nextech.erp.dto.CreatePDFProductOrder;
import com.nextech.erp.dto.CreatePdfForBomProduct;
import com.nextech.erp.model.Bom;
import com.nextech.erp.model.Client;
import com.nextech.erp.model.Notification;
import com.nextech.erp.model.Product;
import com.nextech.erp.model.Productorder;
import com.nextech.erp.model.Rawmaterial;
import com.nextech.erp.model.Status;
import com.nextech.erp.model.Vendor;
import com.nextech.erp.service.BomService;
import com.nextech.erp.service.ProductService;
import com.nextech.erp.service.RawmaterialService;
import com.nextech.erp.service.VendorService;
import com.nextech.erp.status.UserStatus;

@RestController
@RequestMapping("/bom")
public class BomController {

	@Autowired
	BomService bomService;
	
	@Autowired 
	ProductService productService;
	
	@Autowired
	RawmaterialService rawmaterialService;
	
	@Autowired
	VendorService vendorService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addUnit(@Valid @RequestBody Bom bom,HttpServletRequest request,HttpServletResponse response,
			BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			bom.setCreatedBy(request.getAttribute("current_user").toString());
			bom.setIsactive(true);
		long id=	bomService.addEntity(bom);
			System.out.println("id is"+id);
			return new UserStatus(1, "Unit added Successfully !");
		} catch (ConstraintViolationException cve) {
			cve.printStackTrace();
			return new UserStatus(0, cve.getCause().getMessage());
		} catch (PersistenceException pe) {
			System.out.println("Inside PersistenceException");
			pe.printStackTrace();
			return new UserStatus(0, pe.getCause().getMessage());
		} catch (Exception e) {
			System.out.println("Inside Exception");
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
	}
	
	@RequestMapping(value = "/createmultiple", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addMultipleBom(
			@Valid @RequestBody BomDTO bomDTO, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError().getDefaultMessage());
			}
			createMultipleBom(bomDTO, request.getAttribute("current_user").toString());

			return new UserStatus(1, "Bom added Successfully !");
		} catch (ConstraintViolationException cve) {
			System.out.println("Inside ConstraintViolationException");
			cve.printStackTrace();
			return new UserStatus(0, cve.getCause().getMessage());
		} catch (PersistenceException pe) {
			System.out.println("Inside PersistenceException");
			pe.printStackTrace();
			return new UserStatus(0, pe.getCause().getMessage());
		} catch (Exception e) {
			System.out.println("Inside Exception");
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
	}

	private void createMultipleBom(BomDTO bomDTO,String currentUser) throws Exception{
		for(BomModelPart bomModelParts : bomDTO.getBomModelParts()){
			Bom bom =  setMultipleBom(bomModelParts);
			bom.setProduct(productService.getEntityById(Product.class,bomDTO.getProduct()));
			bom.setBomId(bomDTO.getBomId());
			bom.setCreatedBy((currentUser));
			bom.setIsactive(true);
			bomService.addEntity(bom);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Bom getUnit(@PathVariable("id") long id) {
		Bom bom = null;
		try {
			bom = bomService.getEntityById(Bom.class,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bom;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateUnit(@RequestBody Bom bom,HttpServletRequest request,HttpServletResponse response) {
		try {
			bom.setUpdatedBy(request.getAttribute("current_user").toString());
			bom.setIsactive(true);
			bomService.updateEntity(bom);
			return new UserStatus(1, "Bom update Successfully !");
		} catch (Exception e) {
			 e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Bom> getUnit(HttpServletRequest request,HttpServletResponse response) throws IOException {

		List<Bom> bomList = null;
		try {
			bomList = bomService.getEntityList(Bom.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		  downloadPDF(request, response, bomList);
		return bomList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteClient(@PathVariable("id") long id) {

		try {
			Bom bom = bomService.getEntityById(Bom.class, id);
			bom.setIsactive(false);
			bomService.updateEntity(bom);
			return new UserStatus(1, "Bom deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}
	
	@RequestMapping(value = "bomList/{PRODUCT-ID}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Bom> getBomByProductId(@PathVariable("PRODUCT-ID") long productId) {

		List<Bom> boList = null;
		try {
			// TODO afterwards you need to change it from properties
			boList = bomService.getBomListByProductId(productId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return boList;
	}
	@RequestMapping(value = "bomPdfList/{PRODUCT-ID}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Bom> getBomPdfByProductId(@PathVariable("PRODUCT-ID") long productId,HttpServletRequest request, HttpServletResponse response) throws IOException {

		List<Bom> boList = null;
		try {
			// TODO afterwards you need to change it from properties
			boList = bomService.getBomListByProductId(productId);

		} catch (Exception e) {
			e.printStackTrace();
		}
    downloadPDF(request, response, boList);
		return boList;
	}
	
	private Bom setMultipleBom(BomModelPart bomModelPart) throws Exception {
		Bom bom = new Bom();
		Rawmaterial rawmaterial = rawmaterialService.getEntityById(Rawmaterial.class, bomModelPart.getRawmaterial().getId());
		Vendor vendor = vendorService.getEntityById(Vendor.class, bomModelPart.getVendor().getId());
		bom.setQuantity(bomModelPart.getQuantity());
		bom.setVendor(vendor);
		bom.setRawmaterial(rawmaterial);
		bom.setPricePerUnit(bomModelPart.getPricePerUnit());
		bom.setCost(bomModelPart.getQuantity()*bomModelPart.getPricePerUnit());
		return bom;
	}
	public void downloadPDF(HttpServletRequest request, HttpServletResponse response,List<Bom> boList) throws IOException {

		final ServletContext servletContext = request.getSession().getServletContext();
	    final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
	    final String temperotyFilePath = tempDirectory.getAbsolutePath();

	    String fileName = "ProductOrder.pdf";
	    response.setContentType("application/pdf");
	    response.setHeader("Content-disposition", "attachment; filename="+ fileName);

	    try {

	    	CreatePdfForBomProduct createPdfForBomProduct = new CreatePdfForBomProduct();
	    	createPdfForBomProduct.createPDF(temperotyFilePath+"\\"+fileName,boList);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        baos = convertPDFToByteArrayOutputStream(temperotyFilePath+"\\"+fileName);
	        OutputStream os = response.getOutputStream();
	        baos.writeTo(os);
	        os.flush();

	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	}

	private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) throws Exception {

		InputStream inputStream = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			inputStream = new FileInputStream(fileName);
			byte[] buffer = new byte[1024];
			baos = new ByteArrayOutputStream();

			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return baos;
	}

}

