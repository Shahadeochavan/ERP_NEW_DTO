package com.nextech.erp.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nextech.erp.dao.UsertypepageassociationDao;
import com.nextech.erp.model.Usertypepageassociation;
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

}
