package com.nextech.erp.serviceImpl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextech.erp.dao.UserTypeDao;
import com.nextech.erp.model.Usertype;
import com.nextech.erp.newDTO.UserTypeDTO;
import com.nextech.erp.service.UserTypeService;
@Service
public class UserTypeServiceImpl extends CRUDServiceImpl<Usertype> implements UserTypeService {

	@Autowired
	UserTypeDao userTypeDao;
	@Override
	public Usertype saveUserType(UserTypeDTO userTypeDTO,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Usertype usertype = new Usertype();
		usertype.setDescription(userTypeDTO.getDescription());
		usertype.setUsertypeName(userTypeDTO.getUsertypeName());
		usertype.setIsactive(true);
		usertype.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		userTypeDao.add(usertype);
		return usertype;
	}
	@Override
	public Usertype updateUserType(UserTypeDTO userTypeDTO,
			HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Usertype usertype = new Usertype();
		usertype.setId(userTypeDTO.getId());
		usertype.setDescription(userTypeDTO.getDescription());
		usertype.setUsertypeName(userTypeDTO.getUsertypeName());
		usertype.setIsactive(true);
		usertype.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		userTypeDao.update(usertype);
		return usertype;
	}

}
