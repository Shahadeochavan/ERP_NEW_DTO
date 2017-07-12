package com.nextech.erp.factory;

import com.nextech.erp.model.Rawmaterialorderinvoice;
import com.nextech.erp.newDTO.RMOrderInvoiceDTO;

public class RMOrdeInvoiceFactory {
	
	public static Rawmaterialorderinvoice getRMorderInvoice(RMOrderInvoiceDTO rawOrderInvoiceDTO){
		Rawmaterialorderinvoice rawmaterialorderinvoice = new Rawmaterialorderinvoice();
		rawmaterialorderinvoice.setDescription(rawOrderInvoiceDTO.getDescription());
		rawmaterialorderinvoice.setFirstName(rawOrderInvoiceDTO.getFirstName());
		rawmaterialorderinvoice.setId(rawOrderInvoiceDTO.getId());
		rawmaterialorderinvoice.setIntime(rawOrderInvoiceDTO.getIntime());
		rawmaterialorderinvoice.setInvoice_No(rawOrderInvoiceDTO.getInvoice_No());
		rawmaterialorderinvoice.setLastName(rawOrderInvoiceDTO.getLastName());
		rawmaterialorderinvoice.setLicence_no(rawOrderInvoiceDTO.getLicence_no());
		rawmaterialorderinvoice.setOuttime(rawOrderInvoiceDTO.getOuttime());
		rawmaterialorderinvoice.setPo_No(rawOrderInvoiceDTO.getPo_No());
		rawmaterialorderinvoice.setVehicleNo(rawOrderInvoiceDTO.getVehicleNo());
		rawmaterialorderinvoice.setVendorname(rawOrderInvoiceDTO.getVendorname());
		rawmaterialorderinvoice.setIsactive(true);
		return rawmaterialorderinvoice;
	}

}
