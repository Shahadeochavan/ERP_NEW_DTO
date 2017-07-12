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

import com.nextech.erp.factory.PageFactory;
import com.nextech.erp.model.Page;
import com.nextech.erp.newDTO.PageDTO;
import com.nextech.erp.service.PageService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/page")
public class PageController {

	@Autowired
	PageService pageservice;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addPage(@Valid @RequestBody PageDTO pageDTO,
			BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			Page page = PageFactory.getPage(pageDTO);
			page.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			pageservice.addEntity(page);
			return new UserStatus(1, "Page added Successfully !");
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
	public @ResponseBody Page getPage(@PathVariable("id") long id) {
		Page page = null;
		try {
			page = pageservice.getEntityById(Page.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updatePage(@RequestBody PageDTO pageDTO,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Page page = PageFactory.getPage(pageDTO);
			page.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			pageservice.updateEntity(page);
			return new UserStatus(1, "Page update Successfully !");
		} catch (Exception e) {
			// e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Page> getPage() {

		List<Page> PageList = null;
		try {
			PageList = pageservice.getEntityList(Page.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return PageList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deletePage(@PathVariable("id") long id) {

		try {
			Page page = pageservice.getEntityById(Page.class,id);
			page.setIsactive(false);
			pageservice.updateEntity(page);
			return new UserStatus(1, "Page deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}
}
