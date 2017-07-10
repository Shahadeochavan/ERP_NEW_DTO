package com.nextech.erp.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextech.erp.dao.UsertypepageassociationDao;
import com.nextech.erp.model.Usertypepageassociation;
import com.nextech.erp.newDTO.UserTypePageAssoDTO;
import com.nextech.erp.service.UsertypepageassociationService;
@Service
public class UsertypepageassociationServiceImpl extends CRUDServiceImpl<Usertypepageassociation> implements
		UsertypepageassociationService {

	@Autowired
	UsertypepageassociationDao usertypepageassociationDao;
	
	@Override
	public List<Usertypepageassociation> getPagesByUsertype(long usertypeId) {
		return usertypepageassociationDao.getPagesByUsertype(usertypeId);
	}

	@Override
	public boolean checkPageAccess(long usertypeId, long pageId) {
		return usertypepageassociationDao.checkPageAccess(usertypeId, pageId);
	}

	@Override
	public Usertypepageassociation saveUserTypePageAsso(
			UserTypePageAssoDTO userTypePageAssoDTO,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Usertypepageassociation usertypepageassociation = new Usertypepageassociation();
		usertypepageassociation.setPage(userTypePageAssoDTO.getPage());
		usertypepageassociation.setUsertype(userTypePageAssoDTO.getUsertype());
		usertypepageassociation.setIsactive(true);
		usertypepageassociation.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		usertypepageassociationDao.add(usertypepageassociation);
		return usertypepageassociation;
	}

	@Override
	public Usertypepageassociation updateUserTypePageAsso(
			UserTypePageAssoDTO userTypePageAssoDTO, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		Usertypepageassociation usertypepageassociation = new Usertypepageassociation();
		usertypepageassociation.setId(userTypePageAssoDTO.getId());
		usertypepageassociation.setPage(userTypePageAssoDTO.getPage());
		usertypepageassociation.setUsertype(userTypePageAssoDTO.getUsertype());
		usertypepageassociation.setIsactive(true);
		usertypepageassociation.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		usertypepageassociationDao.update(usertypepageassociation);
		return usertypepageassociation;
	}

}
