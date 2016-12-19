package com.sweettoto.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XSSFTest {
	private Workbook workbook=null;
	private Sheet sheet=null;
	private Row row=null;
	
	@Before
	public void setUp() throws Exception {
		File file=new File("SXSSFtest.xlsx");
		if(file.exists()){
			file.delete();
			file.createNewFile();
		}
		
		workbook=new SXSSFWorkbook();
		sheet=workbook.createSheet();
		row=sheet.createRow(0);
		row.createCell(0).setCellValue("hello");
		
		FileOutputStream fileOutputStream=new FileOutputStream(file);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		workbook.close();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		File books = new File("SXSSFtest.xlsx");
		int lastRowNumber=0;
		if (books.exists()) {

			FileInputStream fileInputStream=null;
			
			try {
				fileInputStream = new FileInputStream(books);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				workbook = WorkbookFactory.create(fileInputStream);
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("NumberOfSheet: " + workbook.getNumberOfSheets());
			sheet = workbook.getSheetAt(0);
			
			lastRowNumber = sheet.getLastRowNum();
			System.out.println("LastRowNumber in sheet0: " + lastRowNumber);
			
			try {
				fileInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
/*			
			try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
*/			
			// to see if the sheet kept
			System.out.println(sheet.getRow(lastRowNumber).getCell(0).getStringCellValue());
			
			
			//to append data 

//			preparation for writing even though the file has existed.
			//workbook = new SXSSFWorkbook(100);
			sheet.createRow(lastRowNumber+1).createCell(0).setCellValue("cherry");
			

			
			FileOutputStream fileOutputStream=null;
			try {
				fileOutputStream=new FileOutputStream(books);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				workbook.write(fileOutputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
	}

}
