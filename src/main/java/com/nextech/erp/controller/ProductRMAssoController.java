package com.nextech.erp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nextech.erp.constants.ERPConstants;
import com.nextech.erp.dto.ProductRMAssociationModel;
import com.nextech.erp.dto.ProductRMAssociationModelParts;
import com.nextech.erp.model.Product;
import com.nextech.erp.model.Productrawmaterialassociation;
import com.nextech.erp.model.Rawmaterial;
import com.nextech.erp.service.ProductRMAssoService;
import com.nextech.erp.service.ProductService;
import com.nextech.erp.service.ProductionplanningService;
import com.nextech.erp.service.RawmaterialService;
import com.nextech.erp.status.Response;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/productRMAsso")
public class ProductRMAssoController {

	@Autowired
	ProductRMAssoService productRMAssoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	RawmaterialService rawmaterialService;

	@Autowired
	ProductService productService;

	@Autowired
	ProductionplanningService productionplanningService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addProductrawmaterialassociation(
			@Valid @RequestBody Productrawmaterialassociation productrawmaterialassociation,
			BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			if (productRMAssoService.getPRMAssociationByPidRmid(
					productrawmaterialassociation.getProduct().getId(),
					productrawmaterialassociation.getRawmaterial().getId()) == null){
				productrawmaterialassociation.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
				productrawmaterialassociation.setIsactive(true);
				productRMAssoService
						.addEntity(productrawmaterialassociation);
			}
			else
				return new UserStatus(1,
						messageSource.getMessage(ERPConstants.PRODUCT_RM_ASSO_EXIT, null, null));
			return new UserStatus(1,
					"Productrawmaterialassociation added Successfully !");
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

	@RequestMapping(value = "/createmultiple", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addMultipleRawmaterialorder(
			@Valid @RequestBody ProductRMAssociationModel productRMAssociationModel, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError().getDefaultMessage());
			}
			createMultipleRMAssociations(productRMAssociationModel, request.getAttribute("current_user").toString());

			return new UserStatus(1, "Multiple Rawmaterialorder added Successfully !");
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

	private void createMultipleRMAssociations(ProductRMAssociationModel productRMAssociationModel,String currentUser) throws Exception{
		for(ProductRMAssociationModelParts productRMAssociationModelParts : productRMAssociationModel.getProductRMAssociationModelParts()){
			Productrawmaterialassociation productrawmaterialassociation =  setMultipleRM(productRMAssociationModelParts);
			productrawmaterialassociation.setProduct(productService.getEntityById(Product.class, productRMAssociationModel.getProduct()));
			productrawmaterialassociation.setCreatedBy(Long.parseLong(currentUser));
			productRMAssoService.addEntity(productrawmaterialassociation);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Productrawmaterialassociation getProductrawmaterialassociation(
			@PathVariable("id") long id) {
		Productrawmaterialassociation productrawmaterialassociation = null;
		try {
			productrawmaterialassociation = productRMAssoService
					.getEntityById(Productrawmaterialassociation.class,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productrawmaterialassociation;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateProductrawmaterialassociation(
			@RequestBody Productrawmaterialassociation productrawmaterialassociation,HttpServletRequest request,HttpServletResponse response) {
		try {
			productrawmaterialassociation.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			productrawmaterialassociation.setIsactive(true);
			productRMAssoService.updateEntity(productrawmaterialassociation);
			return new UserStatus(1,"Productrawmaterialassociation update Successfully !");
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/update/multipleProductRMAssociation", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateProductRMAssociation(
			@RequestBody ProductRMAssociationModel productRMAssociationModel,HttpServletRequest request,HttpServletResponse response) {
		try {
			List<Productrawmaterialassociation> productrawmaterialassociations = productRMAssoService.getProductRMAssoListByProductId(productRMAssociationModel.getProduct());
			for(Productrawmaterialassociation productrawmaterialassociation : productrawmaterialassociations){
				//Remove all Product RM Associations
				//productRMAssoService.deleteEntity(Productrawmaterialassociation.class, productrawmaterialassociation.getId());
				deleteProductrawmaterialassociation(productrawmaterialassociation.getId());
			}

			createMultipleRMAssociations(productRMAssociationModel, request.getAttribute("current_user").toString());

			return new UserStatus(1,"Productrawmaterialassociation update Successfully !");
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Productrawmaterialassociation> getProductrawmaterialassociation() {

		List<Productrawmaterialassociation> productrawmaterialassociationList = null;
		try {
			productrawmaterialassociationList = productRMAssoService
					.getEntityList(Productrawmaterialassociation.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productrawmaterialassociationList;
	}

	@RequestMapping(value = "/list/multiple", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<ProductRMAssociationModel> getMultipleProductrawmaterialassociation() {


		List<ProductRMAssociationModel> productRMAssociationModels = new ArrayList<ProductRMAssociationModel>();
		try {
			List<Productrawmaterialassociation> productrawmaterialassociationList = null;
			productrawmaterialassociationList = productRMAssoService.getEntityList(Productrawmaterialassociation.class);
			HashMap<String, List<ProductRMAssociationModelParts>> multplePRMAsso = new HashMap<String, List<ProductRMAssociationModelParts>>();
			for(Productrawmaterialassociation productrawmaterialassociation : productrawmaterialassociationList){
				List<ProductRMAssociationModelParts> productRMAssociationModelParts = null;
				if(multplePRMAsso.get(productrawmaterialassociation.getProduct().getId()) == null){
					productRMAssociationModelParts = new ArrayList<ProductRMAssociationModelParts>();
				}else{
					productRMAssociationModelParts = multplePRMAsso.get(productrawmaterialassociation.getProduct().getId());
				}
				ProductRMAssociationModelParts productRMAssociationModelPart = new ProductRMAssociationModelParts();
				productRMAssociationModelPart.setQuantity(productrawmaterialassociation.getQuantity());
				productRMAssociationModelPart.setRawmaterial(productrawmaterialassociation.getRawmaterial());
				productRMAssociationModelParts.add(productRMAssociationModelPart);
				Product product = productService.getEntityById(Product.class, productrawmaterialassociation.getProduct().getId());
				multplePRMAsso.put(product.getName(), productRMAssociationModelParts);
			}
			 Set<Entry<String, List<ProductRMAssociationModelParts>>> multplePRMAssoEntries =  multplePRMAsso.entrySet();
			for(Entry<String, List<ProductRMAssociationModelParts>> multplePRMAssoEntry : multplePRMAssoEntries){
				ProductRMAssociationModel productRMAssociationModel = new ProductRMAssociationModel();
				//productRMAssociationModel.setProduct(multplePRMAssoEntry.getKey());
				productRMAssociationModel.setName(multplePRMAssoEntry.getKey());
				productRMAssociationModel.setProductRMAssociationModelParts(multplePRMAssoEntry.getValue());
				productRMAssociationModels.add(productRMAssociationModel);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productRMAssociationModels;
	}

	@RequestMapping(value = "productRMAssoList/{productId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Response getProductRMAssoList(@PathVariable("productId") long productId) {

		List<Productrawmaterialassociation> productrawmaterialassociationList = null;
		try {
			productrawmaterialassociationList = productRMAssoService.getProductRMAssoListByProductId(productId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Response(1,"Productionplanning List and Productrawmaterialassociation List",productrawmaterialassociationList);
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteProductrawmaterialassociation(
			@PathVariable("id") long id) {

		try {
			Productrawmaterialassociation productrawmaterialassociation = productRMAssoService
					.getEntityById(Productrawmaterialassociation.class, id);
			productrawmaterialassociation.setIsactive(false);
			productRMAssoService
					.updateEntity(productrawmaterialassociation);
			return new UserStatus(1,
					"Productrawmaterialassociation deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}

	private Productrawmaterialassociation setMultipleRM(ProductRMAssociationModelParts productRMAssociationModelParts) throws Exception {
		Productrawmaterialassociation productrawmaterialassociation = new Productrawmaterialassociation();
		productrawmaterialassociation.setQuantity(productRMAssociationModelParts.getQuantity());
		productrawmaterialassociation.setRawmaterial(rawmaterialService.getEntityById(Rawmaterial.class, productRMAssociationModelParts.getRawmaterial().getId()));
		productrawmaterialassociation.setIsactive(true);
		return productrawmaterialassociation;
	}
}
