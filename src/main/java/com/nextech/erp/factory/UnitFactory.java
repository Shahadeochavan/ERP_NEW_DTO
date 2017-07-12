package com.nextech.erp.factory;



import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Unit;
import com.nextech.erp.newDTO.UnitDTO;

public class UnitFactory {
	
	public static Unit getUnit(UnitDTO unitDTO,HttpServletRequest request){
		Unit unit = new Unit();
		unit.setId(unitDTO.getId());
		unit.setDescription(unitDTO.getDescription());
		unit.setName(unitDTO.getName());
		unit.setIsactive(true);
		return unit;
	}

}
