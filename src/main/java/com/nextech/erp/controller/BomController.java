package com.nextech.erp.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

import com.nextech.erp.dto.BOMModelData;
import com.nextech.erp.dto.BomDTO;
import com.nextech.erp.dto.BomModelPart;
import com.nextech.erp.dto.BomRMVendorModel;
import com.nextech.erp.dto.CreatePdfForBomProduct;
import com.nextech.erp.dto.ProductBomDTO;
import com.nextech.erp.model.Bomrmvendorassociation;
import com.nextech.erp.model.Bom;
import com.nextech.erp.model.Product;
import com.nextech.erp.model.Rawmaterial;
import com.nextech.erp.model.Rawmaterialvendorassociation;
import com.nextech.erp.model.Vendor;
import com.nextech.erp.service.BOMRMVendorAssociationService;
import com.nextech.erp.service.BomService;
import com.nextech.erp.service.ProductService;
import com.nextech.erp.service.RMVAssoService;
import com.nextech.erp.service.RawmaterialService;
import com.nextech.erp.service.VendorService;
import com.nextech.erp.status.Response;
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
	BOMRMVendorAssociationService bOMRMVendorAssociationService;
	
	@Autowired
	VendorService vendorService;
	
	@Autowired
	RMVAssoService rMVAssoService;
	
	
	

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
			
			// TODO save call bom
						Bom bom = saveBom(bomDTO, request, response);
						
						String bomId = generateBomId()+bom.getId();
						bom.setBomId(bomId);
						bomService.updateEntity(bom);

						// TODO add product order association
						addBomRMVendorAsso(bomDTO, bom, request, response);
			

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
	public @ResponseBody List<Bom> getBom(HttpServletRequest request,HttpServletResponse response) throws IOException {

		List<Bom> bomList = null;
		try {
			bomList = bomService.getEntityList(Bom.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		//  downloadPDF(request, response, bomList);
		return bomList;
	}
	
	@RequestMapping(value = "/BomCompletedList", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Response getBomCompleted(HttpServletRequest request,HttpServletResponse response) throws IOException {

		List<Bom> bomList = null;
		List<BOMModelData> bomModelDatas = new ArrayList<BOMModelData>(); 
		try {
			bomList = bomService.getEntityList(Bom.class);
			for (Bom bom : bomList) {
				Product product = productService.getEntityById(Product.class, bom.getProduct().getId());
				BOMModelData bomModelData = new BOMModelData();
				bomModelData.setPartNumber(product.getPartNumber());
				bomModelData.setId(product.getId());
				bomModelDatas.add(bomModelData);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		//  downloadPDF(request, response, bomList);
		return new Response(1, bomModelDatas);
	}
	
	@RequestMapping(value = "/getProductList", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Response getProductList() throws IOException {

		List<Product> products = null;
		List<Long> productIdList = null; 
		Response response = null;
		try {
			productIdList = bomService.getProductList();
			products = productService.getProductList(productIdList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(products == null){
			response = new Response(0,"Please add Product RM Association to Create BOM", products);
		}else{
			response = new Response(1,"Success", products);
		}
		return response;
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
	@RequestMapping(value = "downloadBomPdf/{PRODUCT-ID}/{BOM-ID}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody void getBomPdfByProductIdAndBomId(@PathVariable("PRODUCT-ID") long productId,@PathVariable("BOM-ID") long bomId,HttpServletRequest request, HttpServletResponse response) throws IOException {

		List<Bom> boList = null;
		List<BomRMVendorModel> bomRMVendorModels = new ArrayList<BomRMVendorModel>();
		ProductBomDTO productBomDTO = new ProductBomDTO();
		try {
			// TODO afterwards you need to change it from properties
			boList = bomService.getBomListByProductIdAndBomId(productId, bomId);
			for (Bom bom : boList) {
				List<Bomrmvendorassociation> bOMRMVendorAssociations = bOMRMVendorAssociationService.getBomRMVendorByBomId(bom.getId());
				for (Bomrmvendorassociation bomrmVendorAssociation : bOMRMVendorAssociations) {
					BomRMVendorModel bomRMVendorModel  = new BomRMVendorModel();
					Rawmaterial rawmaterial = rawmaterialService.getEntityById(Rawmaterial.class, bomrmVendorAssociation.getRawmaterial().getId());
					Vendor vendor = vendorService.getEntityById(Vendor.class, bomrmVendorAssociation.getVendor().getId());
					Product product = productService.getEntityById(Product.class, bom.getProduct().getId());
					Rawmaterialvendorassociation rawmaterialvendorassociation = rMVAssoService.getRMVAssoByRMId(rawmaterial.getId());
					bomRMVendorModel.setDescription(rawmaterial.getDescription());
					bomRMVendorModel.setVendorName(vendor.getCompanyName());
					bomRMVendorModel.setProductName(product.getName());
					bomRMVendorModel.setPricePerUnit(rawmaterialvendorassociation.getPricePerUnit());
					bomRMVendorModel.setQuantity(bomrmVendorAssociation.getQuantity());
					bomRMVendorModel.setAmount(bomrmVendorAssociation.getQuantity()*rawmaterialvendorassociation.getPricePerUnit());
					productBomDTO.setClinetPartNumber(product.getClientpartnumber());
					productBomDTO.setProductPartNumber(product.getPartNumber()); 
					productBomDTO.setCreatedDate(bom.getCreatedDate());
					bomRMVendorModels.add(bomRMVendorModel);
					
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    downloadPDF(request, response, bomRMVendorModels,productBomDTO);
//		return boList;
	}
	
	
	private Bom saveBom(BomDTO bomDTO,HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		Bom bom = new Bom();
		bom.setBomId(bomDTO.getBomId());
		bom.setProduct(productService.getEntityById(Product.class,bomDTO.getProduct()));
		bom.setIsactive(true);
		bom.setCreatedBy(request.getAttribute("current_user").toString());
		bomService.addEntity(bom);
		return bom;
	}
	
	private void addBomRMVendorAsso(BomDTO bomDTO,Bom bom,HttpServletRequest request,HttpServletResponse response) throws Exception {
		List<BomModelPart> bomModelParts = bomDTO.getBomModelParts();
		if (bomModelParts != null	&& !bomModelParts.isEmpty()) {
			for (BomModelPart bomModelPart : bomModelParts) {
				
				Bomrmvendorassociation bomrmVendorAssociation = new Bomrmvendorassociation();
				bomrmVendorAssociation.setBom(bom);
				Rawmaterial rawmaterial = rawmaterialService.getEntityById(Rawmaterial.class, bomModelPart.getRawmaterial().getId());
				Vendor vendor = vendorService.getEntityById(Vendor.class, bomModelPart.getVendor().getId());
				bomrmVendorAssociation.setQuantity(bomModelPart.getQuantity());
				bomrmVendorAssociation.setRawmaterial(rawmaterial);
				bomrmVendorAssociation.setVendor(vendor);
				bomrmVendorAssociation.setPricePerUnit(bomModelPart.getPricePerUnit());
				bomrmVendorAssociation.setCost(bomModelPart.getQuantity()*bomModelPart.getPricePerUnit());
				bomrmVendorAssociation.setCreatedBy(request.getAttribute("current_user").toString());
				bomrmVendorAssociation.setIsactive(true);
				bOMRMVendorAssociationService.addEntity(bomrmVendorAssociation);
			}
		}
	}

	public void downloadPDF(HttpServletRequest request, HttpServletResponse response,List<BomRMVendorModel> bomRMVendorModels,ProductBomDTO productBomDTO) throws IOException {

		final ServletContext servletContext = request.getSession().getServletContext();
	    final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
	    final String temperotyFilePath = tempDirectory.getAbsolutePath();

	    String fileName = "bom.pdf";
	    response.setContentType("application/pdf");
	    response.setHeader("Content-disposition", "attachment; filename="+ fileName);

	    try {

	    	CreatePdfForBomProduct createPdfForBomProduct = new CreatePdfForBomProduct();
	    	createPdfForBomProduct.createPDF(temperotyFilePath+"\\"+fileName,bomRMVendorModels,productBomDTO);
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
	private String generateBomId(){
		String bom="";
		bom = "BOM000";
		return bom;
	}
}

