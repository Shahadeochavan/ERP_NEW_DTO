package com.nextech.erp.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nextech.erp.constants.ERPConstants;
import com.nextech.erp.dto.Mail;
import com.nextech.erp.dto.QualityCheckRMDTO;
import com.nextech.erp.exceptions.InvalidRMQuantityInQC;
import com.nextech.erp.model.Notification;
import com.nextech.erp.model.Notificationuserassociation;
import com.nextech.erp.model.Qualitycheckrawmaterial;
import com.nextech.erp.model.Rawmaterial;
import com.nextech.erp.model.Rawmaterialinventory;
import com.nextech.erp.model.Rawmaterialinventoryhistory;
import com.nextech.erp.model.Rawmaterialorder;
import com.nextech.erp.model.Rawmaterialorderassociation;
import com.nextech.erp.model.Rawmaterialorderhistory;
import com.nextech.erp.model.Rawmaterialorderinvoice;
import com.nextech.erp.model.Rmorderinvoiceintakquantity;
import com.nextech.erp.model.Status;
import com.nextech.erp.model.User;
import com.nextech.erp.model.Vendor;
import com.nextech.erp.service.MailService;
import com.nextech.erp.service.NotificationService;
import com.nextech.erp.service.NotificationUserAssociationService;
import com.nextech.erp.service.QualitycheckrawmaterialService;
import com.nextech.erp.service.RawmaterialService;
import com.nextech.erp.service.RawmaterialinventoryService;
import com.nextech.erp.service.RawmaterialinventoryhistoryService;
import com.nextech.erp.service.RawmaterialorderService;
import com.nextech.erp.service.RawmaterialorderassociationService;
import com.nextech.erp.service.RawmaterialorderhistoryService;
import com.nextech.erp.service.RawmaterialorderinvoiceService;
import com.nextech.erp.service.RawmaterialorderinvoiceassociationService;
import com.nextech.erp.service.RmorderinvoiceintakquantityService;
import com.nextech.erp.service.StatusService;
import com.nextech.erp.service.UserService;
import com.nextech.erp.service.VendorService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/qualitycheckrawmaterial")
public class QualitycheckrawmaterialController {
	@Autowired
	QualitycheckrawmaterialService qualitycheckrawmaterialService;

	@Autowired
	RmorderinvoiceintakquantityService rmorderinvoiceintakquantityService;

	@Autowired
	RawmaterialService rawmaterialService;

	@Autowired
	RawmaterialorderinvoiceService rawmaterialorderinvoiceService;

	@Autowired
	RawmaterialorderhistoryService rawmaterialorderhistoryService;

	@Autowired
	RawmaterialinventoryhistoryService rawmaterialinventoryhistoryService;

	@Autowired
	RawmaterialorderService rawmaterialorderService;

	@Autowired
	RawmaterialinventoryService rawmaterialinventoryService;

	@Autowired
	StatusService statusService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	VendorService vendorService;

	@Autowired
	MailService mailService;
	
	@Autowired
	UserService userService;
	
	@Autowired 
	NotificationUserAssociationService notificationUserAssociationService;

	@Autowired
	RawmaterialorderinvoiceassociationService rawmaterialorderinvoiceassociationService;

	@Autowired
	RawmaterialorderassociationService rawmaterialorderassociationService;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private QualitycheckrawmaterialService qcRMService;

	private HashMap<Long,Integer> rmIdQuantityMap;

	private static final int STATUS_RAW_MATERIAL_ORDER_INCOMPLETE=2;
	private static final int STATUS_RAW_MATERIAL_ORDER_COMPLETE=3;

