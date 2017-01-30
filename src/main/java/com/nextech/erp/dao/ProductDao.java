package com.nextech.erp.dao;

import java.util.List;

import com.nextech.erp.model.Product;

public interface ProductDao {
	public boolean addProduct(Product product) throws Exception;

	public Product getProductById(long id) throws Exception;

	public List<Product> getProductist() throws Exception;

	public boolean deleteProduct(long id) throws Exception;

	public Product updateProduct(Product product) throws Exception;
}
