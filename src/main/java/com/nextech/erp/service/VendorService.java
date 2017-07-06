package com.nextech.erp.service;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Vendor;
import com.nextech.erp.newDTO.VendorDTO;

public interface VendorService extends CRUDService<Vendor>{

	public Vendor getVendorByCompanyName(String companyName) throws Exception;

	public Vendor getVendorByEmail(String email) throws Exception;

	public Vendor getVendorByName(String vendorName) throws Exception;
	
	public Vendor saveVendor(VendorDTO vendorDTO,HttpServletRequest request) throws Exception;
	
	public Vendor updateVendor(VendorDTO vendorDTO,HttpServletRequest request) throws Exception;
}
