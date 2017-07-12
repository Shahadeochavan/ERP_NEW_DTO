package com.nextech.erp.factory;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Product;
import com.nextech.erp.newDTO.ProductDTO;

public class ProductFactory {
	
	public static Product getProduct(ProductDTO productDTO,HttpServletRequest request){
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setClientpartnumber(productDTO.getClientpartnumber());
		product.setDescription(productDTO.getDescription());
		product.setDesign(productDTO.getDesign());
		product.setPartNumber(productDTO.getPartNumber());
		product.setName(productDTO.getName());
		product.setIsactive(true);
		return product;
	}

}
