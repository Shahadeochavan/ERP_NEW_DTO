package com.nextech.erp.service;

import java.util.List;

import com.nextech.erp.model.Bom;

public interface BomService extends CRUDService<Bom> {
	
	public List<Bom> getBomListByProductId(long productID) throws Exception;

}
