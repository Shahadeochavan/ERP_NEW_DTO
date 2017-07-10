package com.nextech.erp.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.nextech.erp.dao.NotificationDao;
import com.nextech.erp.dao.NotificationUserassociationDao;
import com.nextech.erp.dao.UserDao;
import com.nextech.erp.dto.Mail;
import com.nextech.erp.model.Notification;
import com.nextech.erp.model.Notificationuserassociation;
import com.nextech.erp.model.User;
import com.nextech.erp.newDTO.UserDTO;
import com.nextech.erp.service.MailService;
import com.nextech.erp.service.UserService;
@Service
@Qualifier("userServiceImpl")
public class UserServiceImpl extends CRUDServiceImpl<User> implements UserService {
	
	@Autowired
	UserDao userdao;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired 
	NotificationDao notificationDao;
	
	@Autowired 
	NotificationUserassociationDao notificationUserassociationDao;
	
	@Autowired
	MailService mailService;
	
	

	@Override
	public User findByUserId(String string) throws Exception {
		return null;
	}

	@Override
	public User getUserByUserId(String userid) throws Exception {
		return userdao.getUserByUserId(userid);
	}

	@Override
	public User getUserByEmail(String email) throws Exception {
		return userdao.getUserByEmail(email);
	}

	@Override
	public User getUserByMobile(String mobile) throws Exception {
		return userdao.getUserByMobile(mobile);
	}

	@Override
	public List<User> getUserProfileByUserId(long id) throws Exception {
		// TODO Auto-generated method stub
		return userdao.getUserProfileByUserId(id);
	}

	@Override
	public User getUserByFirstNamLastName(String firstName,String lastName) throws Exception {
		// TODO Auto-generated method stub
		return userdao.getUserByFirstNamLastName(firstName, lastName);
	}

	@Override
	public User getUserByNotifictionId(long notificatinId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getEmailUserById(long id) throws Exception {
		// TODO Auto-generated method stub
		return userdao.getEmailUserById(id);
	}

	@Override
	public User getUserByContact(String contact) throws Exception {
		// TODO Auto-generated method stub
		return userdao.getUserByContact(contact);
	}

	@Override
	public User saveUser(UserDTO userDTO,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		User user = new User();
		user.setUserid(userDTO.getUserid());
		user.setPassword(userDTO.getPassword());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setDob(userDTO.getDob());
		user.setDoj(userDTO.getDoj());
		user.setEmail(userDTO.getEmail());
		user.setUsertype(userDTO.getUsertype());
		user.setIsactive(true);
		userdao.add(user);
		mailSending(user, request);
		return user;
	}
	public void mailSending(User user,HttpServletRequest request) throws NumberFormatException, Exception{
		  Mail mail = new Mail();

		  Notification notification = notificationDao.getById(Notification.class,5);
		  List<Notificationuserassociation> notificationuserassociations = notificationUserassociationDao.getNotificationuserassociationBynotificationId(notification.getId());
		  for (Notificationuserassociation notificationuserassociation : notificationuserassociations) {
			  User user1 = userdao.getEmailUserById(notificationuserassociation.getUser().getId());
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

	@Override
	public User updateUser(UserDTO userDTO, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		User user = new User();
		user.setId(userDTO.getId());
		user.setUserid(userDTO.getUserid());
		user.setPassword(userDTO.getPassword());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setDob(userDTO.getDob());
		user.setDoj(userDTO.getDoj());
		user.setEmail(userDTO.getEmail());
		user.setUsertype(userDTO.getUsertype());
		user.setIsactive(true);
		userdao.add(user);
		mailSendingUpdate(user, request);
		return user;
	}
	private void mailSendingUpdate(User user,HttpServletRequest request) throws NumberFormatException, Exception{
		  Mail mail = new Mail();

		  Notification notification = notificationDao.getById(Notification.class,16);
		  List<Notificationuserassociation> notificationuserassociations = notificationUserassociationDao.getNotificationuserassociationBynotificationId(notification.getId());
		  for (Notificationuserassociation notificationuserassociation : notificationuserassociations) {
			  User user1 = userdao.getEmailUserById(notificationuserassociation.getUser().getId());
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
