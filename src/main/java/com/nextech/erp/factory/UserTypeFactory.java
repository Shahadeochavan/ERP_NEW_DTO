package com.nextech.erp.factory;

import javax.servlet.http.HttpServletRequest;


import com.nextech.erp.model.Usertype;
import com.nextech.erp.newDTO.UserTypeDTO;

public class UserTypeFactory {
	
	public static Usertype getUserType(UserTypeDTO userTypeDTO,HttpServletRequest request){
		Usertype usertype = new Usertype();
		usertype.setId(userTypeDTO.getId());
		usertype.setDescription(userTypeDTO.getDescription());
		usertype.setUsertypeName(userTypeDTO.getUsertypeName());
		usertype.setIsactive(true);
		return usertype;
	}

}
