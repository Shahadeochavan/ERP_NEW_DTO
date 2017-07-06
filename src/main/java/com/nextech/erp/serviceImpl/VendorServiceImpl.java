package com.nextech.erp.serviceImpl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextech.erp.dao.VendorDao;
import com.nextech.erp.model.Vendor;
import com.nextech.erp.newDTO.VendorDTO;
import com.nextech.erp.service.VendorService;
@Service
public class VendorServiceImpl extends CRUDServiceImpl<Vendor> implements VendorService {

	@Autowired
	VendorDao vendorDao;

	@Override
	public Vendor getVendorByCompanyName(String companyName) throws Exception {
		return vendorDao.getVendorByCompanyName(companyName);
	}

	@Override
	public Vendor getVendorByEmail(String email) throws Exception {
		return vendorDao.getVendorByEmail(email);
	}

	@Override
	public Vendor getVendorByName(String vendorName) throws Exception {
		// TODO Auto-generated method stub
		return vendorDao.getVendorByName(vendorName);
	}

	@Override
	public Vendor saveVendor(VendorDTO vendorDTO, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		Vendor vendor = new Vendor();
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
		vendor.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		vendorDao.add(vendor);
		return vendor;
	}

	@Override
	public Vendor updateVendor(VendorDTO vendorDTO, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
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
		vendor.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		vendorDao.update(vendor);
		return vendor;
	}

}