	@RequestMapping(value = "/qualitycheck", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	@Transactional
	public @ResponseBody UserStatus addRawmaterialorderinvoice(
			@Valid @RequestBody Rawmaterialorderinvoice rawmaterialorderinvoice,
			BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError().getDefaultMessage());
			}
			String message = "";
			Rawmaterialorderinvoice rawmaterialorderinvoiceNew = rawmaterialorderinvoiceService.getEntityById(Rawmaterialorderinvoice.class,rawmaterialorderinvoice.getId());
			Rawmaterialorder rawmaterialorder = rawmaterialorderService.getEntityById(Rawmaterialorder.class, rawmaterialorderinvoiceNew.getPo_No());
			List<Qualitycheckrawmaterial> qualitycheckrawmaterials = rawmaterialorderinvoice.getQualitycheckrawmaterials();
			List<QualityCheckRMDTO>  qualityCheckRMDTOs = new ArrayList<QualityCheckRMDTO>();
			if( rawmaterialorder == null ){
				System.out.println("==== RM Order not found for this Invoice : " + rawmaterialorderinvoiceNew.getInvoice_No() + " . So need to clear out this Invoice");
				for (Qualitycheckrawmaterial qualitycheckrawmaterial : qualitycheckrawmaterials) {
					qcRMService.deleteEntity(Qualitycheckrawmaterial.class, qualitycheckrawmaterial.getRawmaterial().getId());
				}
				rawmaterialorderinvoiceService.deleteEntity(Rawmaterialorderinvoice.class,rawmaterialorderinvoice.getId());
				return new UserStatus(0, messageSource.getMessage(ERPConstants.QC_RM_ORDER_NOT_FOUND, null, null));
			}else if (qualitycheckrawmaterials != null && !qualitycheckrawmaterials.isEmpty()) {
				for (Qualitycheckrawmaterial qualitycheckrawmaterial : qualitycheckrawmaterials) {
					//TODO why are we getting raw material from Raw Material service
					Rawmaterial rawmaterial = rawmaterialService.getEntityById(Rawmaterial.class, qualitycheckrawmaterial.getRawmaterial().getId());
					//TODO save Quality check
					long qualityCheckId = saveQualityCheckRawMaterial(qualitycheckrawmaterial, rawmaterialorderinvoiceNew, rawmaterial, request, response);
					QualityCheckRMDTO qualityCheckRMDTO = new QualityCheckRMDTO();
					qualityCheckRMDTO.setDescription(rawmaterial.getDescription());
					qualityCheckRMDTO.setGoodQuantity(qualitycheckrawmaterial.getGoodQuantity());
					qualityCheckRMDTO.setIntakeQuantity(qualitycheckrawmaterial.getIntakeQuantity());
					qualityCheckRMDTOs.add(qualityCheckRMDTO);
					//if qualityCheckId value is 0 that means this is duplicate entry
					if(qualityCheckId != 0 ){
						//TODO update raw material invoice
						updateRawMaterialInvoice(ERPConstants.STATUS_READY_STORE_IN,rawmaterialorderinvoiceNew, request, response);
						//TODO update raw material order
						updateRMOrderRemainingQuantity(qualitycheckrawmaterial, rawmaterialorder, request, response);
						updateRMIdQuantityMap(qualitycheckrawmaterial.getRawmaterial().getId(), qualitycheckrawmaterial.getIntakeQuantity() - qualitycheckrawmaterial.getGoodQuantity());
						message = messageSource.getMessage(ERPConstants.RM_QUALITY_CHECK, null, null);
					}else{
						System.out.println("==== Invoice id = " + rawmaterialorderinvoiceNew.getId() + " raw material id = " + qualitycheckrawmaterial.getRawmaterial().getId() + " already exists ====");
						message += " Invoice id = " + rawmaterialorderinvoiceNew.getId() + " raw material id = " + qualitycheckrawmaterial.getRawmaterial().getId() + " already exists";
					}
				}
			}else{
				System.out.println("==== This is blank invoice as there are no RMs present in Quality Check RM Table. So need to clear out this Invoice : " + rawmaterialorderinvoiceNew.getInvoice_No());
				rawmaterialorderinvoiceService.deleteEntity(Rawmaterialorderinvoice.class,rawmaterialorderinvoice.getId());
				return new UserStatus(0, messageSource.getMessage(ERPConstants.INFO_QUALITY_CHECK, null, null));
			}
			// TODO  call to order history
			addOrderHistory(rawmaterialorderinvoiceNew, rawmaterialorder, request, response);
			//TODO Update Raw Material Order Status
			updateRawMaterialOrderStatus(rawmaterialorder);
			
