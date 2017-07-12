package com.nextech.erp.dto;

import com.nextech.erp.model.Product;

public class ProductInventoryDTO {
	private String productPartNumber;
	private long inventoryQuantity;
	private long minimum_quantity;
	private long id;
	private String description;
	private long maximum_quantity;
	private String name;
	private long quantityavailable;
	private long racknumber;
	private Product product;
	public String getProductPartNumber() {
		return productPartNumber;
	}
	public void setProductPartNumber(String productPartNumber) {
		this.productPartNumber = productPartNumber;
	}
	public long getInventoryQuantity() {
		return inventoryQuantity;
	}
	public void setInventoryQuantity(long inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}
	public long getMinimum_quantity() {
		return minimum_quantity;
	}
	public void setMinimum_quantity(long minimum_quantity) {
		this.minimum_quantity = minimum_quantity;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getMaximum_quantity() {
		return maximum_quantity;
	}
	public void setMaximum_quantity(long maximum_quantity) {
		this.maximum_quantity = maximum_quantity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getQuantityavailable() {
		return quantityavailable;
	}
	public void setQuantityavailable(long quantityavailable) {
		this.quantityavailable = quantityavailable;
	}
	public long getRacknumber() {
		return racknumber;
	}
	public void setRacknumber(long racknumber) {
		this.racknumber = racknumber;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

}
