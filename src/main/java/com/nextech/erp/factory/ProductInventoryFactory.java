package com.nextech.erp.factory;

import com.nextech.erp.dto.ProductInventoryDTO;
import com.nextech.erp.model.Productinventory;

public class ProductInventoryFactory {
	
	public static Productinventory getProductInventory(ProductInventoryDTO productInventoryDTO){
		Productinventory productinventory = new Productinventory();
		productinventory.setId(productInventoryDTO.getId());
		productinventory.setDescription(productInventoryDTO.getDescription());
		productinventory.setMaximum_quantity(productInventoryDTO.getMaximum_quantity());
		productinventory.setMinimum_quantity(productInventoryDTO.getMinimum_quantity());
		productinventory.setName(productInventoryDTO.getName());
		productinventory.setProduct(productInventoryDTO.getProduct());
		productinventory.setQuantityavailable(productInventoryDTO.getQuantityavailable());
		productinventory.setRacknumber(productInventoryDTO.getRacknumber());
		productinventory.setIsactive(true);
		return productinventory;
	}

}
