package com.nextech.erp.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.nextech.erp.dao.BomDao;
import com.nextech.erp.model.Bom;
import com.nextech.erp.service.BomService;

public class BomServiceImpl extends CRUDServiceImpl<Bom> implements BomService{

	@Autowired
	BomDao bomDao;
	@Override
	public List<Bom> getBomListByProductId(long productID) throws Exception {
		// TODO Auto-generated method stub
		return bomDao.getBomListByProductId(productID);
	}

}
