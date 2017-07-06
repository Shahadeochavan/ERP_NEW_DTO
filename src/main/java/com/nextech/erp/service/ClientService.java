package com.nextech.erp.service;

import javax.servlet.http.HttpServletRequest;

import com.nextech.erp.model.Client;
import com.nextech.erp.newDTO.ClientDTO;

public interface ClientService extends CRUDService<Client>{

	public Client getClientByCompanyName(String companyName) throws Exception;

	public Client getClientByEmail(String email) throws Exception;
	
	public Client saveClient(ClientDTO clientDTO,HttpServletRequest request) throws Exception;
	
	public Client updateClient(ClientDTO clientDTO,HttpServletRequest request) throws Exception;
}
