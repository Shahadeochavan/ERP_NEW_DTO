package com.nextech.erp.service;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Unit;
import com.nextech.erp.newDTO.UnitDTO;

public interface UnitService extends CRUDService<Unit>{
	
	public void saveUnit(UnitDTO unitDTO,HttpServletRequest request) throws Exception;
	
	public void updateUnit(UnitDTO unitDTO,HttpServletRequest request) throws Exception;
	
}
