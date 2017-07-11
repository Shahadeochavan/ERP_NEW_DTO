package com.nextech.erp.serviceImpl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextech.erp.dao.UnitDao;
import com.nextech.erp.factory.UnitFactory;
import com.nextech.erp.model.Unit;
import com.nextech.erp.newDTO.UnitDTO;
import com.nextech.erp.service.UnitService;
@Service
public class UnitServiceImpl extends CRUDServiceImpl<Unit> implements UnitService {

	@Autowired
	UnitDao unitDao;
	@Override
	public void saveUnit(UnitDTO unitDTO,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		Unit unit = new Unit();
		unit.setDescription(unitDTO.getDescription());
		unit.setName(unitDTO.getName());
		unit.setIsactive(true);
		unit.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		unitDao.add(unit);
	}
	@Override
	public void updateUnit(UnitDTO unitDTO, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		Unit unit = new Unit();
		unit.setId(unitDTO.getId());
		unit.setDescription(unitDTO.getDescription());
		unit.setName(unitDTO.getName());
		unit.setIsactive(true);
		unit.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		unitDao.update(unit);
	}

}
