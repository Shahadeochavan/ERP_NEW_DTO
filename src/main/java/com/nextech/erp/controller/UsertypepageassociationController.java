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

import com.nextech.erp.factory.UserTypePageAssoFactory;
import com.nextech.erp.model.Usertypepageassociation;
import com.nextech.erp.newDTO.UserTypePageAssoDTO;
import com.nextech.erp.service.UsertypepageassociationService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/usertypepageassociation")
public class UsertypepageassociationController {

	@Autowired
	UsertypepageassociationService usertypepageassociationService;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addPageAss(
			@Valid @RequestBody UserTypePageAssoDTO userTypePageAssoDTO,
			BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			Usertypepageassociation usertypepageassociation = UserTypePageAssoFactory.getUserTypePageAss(userTypePageAssoDTO, request);
			usertypepageassociation.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			usertypepageassociationService.addEntity(usertypepageassociation);
			return new UserStatus(1,
					"Usertypepageassociation added Successfully !");
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
	public @ResponseBody Usertypepageassociation getPageAss(
			@PathVariable("id") long id) {
		Usertypepageassociation usertypepageassociation = null;
		try {
			usertypepageassociation = usertypepageassociationService
					.getEntityById(Usertypepageassociation.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usertypepageassociation;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updatePageAss(
			@RequestBody UserTypePageAssoDTO userTypePageAssoDTO,HttpServletRequest request,HttpServletResponse response) {
		try {
			Usertypepageassociation usertypepageassociation = UserTypePageAssoFactory.getUserTypePageAss(userTypePageAssoDTO, request);
			usertypepageassociation.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			usertypepageassociationService.addEntity(usertypepageassociation);
			return new UserStatus(1,
					"Usertypepageassociation update Successfully !");
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Usertypepageassociation> getPageAss() {

		List<Usertypepageassociation> UsertypepageassociationList = null;
		try {
			UsertypepageassociationList = usertypepageassociationService
					.getEntityList(Usertypepageassociation.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return UsertypepageassociationList;
	}

	/* Delete an object from DB in Spring Restful Services */
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deletePageAss(@PathVariable("id") long id) {

		try {
			Usertypepageassociation usertypepageassociation = usertypepageassociationService
					.getEntityById(Usertypepageassociation.class, id);
			usertypepageassociation.setIsactive(false);
			usertypepageassociationService
					.updateEntity(usertypepageassociation);
			return new UserStatus(1,
					"Usertypepageassociation deleted Successfully !");
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}

	}
}