			Status status = statusService.getEntityById(Status.class, rawmaterialorderinvoiceNew.getStatus().getId());

			Vendor vendor = vendorService.getEntityById(Vendor.class, Long.parseLong(rawmaterialorderinvoiceNew.getVendorname()));

			Notification notification = notificationService.getNotifiactionByStatus(status.getId());
			mailSending(notification, vendor,qualityCheckRMDTOs,rawmaterialorder);


			// TODO  call to trigger notification (will do it later )
			return new UserStatus(1,
					message);
		} catch(InvalidRMQuantityInQC invalidRMQuantityInQC){
			invalidRMQuantityInQC.printStackTrace();
			return new UserStatus(0, invalidRMQuantityInQC.getCause().getMessage());
		}catch (ConstraintViolationException cve) {
			cve.printStackTrace();
			return new UserStatus(0, cve.getCause().getMessage());
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return new UserStatus(0, pe.getCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
	}

	@RequestMapping(value = "/qualityCheckInReadyStore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	@Transactional
	public @ResponseBody UserStatus addRawmaterialorderinvoiceReadyStore(
			@Valid @RequestBody Rawmaterialorderinvoice rawmaterialorderinvoice,
			BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError().getDefaultMessage());
			}
			Rawmaterialorderinvoice rawmaterialorderinvoiceNew = rawmaterialorderinvoiceService.getEntityById(Rawmaterialorderinvoice.class,rawmaterialorderinvoice.getId());
			Rawmaterialorder rawmaterialorder = rawmaterialorderService.getEntityById(Rawmaterialorder.class, rawmaterialorderinvoiceNew.getPo_No());
			List<Qualitycheckrawmaterial> qualitycheckrawmaterials = qualitycheckrawmaterialService.getQualitycheckrawmaterialByInvoiceId(rawmaterialorderinvoiceNew.getId());
			if (qualitycheckrawmaterials != null&& !qualitycheckrawmaterials.isEmpty()) {
				for (Qualitycheckrawmaterial qualitycheckrawmaterial : qualitycheckrawmaterials) {
					Rawmaterial rawmaterial = rawmaterialService.getRMByRMId(qualitycheckrawmaterial.getRawmaterial().getId());
					//TODO save Quality check
					//if qualityCheckId value is 0 that means this is duplicate entry
					// TODO  update inventory
					Rawmaterialinventory rawmaterialinventory = updateInventory(qualitycheckrawmaterial, rawmaterial, request, response);
					// TODO  call to inventory history
					addRMInventoryHistory(qualitycheckrawmaterial, rawmaterialinventory, request, response);
					//TODO update raw material invoice
					updateRawMaterialInvoice(ERPConstants.STATUS_RAW_MATERIAL_INVENTORY_ADD,rawmaterialorderinvoiceNew, request, response);
				}
			}
			// TODO  call to trigger notification (will do it later )
			return new UserStatus(1,"Store Quality Check information save succesfully");
		} catch (ConstraintViolationException cve) {
			cve.printStackTrace();
			return new UserStatus(0, cve.getCause().getMessage());
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			return new UserStatus(0, pe.getCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
	}


	private void updateRawMaterialOrderStatus(Rawmaterialorder rawmaterialorder) throws Exception{

		rawmaterialorder.setStatus(statusService.getEntityById(Status.class, getOrderStatus(rawmaterialorder)));
		rawmaterialorderService.updateEntity(rawmaterialorder);
	}

	private int getOrderStatus(Rawmaterialorder rawmaterialorder) throws Exception{
		boolean isOrderComplete = false;
		List<Rawmaterialorderassociation> rawmaterialorderassociationList  =rawmaterialorderassociationService.getRMOrderRMAssociationByRMOrderId(rawmaterialorder.getId());
		for (Iterator<Rawmaterialorderassociation> iterator = rawmaterialorderassociationList.iterator(); iterator
				.hasNext();) {
			Rawmaterialorderassociation rawmaterialorderassociation = (Rawmaterialorderassociation) iterator
					.next();
			if(rawmaterialorderassociation.getRemainingQuantity() == 0 ){
				isOrderComplete = true;
			}else{
				isOrderComplete = false;
				break;
			}

		}
		return isOrderComplete ? STATUS_RAW_MATERIAL_ORDER_COMPLETE : STATUS_RAW_MATERIAL_ORDER_INCOMPLETE;
	}

	private void updateRawMaterialInvoice(String invoiceStatus,Rawmaterialorderinvoice rawmaterialorderinvoice,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		//TODO why status RM ADD? Why can't we update status as RM Order Invoice QC Complete?
		Status statusInventoryAdd = statusService.getEntityById(Status.class,Long.parseLong(messageSource.getMessage(invoiceStatus, null, null)));
		rawmaterialorderinvoice.setStatus(statusInventoryAdd);
		rawmaterialorderinvoice.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		rawmaterialorderinvoiceService.updateEntity(rawmaterialorderinvoice);

		//TODO Mail Sending
		Status status = statusService.getEntityById(Status.class, rawmaterialorderinvoice.getStatus().getId());
		Vendor vendor = vendorService.getEntityById(Vendor.class, Long.parseLong(rawmaterialorderinvoice.getVendorname()));
		Notification notification = notificationService.getNotifiactionByStatus(status.getId());
		//mailSending(notification, vendor);
	}

	private long saveQualityCheckRawMaterial(Qualitycheckrawmaterial qualitycheckrawmaterial,Rawmaterialorderinvoice rawmaterialorderinvoiceNew,Rawmaterial rawmaterial,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String message = "";
		qualitycheckrawmaterial.setRawmaterialorderinvoice(rawmaterialorderinvoiceNew);
		qualitycheckrawmaterial.setRawmaterial(rawmaterial);
		qualitycheckrawmaterial.setGoodQuantity(qualitycheckrawmaterial.getGoodQuantity());
		qualitycheckrawmaterial.setIntakeQuantity(qualitycheckrawmaterial.getIntakeQuantity());
		qualitycheckrawmaterial.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		long rmOrderInvoiceId = qualitycheckrawmaterial.getRawmaterialorderinvoice().getId();
		long qcRMId = qualitycheckrawmaterial.getRawmaterial().getId();
		if(qualitycheckrawmaterialService.getQualitycheckrawmaterialByInvoiceIdAndRMId(rmOrderInvoiceId, qcRMId) == null){
			System.out.println("====== Creating Entry Qualitycheckrawmaterial Invoice id = " + rawmaterialorderinvoiceNew.getId() + " raw material id = " + qualitycheckrawmaterial.getRawmaterial().getId() + " =====");
			qualitycheckrawmaterial.setIsactive(true);
			qualitycheckrawmaterialService.addEntity(qualitycheckrawmaterial);
			Status status = statusService.getEntityById(Status.class, rawmaterialorderinvoiceNew.getStatus().getId());
			Vendor vendor = vendorService.getEntityById(Vendor.class, Long.parseLong(rawmaterialorderinvoiceNew.getVendorname()));
			Notification notification = notificationService.getNotifiactionByStatus(status.getId());

			//TODO Mail Sending to Vendor when quality check or rm order
			//mailSending(notification, vendor);
		}else {
			message += " Invoice id = " + rawmaterialorderinvoiceNew.getId() + " raw material id = " + qualitycheckrawmaterial.getRawmaterial().getId() + " already exists";
			System.out.println("====== Invoice id = " + rawmaterialorderinvoiceNew.getId() + " raw material id = " + qualitycheckrawmaterial.getRawmaterial().getId() + " already exists =====");
		}
		return qualitycheckrawmaterial.getId();
	}

	private Rawmaterialinventory updateInventory(Qualitycheckrawmaterial qualitycheckrawmaterial,Rawmaterial rawmaterial,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Rawmaterialinventory rawmaterialinventory =  rawmaterialinventoryService.getByRMId(qualitycheckrawmaterial.getRawmaterial().getId());
		if(rawmaterialinventory == null){
			rawmaterialinventory = new Rawmaterialinventory();
			rawmaterialinventory.setRawmaterial(rawmaterial);
			rawmaterialinventory.setIsactive(true);
			rawmaterialinventory.setQuantityAvailable(qualitycheckrawmaterial.getGoodQuantity());
			rawmaterialinventory.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			rawmaterialinventoryService.addEntity(rawmaterialinventory);
		}else{
			rawmaterialinventory.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			rawmaterialinventory.setIsactive(true);
			rawmaterialinventory.setQuantityAvailable(rawmaterialinventory.getQuantityAvailable()+qualitycheckrawmaterial.getGoodQuantity());
			rawmaterialinventory.setUpdatedDate(new Timestamp(new Date().getTime()));
			 rawmaterialinventoryService.updateEntity(rawmaterialinventory);
		}
		return rawmaterialinventory;
	}
	private void  updateRMOrderRemainingQuantity(Qualitycheckrawmaterial qualitycheckrawmaterial ,Rawmaterialorder rawmaterialorder,HttpServletRequest request,HttpServletResponse response) throws InvalidRMQuantityInQC,Exception{
		Rawmaterialorderassociation rawmaterialorderassociation = rawmaterialorderassociationService.getRMOrderRMAssociationByRMOrderIdandRMId(rawmaterialorder.getId(),qualitycheckrawmaterial.getRawmaterial().getId());
		if(rawmaterialorderassociation.getRemainingQuantity()-qualitycheckrawmaterial.getGoodQuantity() >= 0){
			rawmaterialorderassociation.setRemainingQuantity(rawmaterialorderassociation.getRemainingQuantity()-qualitycheckrawmaterial.getGoodQuantity());
		}else{
			throw new InvalidRMQuantityInQC("Good quantity exceeded than Remaining Quantity");
		}
		rawmaterialorderassociation.setUpdatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		rawmaterialorderassociationService.updateEntity(rawmaterialorderassociation);
	}

	private void addRMInventoryHistory(Qualitycheckrawmaterial qualitycheckrawmaterial,Rawmaterialinventory rawmaterialinventory,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Rawmaterialinventoryhistory rawmaterialinventoryhistory = new Rawmaterialinventoryhistory();
		rawmaterialinventoryhistory.setQualitycheckrawmaterial(qualitycheckrawmaterial);
		rawmaterialinventoryhistory.setRawmaterialinventory(rawmaterialinventory);
		rawmaterialinventoryhistory.setIsactive(true);
		rawmaterialinventoryhistory.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		rawmaterialinventoryhistory.setCreatedDate(new Timestamp(new Date().getTime()));
		rawmaterialinventoryhistory.setStatus(statusService.getEntityById(Status.class,Long.parseLong(messageSource.getMessage(ERPConstants.STATUS_RAW_MATERIAL_INVENTORY_ADD, null, null))));
		rawmaterialinventoryhistoryService.addEntity(rawmaterialinventoryhistory);
	}

	private void addOrderHistory(Rawmaterialorderinvoice rawmaterialorderinvoice,Rawmaterialorder rawmaterialorder,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Rawmaterialorderhistory rawmaterialorderhistory = new Rawmaterialorderhistory();
		rawmaterialorderhistory.setComment(rawmaterialorderinvoice.getDescription());
		rawmaterialorderhistory.setRawmaterialorder(rawmaterialorder);
		rawmaterialorderhistory.setRawmaterialorderinvoice(rawmaterialorderinvoice);
		rawmaterialorderhistory.setCreatedDate(new Timestamp(new Date().getTime()));
		rawmaterialorderhistory.setIsactive(true);
		rawmaterialorderhistory.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
		rawmaterialorderhistory.setQualitycheckrawmaterial(null);
		rawmaterialorderhistory.setStatus1(statusService.getEntityById(Status.class,rawmaterialorder.getStatus().getId()));
		rawmaterialorderhistory.setStatus2(statusService.getEntityById(Status.class, getOrderStatus(rawmaterialorder)));
		rawmaterialorderhistory.setCreatedBy(3);
		rawmaterialorderhistoryService.addEntity(rawmaterialorderhistory);
	}
	private void updateRMIdQuantityMap(long rmId,int quantity){
		if(rmIdQuantityMap == null){
			rmIdQuantityMap = new HashMap<Long, Integer>();
		}
		rmIdQuantityMap.put(rmId, quantity);

	}
	@RequestMapping(value = "listrm/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Rmorderinvoiceintakquantity> getRmorderinvoiceintakquantityByRMOInvoiceId(
			@PathVariable("id") long id) {
		List<Rmorderinvoiceintakquantity> rmorderinvoiceintakquantities = null;
		try {
			rmorderinvoiceintakquantities = rmorderinvoiceintakquantityService.getRmorderinvoiceintakquantityByRMOInvoiceId(id);
			System.out.println("list size "
					+ rmorderinvoiceintakquantities.size());
			for (Rmorderinvoiceintakquantity rmorderinvoiceintakquantity : rmorderinvoiceintakquantities) {
				Rawmaterial rawmaterial = rawmaterialService.getEntityById(
						Rawmaterial.class, rmorderinvoiceintakquantity
								.getRawmaterial().getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//TODO Some meaningful message needs to be provided when rmorderinvoiceintakquantities is empty
//		(rmorderinvoiceintakquantities == null || rmorderinvoiceintakquantities.size() == 0) ? 
//				"Invoice is generated but due to some issues there are not"
//		return new Response(1,"Success", rmorderinvoiceintakquantities);
		return rmorderinvoiceintakquantities;
	}
	
	@RequestMapping(value = "listrmGoodQuantity/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Qualitycheckrawmaterial> getRmorderinvoiceintakquantity(
			@PathVariable("id") long id) {
		List<Qualitycheckrawmaterial> qualitycheckrawmaterials = null;
		try {
			qualitycheckrawmaterials = qualitycheckrawmaterialService.getQualitycheckrawmaterialByInvoiceId(id);
			System.out.println("list size "
					+ qualitycheckrawmaterials.size());
			for (Qualitycheckrawmaterial qualitycheckrawmaterial : qualitycheckrawmaterials) {
				Rawmaterial rawmaterial = rawmaterialService.getEntityById(
						Rawmaterial.class, qualitycheckrawmaterial
								.getRawmaterial().getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qualitycheckrawmaterials;
	}
	private void mailSending(Notification notification,Vendor vendor,List<QualityCheckRMDTO> qualityCheckRMDTOs,Rawmaterialorder rawmaterialorder) throws Exception{
		  Mail mail = new Mail();
		  List<Notificationuserassociation> notificationuserassociations = notificationUserAssociationService.getNotificationuserassociationBynotificationId(notification.getId());
		  for (Notificationuserassociation notificationuserassociation : notificationuserassociations) {
			  User user = userService.getEntityById(User.class, notificationuserassociation.getUser().getId());
			  if(notificationuserassociation.getTo()==true){
				  mail.setMailTo(vendor.getEmail());
			  }else if(notificationuserassociation.getBcc()==true){
				  mail.setMailBcc(user.getEmail());
			  }else if(notificationuserassociation.getCc()==true){
				  mail.setMailCc(user.getEmail());
			  }
		}
	        mail.setMailSubject(notification.getSubject());
	        Map < String, Object > model = new HashMap < String, Object > ();
	        model.put("firstName", vendor.getFirstName());
	        model.put("qualityCheckRMDTOs", qualityCheckRMDTOs);
	        model.put("address", vendor.getAddress());
	        model.put("companyName", vendor.getCompanyName());
	        model.put("lastName", vendor.getLastName());
	        model.put("location", "Pune");
	        model.put("signature", "www.NextechServices.in");
	        mail.setModel(model);

		mailService.sendEmailWithoutPdF(mail,notification);
	}

}
