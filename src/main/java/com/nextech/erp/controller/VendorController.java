package com.nextech.erp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nextech.erp.constants.ERPConstants;
import com.nextech.erp.model.Vendor;
import com.nextech.erp.service.VendorService;
import com.nextech.erp.status.UserStatus;

@Controller
@RequestMapping("/vendor")
public class VendorController {

	@Autowired
	VendorService vendorService;
	
	@Autowired
	private MessageSource messageSource;
	
	 private static final String UPLOAD_DIRECTORY ="/images";  

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
	public @ResponseBody UserStatus addVendor(
			@Valid @RequestBody Vendor vendor, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				return new UserStatus(0, bindingResult.getFieldError()
						.getDefaultMessage());
			}
		
			if (vendorService.getVendorByCompanyName(vendor.getCompanyName()) == null) {

			} else {
				return new UserStatus(1, messageSource.getMessage(ERPConstants.COMPANY_NAME_EXIT, null, null));
			}
			if (vendorService.getVendorByEmail(vendor.getEmail()) == null) {
			} else {
				return new UserStatus(1,messageSource.getMessage(ERPConstants.EMAIL_ALREADY_EXIT, null, null));
			}
			vendor.setCreatedBy(Long.parseLong(request.getAttribute("current_user").toString()));
			vendor.setIsactive(true);
			vendorService.addEntity(vendor);
			return new UserStatus(1, "vendor added Successfully !");
		} catch (ConstraintViolationException cve) {
			System.out.println("Inside ConstraintViolationException");
			cve.printStackTrace();
			return new UserStatus(0, cve.getCause().getMessage());
		} catch (PersistenceException pe) {
			System.out.println("Inside PersistenceException");
			pe.printStackTrace();
			return new UserStatus(0, pe.getCause().getMessage());
		} catch (Exception e) {
			System.out.println("Inside Exception");
			e.printStackTrace();
			return new UserStatus(0, e.getCause().getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody Vendor getVendor(@PathVariable("id") long id) {
		Vendor vendor = null;
		try {
			vendor = vendorService.getEntityById(Vendor.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vendor;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	public @ResponseBody UserStatus updateVendor(@RequestBody Vendor vendor) {
		try {
			vendorService.updateEntity(vendor);
			return new UserStatus(1, "Vendor update Successfully !");
		} catch (Exception e) {
			e.printStackTrace();
			return new UserStatus(0, e.toString());
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<Vendor> getVendor() {

		List<Vendor> userList = null;
		try {
			userList = vendorService.getEntityList(Vendor.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userList;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public @ResponseBody UserStatus deleteVendor(@PathVariable("id") long id) {

		try {
			Vendor vendor = vendorService.getEntityById(Vendor.class, id);
			vendor.setIsactive(false);
			vendorService.updateEntity(vendor);
			return new UserStatus(1, "Vendor deleted Successfully !");
		} catch (Exception e) {
			return new UserStatus(0, e.toString());
		}

	}

     
	    @RequestMapping("uploadform")  
	    public ModelAndView uploadForm(){  
	        return new ModelAndView("uploadform");    
	    }  
	      
	    @RequestMapping(value="savefile",method=RequestMethod.POST)  
	    public ModelAndView saveimage( @RequestParam CommonsMultipartFile file,  
	           HttpSession session) throws Exception{  
	  
	    ServletContext context = session.getServletContext();  
	    String path = context.getRealPath(UPLOAD_DIRECTORY);  
	    String filename = file.getOriginalFilename();  
	  
	    System.out.println(path+" "+filename);        
	  
	    byte[] bytes = file.getBytes();  
	    BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(  
	         new File(path + File.separator + filename)));  
	    stream.write(bytes);  
	    stream.flush();  
	    stream.close();  
	           
	    return new ModelAndView("uploadform","filesuccess","File successfully saved!");  
	    }  
}
