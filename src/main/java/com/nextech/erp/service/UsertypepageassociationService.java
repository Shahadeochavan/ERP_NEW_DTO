package com.nextech.erp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Usertypepageassociation;
import com.nextech.erp.newDTO.UserTypePageAssoDTO;

public interface UsertypepageassociationService extends CRUDService<Usertypepageassociation>{
	public List<Usertypepageassociation> getPagesByUsertype(long usertypeId);
	public boolean checkPageAccess(long usertypeId,long pageId);
	
	public Usertypepageassociation saveUserTypePageAsso(UserTypePageAssoDTO userTypePageAssoDTO,HttpServletRequest request) throws Exception;
	
	public Usertypepageassociation updateUserTypePageAsso(UserTypePageAssoDTO userTypePageAssoDTO,HttpServletRequest request) throws Exception;
	
	
}
