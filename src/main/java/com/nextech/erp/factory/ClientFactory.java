package com.nextech.erp.factory;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Client;
import com.nextech.erp.newDTO.ClientDTO;

public class ClientFactory {

	public static Client getClient( ClientDTO clientDTO,HttpServletRequest request){
		Client client = new Client();
		client.setId(clientDTO.getId());
		client.setAddress(clientDTO.getAddress());
		client.setCommisionerate(clientDTO.getCommisionerate());
		client.setCompanyname(clientDTO.getCompanyname());
		client.setContactnumber(clientDTO.getContactnumber());
		client.setContactpersonname(clientDTO.getContactpersonname());
		client.setCst(clientDTO.getCst());
		client.setCustomerEccNumber(clientDTO.getCustomerEccNumber());
		client.setDescription(clientDTO.getDescription());
		client.setDivision(clientDTO.getDivision());
		client.setEmailid(clientDTO.getEmailid());
		client.setRenge(clientDTO.getRenge());
		client.setVatNo(clientDTO.getVatNo());
		client.setIsactive(true);
		return client;
	}
	
}
