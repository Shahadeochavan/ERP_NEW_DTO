package com.nextech.erp.factory;

import javax.servlet.http.HttpServletRequest;
import com.nextech.erp.model.Vendor;
import com.nextech.erp.newDTO.VendorDTO;

public class VendorFactory {
	
	public static Vendor getVendor(VendorDTO  vendorDTO,HttpServletRequest request){
		Vendor vendor = new Vendor();
		vendor.setId(vendorDTO.getId());
		vendor.setAddress(vendorDTO.getAddress());
		vendor.setCity(vendorDTO.getCity());
		vendor.setCommisionerate(vendorDTO.getCommisionerate());
		vendor.setCompanyName(vendorDTO.getCompanyName());
		vendor.setContactNumberMobile(vendorDTO.getContactNumberOffice());
		vendor.setContactNumberOffice(vendorDTO.getContactNumberOffice());
		vendor.setCst(vendorDTO.getCst());
		vendor.setCustomerEccNumber(vendorDTO.getCustomerEccNumber());
		vendor.setDescription(vendorDTO.getDescription());
		vendor.setDivison(vendorDTO.getDivison());
		vendor.setEmail(vendorDTO.getEmail());
		vendor.setFirstName(vendorDTO.getFirstName());
		vendor.setLastName(vendorDTO.getLastName());
		vendor.setPostalcode(vendorDTO.getPostalcode());
		vendor.setRenge(vendorDTO.getRenge());
		vendor.setState(vendorDTO.getState());
		vendor.setVatNo(vendorDTO.getVatNo());
		vendor.setIsactive(true);
		return vendor;
	}

}
