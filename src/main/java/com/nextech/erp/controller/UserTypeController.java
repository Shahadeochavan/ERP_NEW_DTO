package com.nextech.erp.controller;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nextech.erp.factory.UserTypeFactory;
import com.nextech.erp.model.Usertype;
import com.nextech.erp.newDTO.UserTypeDTO;
import com.nextech.erp.service.UserTypeService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/usertype")
public class UserTypeController {
	@Autowired
	UserTypeService userTypeService;
	
	

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addUserType(@Valid @RequestBody UserTypeDTO userTypeDTO,
			BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			Usertype usertype = UserTypeFactory.getUserType(userTypeDTO, request);
			usertype.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			userTypeService.addEntity(usertype);
			return new UserStatus(1, "Usertype added Successfully !");
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
	public @ResponseBody Usertype getUserType(@PathVariable("id") long id) {
		Usertype userType = null;
		try {
			userType = userTypeService.getEntityById(Usertype.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userType;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateUserType(@RequestBody UserTypeDTO userTypeDTO,HttpServletRequest request,HttpServletResponse response) {
		try {
		Usertype usertype =	UserTypeFactory.getUserType(userTypeDTO, request);
		usertype.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		userTypeService.updateEntity(usertype);
			return new UserStatus(1, "UserType update Successfully !");
		} catch (Exception e) {
			 e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Usertype> getUserType() {

		List<Usertype> userList = null;
		try {
			userList = userTypeService.getEntityList(Usertype.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteUserType(@PathVariable("id") long id) {

		try {
			Usertype usertype = userTypeService.getEntityById(Usertype.class,id);
			usertype.setIsactive(false);
			userTypeService.updateEntity(usertype);
			return new UserStatus(1, "UserType deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}
}
