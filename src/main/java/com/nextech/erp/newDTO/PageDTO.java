package com.nextech.erp.newDTO;




public class PageDTO {

	private long id;
	private String description;
	private String menu;
	private String pageName;
	private String submenu;
	private String url;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getSubmenu() {
		return submenu;
	}
	public void setSubmenu(String submenu) {
		this.submenu = submenu;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
