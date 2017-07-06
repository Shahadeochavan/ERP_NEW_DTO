package com.nextech.erp.service;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Usertype;
import com.nextech.erp.newDTO.UserTypeDTO;

public interface UserTypeService extends CRUDService<Usertype>{
	
	public Usertype  saveUserType(UserTypeDTO userTypeDTO,HttpServletRequest request) throws Exception;
	
	public Usertype  updateUserType(UserTypeDTO userTypeDTO,HttpServletRequest request) throws Exception;

}
