package com.nextech.erp.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.nextech.erp.controller.UserController;
import com.nextech.erp.dao.UserDao;
import com.nextech.erp.dto.Mail;
import com.nextech.erp.model.Notification;
import com.nextech.erp.model.Notificationuserassociation;
import com.nextech.erp.model.User;
import com.nextech.erp.newDTO.UserDTO;
import com.nextech.erp.service.MailService;
import com.nextech.erp.service.NotificationService;
import com.nextech.erp.service.NotificationUserAssociationService;
import com.nextech.erp.service.UserService;

public class UserFactory {

	//TODO CALL TO ADD USER CONTROLLER
	
	@Autowired
	UserService userService;
	
	@Autowired
	static
	NotificationService notificationService;
	
	@Autowired
	NotificationUserAssociationService notificationUserAssService;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	static
	UserDao userDao;
	

	public static User saveUser(UserDTO userDTO) throws Exception{
		User user = new User();
		user.setUserid(userDTO.getUserid());
		user.setPassword(userDTO.getPassword());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setDob(userDTO.getDob());
		user.setDoj(userDTO.getDoj());
		user.setEmail(userDTO.getEmail());
		user.setUsertype(userDTO.getUsertype());
		userDao.add(user);
		return user;
	}
	public void mailSending(User user,HttpServletRequest request) throws NumberFormatException, Exception{
		  Mail mail = new Mail();

		  Notification notification = notificationService.getEntityById(Notification.class,5);
		  List<Notificationuserassociation> notificationuserassociations = notificationUserAssService.getNotificationuserassociationBynotificationId(notification.getId());
		  for (Notificationuserassociation notificationuserassociation : notificationuserassociations) {
			  User user1 = userService.getEmailUserById(notificationuserassociation.getUser().getId());
			  if(notificationuserassociation.getTo()==true){
				  mail.setMailTo(user.getEmail());
			  }else if(notificationuserassociation.getBcc()==true){
				  mail.setMailBcc(user1.getEmail());
			  }else if(notificationuserassociation.getCc()==true){
				  mail.setMailCc(user1.getEmail());
			  }
			
		}
		        mail.setMailSubject(notification.getSubject());
		        Map < String, Object > model = new HashMap < String, Object > ();
		        model.put("firstName", user.getFirstName());
		        model.put("lastName", user.getLastName());
		        model.put("userId", user.getUserid());
		        model.put("password", user.getPassword());
		        model.put("email", user.getEmail());
		        model.put("location", "Pune");
		        model.put("signature", "www.NextechServices.in");
		        mail.setModel(model);
		        mailService.sendEmailWithoutPdF(mail, notification);
}
	private  void mailSendingUpdate(User user,HttpServletRequest request,HttpServletResponse response) throws NumberFormatException, Exception{
		  Mail mail = new Mail();

		  Notification notification = notificationService.getEntityById(Notification.class,16);
		  List<Notificationuserassociation> notificationuserassociations = notificationUserAssService.getNotificationuserassociationBynotificationId(notification.getId());
		  for (Notificationuserassociation notificationuserassociation : notificationuserassociations) {
			  User user1 = userService.getEmailUserById(notificationuserassociation.getUser().getId());
			  if(notificationuserassociation.getTo()==true){
				  mail.setMailTo(user.getEmail());
			  }else if(notificationuserassociation.getBcc()==true){
				  mail.setMailBcc(user1.getEmail());
			  }else if(notificationuserassociation.getCc()==true){
				  mail.setMailCc(user1.getEmail());
			  }
			
		}
		        mail.setMailSubject(notification.getSubject());
		        Map < String, Object > model = new HashMap < String, Object > ();
		        model.put("firstName", user.getFirstName());
		        model.put("lastName", user.getLastName());
		        model.put("userId", user.getUserid());
		        model.put("password", user.getPassword());
		        model.put("email", user.getEmail());
		        model.put("location", "Pune");
		        model.put("signature", "www.NextechServices.in");
		        mail.setModel(model);
		        mailService.sendEmailWithoutPdF(mail, notification);
  }
}
