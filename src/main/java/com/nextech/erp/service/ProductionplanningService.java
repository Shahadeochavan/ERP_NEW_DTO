package com.nextech.erp.service;

import java.util.Date;
import java.util.List;

import com.nextech.erp.model.Product;
import com.nextech.erp.model.ProductionPlan;
import com.nextech.erp.model.Productionplanning;

public interface ProductionplanningService extends CRUDService<Productionplanning>{
	
	public Productionplanning getProductionPlanningforCurrentMonthByProductIdAndDate(long pId,Date date)throws Exception;
	
	public List<Productionplanning> getProductionplanningByMonth(Date month) throws Exception;
	
	public List<ProductionPlan> getProductionPlanForCurrentMonth() throws Exception;
	
	public List<Productionplanning> updateProductionPlanByMonthYear(String month_year) throws Exception;
	
	public List<Productionplanning> createProductionPlanMonthYear(List<Product> productList,String month_year) throws Exception;
	
	public void updateProductionplanningForCurrentMonth(List<ProductionPlan> productionplanningList) throws Exception;
	
	public Productionplanning getProductionplanByDateAndProductId(Date date,long pId)throws Exception;
	
	public List<Productionplanning> getProductionplanByDate(Date date)throws Exception;

}
