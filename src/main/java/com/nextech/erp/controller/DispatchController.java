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

import com.nextech.erp.dto.DispatchDTO;
import com.nextech.erp.dto.Part;
import com.nextech.erp.model.Dispatch;
import com.nextech.erp.model.Product;
import com.nextech.erp.model.Productinventory;
import com.nextech.erp.model.Productinventoryhistory;
import com.nextech.erp.model.Productorder;
import com.nextech.erp.model.Productorderassociation;
import com.nextech.erp.model.Status;
import com.nextech.erp.service.DispatchService;
import com.nextech.erp.service.ProductService;
import com.nextech.erp.service.ProductinventoryService;
import com.nextech.erp.service.ProductinventoryhistoryService;
import com.nextech.erp.service.ProductorderService;
import com.nextech.erp.service.ProductorderassociationService;
import com.nextech.erp.service.StatusService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/dispatch")
public class DispatchController {

	@Autowired
	DispatchService dispatchservice;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	ProductorderService productorderService;

	@Autowired
	ProductService productService;

	@Autowired
	ProductorderassociationService productorderassociationService;

	@Autowired
	StatusService statusService;

	@Autowired
	ProductinventoryService productinventoryService;

	@Autowired
	ProductinventoryhistoryService productinventoryhistoryService;

	private static final int STATUS_PRODUCT__INVENTORY_ADD = 25;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addDispatch(
			@Valid @RequestBody Dispatch dispatch, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			dispatch.setIsactive(true);
			dispatchservice.addEntity(dispatch);
			return new UserStatus(1, "Dispatch added Successfully !");
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

	@RequestMapping(value = "/dispatchProducts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus listDispatch(
			@RequestBody DispatchDTO dispatchDTO, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			for (Part part : dispatchDTO.getParts()) {
				Dispatch dispatch = setPart(part);

				dispatch.setDescription(dispatchDTO.getDescription());
				dispatch.setProductorder(productorderService.getEntityById(
						Productorder.class, dispatchDTO.getOrderId()));
				dispatch.setInvoiceNo(dispatchDTO.getInvoiceNo());
				dispatch.setCreatedBy(Long.parseLong(request.getAttribute(
						"current_user").toString()));
				dispatch.setUpdatedBy(Long.parseLong(request.getAttribute(
						"current_user").toString()));
				dispatchservice.addEntity(dispatch);
				Productorder productorder = productorderService.getEntityById(
						Productorder.class, dispatch.getProductorder().getId());
				Product product = productService.getEntityById(Product.class,
						dispatch.getProduct().getId());

				// TODO update product order association
				updateProductOrderAssoRemainingQuantity(productorder, dispatch,
						request, response);

				// TODO add product Inventroy history
				addProductInventoryHistory(dispatch.getQuantity(), product,
						dispatch, request, response);

				// TODO update product Inventory
				updateProductInventory(dispatch, product, request, response);

				// TODO update product order
				updateProductOrder(productorder);
			}

			return new UserStatus(1, "Dispatch added Successfully !");
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

	private Dispatch setPart(Part part) throws Exception {
		Dispatch dispatch = new Dispatch();
		dispatch.setProduct(productService.getEntityById(Product.class,
				part.getProductId()));
		dispatch.setQuantity(part.getQuantity());
		dispatch.setIsactive(true);
		return dispatch;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Dispatch getDispatch(@PathVariable("id") long id) {
		Dispatch dispatch = null;
		try {
			dispatch = dispatchservice.getEntityById(Dispatch.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dispatch;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateDispatch(
			@RequestBody Dispatch dispatch,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			dispatch.setIsactive(true);
			dispatch.setCreatedBy(Long.parseLong(request.getAttribute(
					"current_user").toString()));
			dispatch.setUpdatedBy(Long.parseLong(request.getAttribute(
					"current_user").toString()));
			dispatchservice.updateEntity(dispatch);
			return new UserStatus(1, "Dispatch update Successfully !");
		} catch (Exception e) {
			// e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Dispatch> getDispatch() {

		List<Dispatch> dispatchList = null;
		try {
			dispatchList = dispatchservice.getEntityList(Dispatch.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dispatchList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteDispatch(@PathVariable("id") long id) {

		try {
			Dispatch dispatch = dispatchservice.getEntityById(Dispatch.class,
					id);
			dispatch.setIsactive(false);
			dispatchservice.updateEntity(dispatch);
			return new UserStatus(1, "Dispatch deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}

	private void updateProductOrderAssoRemainingQuantity(
			Productorder productorder, Dispatch dispatch,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Productorderassociation productorderassociation = productorderassociationService
				.getProductorderassociationByProdcutOrderIdandProdcutId(
						productorder.getId(), dispatch.getProduct().getId());
		productorderassociation.setRemainingQuantity(productorderassociation
				.getRemainingQuantity() - dispatch.getQuantity());
		productorderassociation.setCreatedBy(Long.parseLong(request
				.getAttribute("current_user").toString()));
		productorderassociation.setUpdatedBy(Long.parseLong(request
				.getAttribute("current_user").toString()));
		productorderassociationService.updateEntity(productorderassociation);
	}

	private void updateProductOrder(Productorder productorder) throws Exception {

		productorder.setStatus(statusService.getEntityById(Status.class, 12));
		productorderService.updateEntity(productorder);
	}

	private Productinventory updateProductInventory(Dispatch dispatch,
			Product product,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Productinventory productinventory = productinventoryService
				.getProductinventoryByProductId(dispatch.getProduct().getId());
		if (productinventory == null) {
			productinventory = new Productinventory();
			productinventory.setProduct(product);
			productinventory.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			productinventory.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			productinventory.setQuantityavailable(productinventory
					.getQuantityavailable() - dispatch.getQuantity());
			productinventory.setIsactive(true);
			productinventoryService.addEntity(productinventory);
		} else {
			productinventory.setQuantityavailable(productinventory
					.getQuantityavailable() - dispatch.getQuantity());
			productinventoryService.updateEntity(productinventory);
		}
		return productinventory;
	}

	private void addProductInventoryHistory(long goodQuantity, Product product,
			Dispatch dispatch,HttpServletRequest request,HttpServletResponse response) throws Exception {
		Productinventory productinventory = productinventoryService
				.getProductinventoryByProductId(product.getId());
		if (productinventory == null) {
			productinventory = new Productinventory();
			productinventory.setProduct(product);
			productinventory.setQuantityavailable(0);
			productinventory.setIsactive(true);
			productinventoryService.addEntity(productinventory);
		}
		Productinventoryhistory productinventoryhistory = new Productinventoryhistory();
		productinventoryhistory.setProductinventory(productinventory);
		productinventoryhistory.setIsactive(true);
		productinventoryhistory.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		productinventoryhistory.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		productinventoryhistory.setBeforequantity(productinventory
				.getQuantityavailable() - dispatch.getQuantity());
		productinventoryhistory.setAfterquantity((goodQuantity
				+ productinventory.getQuantityavailable() - dispatch
				.getQuantity()));
		productinventoryhistory.setStatus(statusService.getEntityById(
				Status.class, STATUS_PRODUCT__INVENTORY_ADD));
		productinventoryhistoryService.addEntity(productinventoryhistory);

	}

}
