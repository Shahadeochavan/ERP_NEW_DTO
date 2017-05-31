package com.nextech.erp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nextech.erp.constants.ReportColumn;
import com.nextech.erp.status.UserStatus;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;

@RestController
@RequestMapping("/report")
public class ReportController {

	private static final String APPLICATION_XLS = "application/xls";
	private static final String APPLICATION_SCV = "application/csv";
	private static final String APPLICATION_PDF = "application/pdf";
	
	private static final long XLS = 1;
	private static final long CSV = 2;
	private static final long PDF = 3;
	
	@Autowired
	SessionFactory sessionFactory;
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = APPLICATION_SCV , headers = "Accept=application/json")
	public UserStatus login(@PathVariable("id") long id, final HttpServletRequest request,final HttpServletResponse response) throws Exception {
		Connection connection = null;
		try {
			connection = sessionFactory.getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class).getConnection();
			JasperReportBuilder report = DynamicReports.report();
			report.addColumn(ReportColumn.USER_ID);
			report.addColumn(ReportColumn.FIRST_NAME);
			report.addColumn(ReportColumn.LAST_NAME);
			report.title(Components.text(ReportColumn.USER_REPORT).setHorizontalAlignment(HorizontalAlignment.CENTER));
			report.setDataSource(ReportColumn.USER_REPORT_QUERY, connection);
			String fileName = ReportColumn.USER_REPORT_PATH;
			
			downloadReport(report, id, fileName, response);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} 
		return null;
	}
	
	private void downloadReport(JasperReportBuilder report,long reportType, String fileName, HttpServletResponse response){
		try {
			if(reportType == XLS){
				fileName = fileName+".xls";
				report.toXls(new FileOutputStream(fileName));
				response.setContentType(APPLICATION_XLS);
			}
			else if(reportType == CSV){
				fileName = fileName+".csv";
				report.toCsv(new FileOutputStream(fileName));
				response.setContentType(APPLICATION_SCV);
			}
			else if(reportType == PDF){
				fileName = fileName+".pdf";
				report.toPdf(new FileOutputStream(fileName));
				response.setContentType(APPLICATION_PDF);
			}
			File file = new File(fileName);
	        InputStream in = new FileInputStream(file);
	        
	        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
	        response.setHeader("Content-Length", String.valueOf(file.length()));
	        FileCopyUtils.copy(in, response.getOutputStream());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DRException e) {
			e.printStackTrace();
		}
	}
}