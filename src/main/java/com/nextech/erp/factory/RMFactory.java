package com.nextech.erp.factory;

import com.nextech.erp.model.Rawmaterial;
import com.nextech.erp.newDTO.RawMaterialDTO;

public class RMFactory {
	
	public static Rawmaterial getRawMaterial(RawMaterialDTO rawMaterialDTO){
		Rawmaterial rawmaterial = new Rawmaterial();
		rawmaterial.setId(rawMaterialDTO.getId());
		rawmaterial.setDescription(rawMaterialDTO.getDescription());
		rawmaterial.setName(rawMaterialDTO.getName());
		rawmaterial.setPartNumber(rawMaterialDTO.getPartNumber());
		rawmaterial.setPricePerUnit(rawMaterialDTO.getPricePerUnit());
		rawmaterial.setUnit(rawMaterialDTO.getUnit());
		rawmaterial.setIsactive(true);
		return rawmaterial;
	}

}
