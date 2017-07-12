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


}
