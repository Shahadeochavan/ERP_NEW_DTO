package com.nextech.erp.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextech.erp.dao.ProductDao;
import com.nextech.erp.dao.ProductorderDao;
import com.nextech.erp.model.Product;
import com.nextech.erp.model.Productorder;
import com.nextech.erp.model.Productorderassociation;
import com.nextech.erp.newDTO.ProductDTO;
import com.nextech.erp.service.ProductService;
@Service
public class ProductServiceImpl extends CRUDServiceImpl<Product> implements ProductService {

	@Autowired
	ProductDao productDao;

	@Autowired
	ProductorderDao productorderDao; 
	
	@Override
	public Product getProductByName(String name) throws Exception {
		return productDao.getProductByName(name);
	}

	@Override
	public Product getProductByPartNumber(String partnumber) throws Exception {
		return productDao.getProductByPartNumber(partnumber);
	}

	@Override
	public boolean isOrderPartiallyDispatched(long orderId) throws Exception {
		boolean isDispatched = false;
		Productorder productorder = productorderDao.getProductorderByProductOrderId(orderId);
		for (Productorderassociation productorderassociation : productorder.getOrderproductassociations()) {
			if(productorderassociation.getRemainingQuantity() >= 0){
				isDispatched = true;
				break;
			}
		}
		return isDispatched;
	}

	@Override
	public Product getProductListByProductId(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductList(List<Long> productIdList) {
		// TODO Auto-generated method stub
		return productDao.getProductList(productIdList);
	}

	@Override
	public Product saveProduct(ProductDTO productDTO, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		Product product = new Product();
		product.setClientpartnumber(productDTO.getClientpartnumber());
		product.setDescription(productDTO.getDescription());
		product.setDesign(productDTO.getDesign());
		product.setPartNumber(productDTO.getPartNumber());
		product.setName(productDTO.getName());
		product.setIsactive(true);
		product.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		productDao.add(product);
		return product;
	}

	@Override
	public Product updateProduct(ProductDTO productDTO, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setClientpartnumber(productDTO.getClientpartnumber());
		product.setDescription(productDTO.getDescription());
		product.setDesign(productDTO.getDesign());
		product.setPartNumber(productDTO.getPartNumber());
		product.setName(productDTO.getName());
		product.setIsactive(true);
		product.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		productDao.update(product);
		return product;
	}
}
