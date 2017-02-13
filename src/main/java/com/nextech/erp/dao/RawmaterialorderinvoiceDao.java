package com.nextech.erp.dao;

import java.util.List;

import com.nextech.erp.model.Rawmaterialorderinvoice;

public interface RawmaterialorderinvoiceDao extends
		SuperDao<Rawmaterialorderinvoice> {
	public List<Rawmaterialorderinvoice> getRawmaterialorderinvoiceByStatusId(
			long id) throws Exception;
}