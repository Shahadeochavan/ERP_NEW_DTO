package com.nextech.erp.serviceImpl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextech.erp.dao.ClientDao;
import com.nextech.erp.model.Client;
import com.nextech.erp.newDTO.ClientDTO;
import com.nextech.erp.service.ClientService;
@Service
public class ClientServiceImpl extends CRUDServiceImpl<Client> implements ClientService{

	@Autowired
	ClientDao clientDao;

	@Override
	public Client getClientByCompanyName(String companyName) throws Exception {
		return clientDao.getClientByCompanyName(companyName);
	}

	@Override
	public Client getClientByEmail(String email) throws Exception {
		return clientDao.getClientByEmail(email);
	}

	@Override
	public Client saveClient(ClientDTO clientDTO, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		Client client = new Client();
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
		client.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		clientDao.add(client);
		return client;
	}

	@Override
	public Client updateClient(ClientDTO clientDTO, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
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
		client.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		clientDao.update(client);
		return client;
	}
}

