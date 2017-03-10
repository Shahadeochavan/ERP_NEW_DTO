package com.nextech.erp.controller;

import java.util.List;

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
import com.nextech.erp.model.Productinventory;
import com.nextech.erp.service.ProductinventoryService;
import com.nextech.erp.status.UserStatus;
@Controller
@RequestMapping("/productinventory")
public class ProductinventoryController {


	@Autowired
	ProductinventoryService productinventoryService;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addProductinventory(
			@Valid @RequestBody Productinventory productinventory, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			if (productinventoryService.getProductinventoryByProductId(
					productinventory.getProduct().getId()) == null){
				productinventory.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
				productinventory.setIsactive(true);
				productinventoryService.addEntity(productinventory);
			}	
			else
				return new UserStatus(1, messageSource.getMessage(
						ERPConstants.PRODUCT_INVENTORY_ASSO_EXIT, null, null));
			return new UserStatus(1, "Productinventory added Successfully !");
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
	public @ResponseBody Productinventory getProductinventory(@PathVariable("id") long id) {
		Productinventory productinventory = null;
		try {
			productinventory = productinventoryService.getEntityById(Productinventory.class,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productinventory;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateProductinventory(@RequestBody Productinventory productinventory) {
		try {
			productinventoryService.updateEntity(productinventory);
			return new UserStatus(1, "Productinventory update Successfully !");
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Productinventory> getProductinventory() {

		List<Productinventory> productinventoryList = null;
		try {
			productinventoryList = productinventoryService.getEntityList(Productinventory.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return productinventoryList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteProductinventory(@PathVariable("id") long id) {

		try {
			Productinventory productinventory = productinventoryService.getEntityById(Productinventory.class,id);
			productinventory.setIsactive(false);
			productinventoryService.updateEntity(productinventory);
			return new UserStatus(1, "Productinventory deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}
	private void saveProductInventory(){
		
	}

}