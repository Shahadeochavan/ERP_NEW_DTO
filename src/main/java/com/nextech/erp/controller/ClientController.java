package com.nextech.erp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.nextech.erp.dto.Mail;
import com.nextech.erp.model.Client;
import com.nextech.erp.model.Notification;
import com.nextech.erp.model.Notificationuserassociation;
import com.nextech.erp.model.User;
import com.nextech.erp.model.Vendor;
import com.nextech.erp.newDTO.ClientDTO;
import com.nextech.erp.service.ClientService;
import com.nextech.erp.service.MailService;
import com.nextech.erp.service.NotificationService;
import com.nextech.erp.service.NotificationUserAssociationService;
import com.nextech.erp.service.UserService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/client")
public class ClientController {

	@Autowired
	ClientService clientService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	NotificationService notificationService;


	@Autowired
	NotificationUserAssociationService notificationUserAssService;
	
	@Autowired
	UserService userService;

	@Autowired
	MailService mailService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addClient(
			@Valid @RequestBody ClientDTO clientDTO, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
			if (clientService.getClientByCompanyName(clientDTO.getCompanyname()) == null) {

			} else {
				return new UserStatus(2, messageSource.getMessage(
						ERPConstants.COMPANY_NAME_EXIT, null, null));

			}
			if (clientService.getClientByEmail(clientDTO.getEmailid()) == null) {
			} else {
				return new UserStatus(2, messageSource.getMessage(
						ERPConstants.EMAIL_ALREADY_EXIT, null, null));
			}

		//	mailSending(client, request, response);
			//clientService.addEntity(client);
			clientService.saveClient(clientDTO, request);
			return new UserStatus(1, messageSource.getMessage(
					ERPConstants.CLIENT_ADDED, null, null));
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

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Client getClient(@PathVariable("id") long id) {
		Client client = null;
		try {
			client = clientService.getEntityById(Client.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateClient(@RequestBody ClientDTO clientDTO,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			
			Client oldClientInfo = clientService.getEntityById(Client.class, clientDTO.getId());
			System.out.println(oldClientInfo);
			if(clientDTO.getCompanyname().equals(oldClientInfo.getCompanyname())){  	
			} else { 
				if (clientService.getClientByCompanyName(clientDTO.getCompanyname()) == null) {
			    }else{  
				return new UserStatus(2, messageSource.getMessage(ERPConstants.COMPANY_NAME_EXIT, null, null));
				}
			 }
            if(clientDTO.getEmailid().equals(oldClientInfo.getEmailid())){  			
			} else { 
				if (clientService.getClientByEmail(clientDTO.getEmailid()) == null) {
			    }else{  
				return new UserStatus(2, messageSource.getMessage(ERPConstants.EMAIL_ALREADY_EXIT, null, null));
				}
			 }
		/*	client.setUpdatedBy(Long.parseLong(request.getAttribute(
					"current_user").toString()));
			client.setIsactive(true);*/
			
			//clientService.updateEntity(client);
			//mailSendingUpdate(client, request, response);
            clientService.updateClient(clientDTO, request);
			return new UserStatus(1, messageSource.getMessage(
					ERPConstants.CLIENT_UPDATE, null, null));
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Client> getClient() {

		List<Client> clientList = null;
		try {
			clientList = clientService.getEntityList(Client.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteClient(@PathVariable("id") long id) {

		try {
			Client client = clientService.getEntityById(Client.class, id);
			client.setIsactive(false);
			clientService.updateEntity(client);
			return new UserStatus(1, messageSource.getMessage(
					ERPConstants.CLIENT_DELETE, null, null));
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}
	private void mailSending(Client client,HttpServletRequest request, HttpServletResponse response) throws Exception{
		  Mail mail = new Mail();

		  Notification notification = notificationService.getEntityById(Notification.class,Long.parseLong(messageSource.getMessage(ERPConstants.CLIENT_ADDED_SUCCESSFULLY, null, null)));
		  List<Notificationuserassociation> notificationuserassociations = notificationUserAssService.getNotificationuserassociationBynotificationId(notification.getId());
		  for (Notificationuserassociation notificationuserassociation : notificationuserassociations) {
			  User user = userService.getEmailUserById(notificationuserassociation.getUser().getId());
			  if(notificationuserassociation.getTo()==true){
				   mail.setMailTo(client.getEmailid()); 
			  }else if(notificationuserassociation.getBcc()==true){
				  mail.setMailBcc(user.getEmail());
			  }else if(notificationuserassociation.getCc()==true){
				  mail.setMailCc(user.getEmail());
			  }
			
		}
	     
	        mail.setMailSubject(notification.getSubject());

	        Map < String, Object > model = new HashMap < String, Object > ();
	        model.put("firstName", client.getCompanyname());
	        model.put("email", client.getEmailid());
	        model.put("contactNumber", client.getContactnumber());
	        model.put("location", "Pune");
	        model.put("signature", "www.NextechServices.in");
	        mail.setModel(model);

		mailService.sendEmailWithoutPdF(mail, notification);
	}
	
	private void mailSendingUpdate(Client client,HttpServletRequest request, HttpServletResponse response) throws Exception{
		  Mail mail = new Mail();

		  Notification notification = notificationService.getEntityById(Notification.class,Long.parseLong(messageSource.getMessage(ERPConstants.CLIENT_UPDATE_SUCCESSFULLY, null, null)));
		  List<Notificationuserassociation> notificationuserassociations = notificationUserAssService.getNotificationuserassociationBynotificationId(notification.getId());
		  for (Notificationuserassociation notificationuserassociation : notificationuserassociations) {
			  User user = userService.getEmailUserById(notificationuserassociation.getUser().getId());
			  if(notificationuserassociation.getTo()==true){
				  mail.setMailTo(client.getEmailid());
			  }else if(notificationuserassociation.getBcc()==true){
				  mail.setMailBcc(user.getEmail());
			  }else if(notificationuserassociation.getCc()==true){
				  mail.setMailCc(user.getEmail());
			  }
			
		}
	     
	        mail.setMailSubject(notification.getSubject());

	        Map < String, Object > model = new HashMap < String, Object > ();
	        model.put("firstName", client.getCompanyname());
	        model.put("email", client.getEmailid());
	        model.put("contactNumber", client.getContactnumber());
	        model.put("location", "Pune");
	        model.put("signature", "www.NextechServices.in");
	        mail.setModel(model);

		mailService.sendEmailWithoutPdF(mail, notification);
	}
}
