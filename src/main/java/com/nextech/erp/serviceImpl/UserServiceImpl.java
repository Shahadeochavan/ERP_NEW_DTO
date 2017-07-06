package com.nextech.erp.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.nextech.erp.constants.ERPConstants;
import com.nextech.erp.dao.UserDao;
import com.nextech.erp.factory.UserFactory;
import com.nextech.erp.model.User;
import com.nextech.erp.newDTO.UserDTO;
import com.nextech.erp.service.UserService;
import com.nextech.erp.status.UserStatus;
@Service
@Qualifier("userServiceImpl")
public class UserServiceImpl extends CRUDServiceImpl<User> implements UserService {
	
	@Autowired
	UserDao userdao;
	
	@Autowired
	private MessageSource messageSource;

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
		UserFactory userFactory = new UserFactory();
		userFactory.mailSending(user, request);
		return user;
	}
	
}
