package com.nextech.erp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Product;
import com.nextech.erp.newDTO.ProductDTO;
public interface ProductService extends CRUDService<Product>{

	public Product getProductByName(String name) throws Exception;
	
	public Product getProductByPartNumber(String partnumber) throws Exception;
	
	public boolean isOrderPartiallyDispatched(long orderId) throws Exception;

	public Product getProductListByProductId(long id);
	
	public List<Product> getProductList(List<Long> productIdList);
	
	public Product saveProduct(ProductDTO productDTO,HttpServletRequest request) throws Exception;
	
	public Product updateProduct(ProductDTO productDTO,HttpServletRequest request) throws Exception;
	
	
}
