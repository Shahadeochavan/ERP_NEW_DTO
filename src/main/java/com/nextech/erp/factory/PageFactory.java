package com.nextech.erp.factory;

import com.nextech.erp.model.Page;
import com.nextech.erp.newDTO.PageDTO;

public class PageFactory {
	
	public static Page getPage(PageDTO pageDTO){
		Page page  = new Page();
		page.setId(pageDTO.getId());
		page.setMenu(pageDTO.getMenu());
		page.setDescription(pageDTO.getDescription());
		page.setPageName(pageDTO.getPageName());
		page.setSubmenu(pageDTO.getSubmenu());
		page.setUrl(pageDTO.getUrl());
		page.setIsactive(true);
		return page;
	}

}
