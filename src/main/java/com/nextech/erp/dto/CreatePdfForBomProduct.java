package com.nextech.erp.dto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nextech.erp.model.Bom;
import com.nextech.erp.model.Product;
import com.nextech.erp.model.Productorder;
import com.nextech.erp.service.BomService;

public class CreatePdfForBomProduct {

	private static Font TIME_ROMAN = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
	private static Font TIME_ROMAN_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

	/**
	 * @param args
	 */
	
	@Autowired
	BomService bomService;

	public Document createPDF(String file, List<Bom> boList)
			throws Exception {

		Document document = null;

		try {
			document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			addMetaData(document);

			addTitlePage(document);

			createTable(document, boList);

			document.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;

	}

	private static void addMetaData(Document document) {
		document.addTitle("Generate PDF report");
		document.addSubject("Generate PDF report");
		document.addAuthor("Java Honk");
		document.addCreator("Java Honk");
	}

	private static void addTitlePage(Document document)
			throws DocumentException {

		Paragraph preface = new Paragraph();
		creteEmptyLine(preface, 1);
		preface.add(new Paragraph("RETAIL INVOICE", TIME_ROMAN));

		creteEmptyLine(preface, 1);
		 document.setMargins(36, 72, 108, 180);
		preface.add(new Paragraph("Nextech Services Pvt.LTd"));
		preface.add(new Paragraph("Office:18,3rd Floor Vasantika Aparment"));
		preface.add(new Paragraph("S.No,47/6B,Opp,Yena Bunglow ,"));
		preface.add(new Paragraph("Beside Manglam Chembers,Paud Road ,"));
		preface.add(new Paragraph("Kothrud,Pune:411038 "));
	

		preface.setAlignment(Element.ALIGN_JUSTIFIED);
		preface.setIndentationRight(20);
		preface.add(new Paragraph("Kothrud,Pune:411038 "));
		preface.setSpacingAfter(10);
		preface.add(new Paragraph("Kothrud,Pune:411038fgfg "));

		creteEmptyLine(preface, 1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		preface.add(new Paragraph("Product Order Invoice Date :"
				+ simpleDateFormat.format(new Date()), TIME_ROMAN_SMALL));
		document.add(preface);

	}

	private static void creteEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private void createTable(Document document, List<Bom> boList)
			throws Exception {
		
		Paragraph paragraph = new Paragraph();
		creteEmptyLine(paragraph, 2);
		document.add(paragraph);
		PdfPTable table = new PdfPTable(2);

		PdfPCell c1 = new PdfPCell(new Phrase("BOM ID"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.WHITE);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Quantity"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.WHITE);
		table.addCell(c1);
		table.setHeaderRows(1);

     for (Bom bom : boList) {
 	  table.setWidthPercentage(100);
	 table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	 table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	 table.addCell(bom.getBomId());
	 table.addCell(Long.toString(bom.getQuantity()));
    }
     document.add(table);	
	}

}
