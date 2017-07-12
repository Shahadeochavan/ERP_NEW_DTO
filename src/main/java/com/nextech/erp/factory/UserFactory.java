package com.nextech.erp.factory;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.User;
import com.nextech.erp.newDTO.UserDTO;



public class UserFactory {

	//TODO CALL TO ADD USER CONTROLLER
	public static User getUser(UserDTO userDTO,HttpServletRequest request) throws Exception{
		User user = new User();
		user.setId(userDTO.getId());
		user.setUserid(userDTO.getUserid());
		user.setPassword(userDTO.getPassword());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setMobile(userDTO.getMobile());
		user.setDob(userDTO.getDob());
		user.setDoj(userDTO.getDoj());
		user.setEmail(userDTO.getEmail());
		user.setUsertype(userDTO.getUsertype());
		user.setIsactive(true);
		return user;
	}

}
