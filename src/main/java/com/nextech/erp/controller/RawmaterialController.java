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
import com.nextech.erp.factory.RMFactory;
import com.nextech.erp.model.Product;
import com.nextech.erp.model.Productinventory;
import com.nextech.erp.model.Rawmaterial;
import com.nextech.erp.model.Rawmaterialinventory;
import com.nextech.erp.model.Rawmaterialvendorassociation;
import com.nextech.erp.newDTO.RawMaterialDTO;
import com.nextech.erp.service.RawmaterialService;
import com.nextech.erp.service.RawmaterialinventoryService;
import com.nextech.erp.status.UserStatus;
@Controller
@RequestMapping("/rawmaterial")
public class RawmaterialController {

	@Autowired
	RawmaterialService rawmaterialService;
	
	@Autowired
	RawmaterialinventoryService rawmaterialinventoryService;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addRawmaterial(
			@Valid @RequestBody RawMaterialDTO rawMaterialDTO, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			Rawmaterial rawmaterial = RMFactory.getRawMaterial(rawMaterialDTO);
			rawmaterial.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			rawmaterialService.addEntity(rawmaterial);
			addRMInventory(rawmaterial, Long.parseLong(request.getAttribute("current_user").toString()));
			return new UserStatus(1, messageSource.getMessage(ERPConstants.RAW_MATERAIL_ADD, null, null));
		} catch (ConstraintViolationException cve) {
			cve.printStackTrace();
			return new UserStatus(0, cve.getCause().getMessage());
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return new UserStatus(0, pe.getCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Rawmaterial getRawmaterial(@PathVariable("id") long id) {
		Rawmaterial rawmaterial = null;
		try {
			rawmaterial = rawmaterialService.getEntityById(Rawmaterial.class,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rawmaterial;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateRawmaterial(@RequestBody RawMaterialDTO rawMaterialDTO,HttpServletRequest request,HttpServletResponse response) {
		try {
			Rawmaterial rawmaterial = RMFactory.getRawMaterial(rawMaterialDTO);
			rawmaterial.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			rawmaterialService.updateEntity(rawmaterial);
			return new UserStatus(1,messageSource.getMessage(ERPConstants.RAW_MATERAIL_UPDATE, null, null));
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Rawmaterial> getRawmaterial() {

		List<Rawmaterial> rawmaterialList = null;
		try {
			rawmaterialList = rawmaterialService.getEntityList(Rawmaterial.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rawmaterialList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteRawmaterial(@PathVariable("id") long id) {

		try {
			Rawmaterial rawmaterial = rawmaterialService.getEntityById(Rawmaterial.class,id);
			rawmaterial.setIsactive(false);
			rawmaterialService.updateEntity(rawmaterial);
			return new UserStatus(1, messageSource.getMessage(ERPConstants.RAW_MATERAIL_DELETE, null, null));
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}

	@RequestMapping(value = "/getRMaterial/{VendorId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Rawmaterialvendorassociation> getRawmaterialForVendor(@PathVariable("VendorId") long id) {

		List<Rawmaterialvendorassociation> rawmaterialvendorassociationList = null;
		try {
			rawmaterialvendorassociationList = rawmaterialService.getRawmaterialByVenodrId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rawmaterialvendorassociationList;
	}

	@RequestMapping(value = "/getRMForRMOrder/{RMOrderId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Rawmaterial> getRawmaterialForRMOrder(@PathVariable("RMOrderId") long id) {

		List<Rawmaterial> rawmaterialList = null;
		try {
			rawmaterialList = rawmaterialService.getRawMaterialByRMOrderId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rawmaterialList;
	}
	private void addRMInventory(Rawmaterial rawmaterial,long userId) throws Exception{
		Rawmaterialinventory rawmaterialinventory = new Rawmaterialinventory();
		rawmaterialinventory.setRawmaterial(rawmaterial);
		rawmaterialinventory.setQuantityAvailable(0);
		rawmaterialinventory.setCreatedBy(userId);
		rawmaterialinventory.setIsactive(true);
		rawmaterialinventoryService.addEntity(rawmaterialinventory);
	}
}
