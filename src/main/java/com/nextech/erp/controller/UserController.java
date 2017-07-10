package com.nextech.erp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.naming.AuthenticationException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.nextech.erp.constants.ERPConstants;
import com.nextech.erp.filter.TokenFactory;
import com.nextech.erp.model.Authorization;
import com.nextech.erp.model.Page;
import com.nextech.erp.model.Report;
import com.nextech.erp.model.Reportusertypeassociation;
import com.nextech.erp.model.User;
import com.nextech.erp.model.Usertype;
import com.nextech.erp.model.Usertypepageassociation;
import com.nextech.erp.newDTO.UserDTO;
import com.nextech.erp.service.MailService;
import com.nextech.erp.service.NotificationService;
import com.nextech.erp.service.NotificationUserAssociationService;
import com.nextech.erp.service.ReportusertypeassociationService;
import com.nextech.erp.service.UserService;
import com.nextech.erp.service.UserTypeService;
import com.nextech.erp.service.UsertypepageassociationService;
import com.nextech.erp.status.UserStatus;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userservice;

	@Autowired
	com.nextech.erp.filter.TokenFactory tokenFactory;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	UserTypeService userTypeService;

	@Autowired
	UsertypepageassociationService usertypepageassociationService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	NotificationUserAssociationService notificationUserAssService;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	ReportusertypeassociationService reportusertypeassociationService;

	@Autowired
	MailService mailService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addUser(@Valid @RequestBody UserDTO userDTO,
			BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			if ((Boolean) request.getAttribute("auth_token")) {
				if (userservice.getUserByUserId(userDTO.getUserid()) == null) {

				} else {
					return new UserStatus(2, messageSource.getMessage(
							ERPConstants.USER_ID, null, null));
				}
				if (userservice.getUserByEmail(userDTO.getEmail()) == null) {
				} else {
					return new UserStatus(2, messageSource.getMessage(
							ERPConstants.EMAIL_ALREADY_EXIT, null, null));
				}
				if (userservice.getUserByMobile(userDTO.getMobile()) == null) {
				} else {
					return new UserStatus(2, messageSource.getMessage(
							ERPConstants.CONTACT_NUMBER_EXIT, null, null));
				}
				userservice.saveUser(userDTO,request);
				return new UserStatus(1, "User added Successfully !");
			} else {
				new UserStatus(0, "User is not authenticated.");
			}
		} catch (ConstraintViolationException cve) {
			System.out.println("Inside ConstraintViolationException");
			cve.printStackTrace();
			return new UserStatus(0, cve.getCause().getMessage());
		} catch (PersistenceException pe) {
			System.out.println("Inside PersistenceException");
			pe.printStackTrace();
			return new UserStatus(0, pe.getCause().getMessage());
		} catch (AuthenticationException authException) {

			return new UserStatus(0, authException.getCause().getMessage());
		} catch (Exception e) {
			System.out.println("Inside Exception");
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
		return new UserStatus(0, "User is not authenticated.");
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	public UserStatus login(@RequestBody User user, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user2 = userservice.getUserByUserId(user.getUserid());
		try {

			System.out.println(messageSource.getMessage(ERPConstants.COUNT,
					null, null));
			if (user2 != null && authenticate(user, user2)) {
				Authorization authorization = new Authorization();
				authorization.setUserid(user.getUserid());
				authorization.setPassword(user.getPassword());
				authorization.setUpdatedDate(new Date());
				String token = TokenFactory.createAccessJwtToken(user2);
				authorization.setToken(token);
				response.addHeader("auth_token", token);
				Usertype usertype = userTypeService.getEntityById(
						Usertype.class, user2.getUsertype().getId());
				List<Usertypepageassociation> usertypepageassociations = usertypepageassociationService
						.getPagesByUsertype(usertype.getId());
				List<Reportusertypeassociation> reportusertypeassociations = reportusertypeassociationService.getReportByUsertype(usertype.getId());
				HashMap<String, Object> result = new HashMap<String, Object>();
				List<Page> pages = new ArrayList<Page>();
				List<Report> reports = new ArrayList<Report>();
				if(reportusertypeassociations !=null){
					for (Reportusertypeassociation reportusertypeassociation : reportusertypeassociations) {
						reports.add(reportusertypeassociation.getReport());
					}
					result.put("reports", reports);
				}
				if(usertypepageassociations !=null){
				for (Usertypepageassociation usertypepageassociation : usertypepageassociations) {
					pages.add(usertypepageassociation.getPage());
				}
				result.put("pages", pages);
				}
				return new UserStatus(1, "User logged in Successfully !", result,user2);
			}
		} catch (AuthenticationException authException) {
			return new UserStatus(0, authException.getCause().getMessage());
		} catch (Exception e) {
			System.out.println("Inside Exception");
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
		return new UserStatus(0, "Please enter correct credentials");

	}

	private boolean authenticate(User formUser, User dbUser) {
		if (formUser.getUserid().equals(dbUser.getUserid())
				&& formUser.getPassword().equals(dbUser.getPassword())) {
			dbUser.getFirstName();
			dbUser.getLastName();
			return true;
		} else {
			return false;
		}

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody User getUser(@PathVariable("id") long id) {
		User user = null;
		try {
			user = userservice.getEntityById(User.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateUser(@RequestBody UserDTO userDTO,HttpServletRequest request,HttpServletResponse response) {
		try {
			User oldUserInfo = userservice.getEntityById(User.class, userDTO.getId());
			if(userDTO.getUserid().equals(oldUserInfo.getUserid())){  
			} else { 
				if (userservice.getUserByUserId(userDTO.getUserid()) == null) {
			    }else{  
				return new UserStatus(2, messageSource.getMessage(ERPConstants.USER_ID, null, null));
				}
			 }
            if(userDTO.getEmail().equals(oldUserInfo.getEmail())){  
			} else { 
				if (userservice.getUserByEmail(userDTO.getEmail()) == null) {
			    }else{  
				return new UserStatus(2, messageSource.getMessage(ERPConstants.EMAIL_ALREADY_EXIT, null, null));
				}
			 }           
			if (userDTO.getMobile().equals(oldUserInfo.getMobile())) {
			} else {
				if (userservice.getUserByMobile(userDTO.getMobile()) == null) {
				} else {
					return new UserStatus(2, messageSource.getMessage(
							ERPConstants.CONTACT_NUMBER_EXIT, null, null));
				}
			}
			userservice.updateUser(userDTO, request);
			return new UserStatus(1, "User update Successfully !");
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	/* @CrossOrigin(origins = "http://localhost:8080") */
	/* Getting List of objects in Json format in Spring Restful Services */
	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<User> getUser() {

		List<User> userList = null;
		try {
			userList = userservice.getEntityList(User.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userList;
	}

	@RequestMapping(value = "userProfile/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<User> getUserProfile(@PathVariable("id") long id) {

		List<User> userProfileList = null;
		try {
			userProfileList = userservice.getUserProfileByUserId(id);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userProfileList;
	}

	/* Delete an object from DB in Spring Restful Services */
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteEmployee(@PathVariable("id") long id) {

		try {
			User user = userservice.getEntityById(User.class, id);
			user.setIsactive(false);
			userservice.updateEntity(user);
			return new UserStatus(1, "User deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}

}
