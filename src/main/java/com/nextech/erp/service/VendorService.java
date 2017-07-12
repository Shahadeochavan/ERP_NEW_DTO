package com.nextech.erp.service;


import com.nextech.erp.model.Vendor;

public interface VendorService extends CRUDService<Vendor>{

	public Vendor getVendorByCompanyName(String companyName) throws Exception;

	public Vendor getVendorByEmail(String email) throws Exception;

	public Vendor getVendorByName(String vendorName) throws Exception;
}
