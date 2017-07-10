package com.nextech.erp.newDTO;



import com.nextech.erp.model.Page;
import com.nextech.erp.model.Usertype;

public class UserTypePageAssoDTO {

	private long id;
	private Page page;
	private Usertype usertype;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Usertype getUsertype() {
		return usertype;
	}
	public void setUsertype(Usertype usertype) {
		this.usertype = usertype;
	}
	
	
}
