package com.nextech.erp.factory;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Usertypepageassociation;
import com.nextech.erp.newDTO.UserTypePageAssoDTO;

public class UserTypePageAssoFactory {

	public static  Usertypepageassociation getUserTypePageAss(UserTypePageAssoDTO userTypePageAssoDTO, HttpServletRequest request){
		Usertypepageassociation usertypepageassociation = new Usertypepageassociation();
		usertypepageassociation.setId(userTypePageAssoDTO.getId());
		usertypepageassociation.setPage(userTypePageAssoDTO.getPage());
		usertypepageassociation.setUsertype(userTypePageAssoDTO.getUsertype());
		usertypepageassociation.setIsactive(true);
		return usertypepageassociation;
	}
}
