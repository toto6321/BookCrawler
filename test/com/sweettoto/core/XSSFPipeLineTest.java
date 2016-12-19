package com.sweettoto.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

public class XSSFPipeLineTest {

	private Workbook workbook = null;
	private Sheet sheet = null;

	public XSSFPipeLineTest() {

	}

	@Test
	public void testSXSSF() throws IOException {
		Row activeRow = null;
		int lastRowNumber = -1;

		// to store data into a xlsx
		File books = new File("mybooks_test.xlsx");
		if (books.exists()) {
			FileInputStream fileInputStream = new FileInputStream(books);
			try {
				workbook = WorkbookFactory.create(fileInputStream);
			} catch (EncryptedDocumentException | InvalidFormatException e) {
				e.printStackTrace();
			}
			System.out.println("NumberOfSheet: " + workbook.getNumberOfSheets());
			sheet = workbook.getSheetAt(0);
			lastRowNumber = sheet.getLastRowNum();
			System.out.println("LastRowNumber in sheet0: " + lastRowNumber);
			
			fileInputStream.close();

		} else {
			System.out.println("mybooks_test.xlsx doesn't exist before. We will create it later.");
			try {
				books.createNewFile();
			} catch (IOException e) {
				System.out.println("failed to create mybooks1.xlsl");
				e.printStackTrace();
			}

			// we should add a row for head, if file doesn't exist before
			// writing data into it.
			workbook = new SXSSFWorkbook(100);
			sheet = workbook.createSheet();
			Row header = sheet.createRow(0);
			Cell[] heads = new Cell[7];
			for (int i = 0; i < heads.length; i++) {
				heads[i] = header.createCell(i);
			}
			heads[0].setCellValue("ISBN");
			heads[1].setCellValue("TITLE");
			heads[2].setCellValue("AUTHOR");
			heads[3].setCellValue("PUBLISHER");
			heads[4].setCellValue("PUBLICATIONDATE");
			heads[5].setCellValue("PRICEONTAG");
			heads[6].setCellValue("LINK");

			lastRowNumber++;
		}

		// here are the data prepare for test
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("ISBN", "9787111214045");
		tempMap.put("TITLE", "应用逻辑(原书第2版)  ;原书名：Logic for Applications ");
		tempMap.put("AUTHOR", "(美)Anil Nerode;丁德成 徐亚涛 吴永成 金陈园");
		tempMap.put("PUBLISHER", "机械工业出版社");
		tempMap.put("PUBLICATIONDATE", "出版日期：2007 年7月");
		tempMap.put("PRICEONTAG", "￥38.00");
		tempMap.put("LINK", "http://product.china-pub.com/35575");

		String isbn = null;
		String title = null;
		String author = null;
		String publisher = null;
		String publicationDate = null;
		String priceOnTag = null;
		String link = null;

		isbn = tempMap.get("ISBN").toString().trim();
		title = tempMap.get("TITLE").toString().trim();
		author = tempMap.get("AUTHOR").toString().trim();
		publisher = tempMap.get("PUBLISHER").toString().trim();
		publicationDate = tempMap.get("PUBLICATIONDATE").toString().trim();
		priceOnTag = tempMap.get("PRICEONTAG").toString().trim();
		link = tempMap.get("LINK").toString().trim();

		// to write data
		// activeRow is the o
		activeRow = sheet.createRow(lastRowNumber + 1);

		Cell activedCell = null;
		activedCell = activeRow.createCell(0);
		activedCell.setCellValue(isbn);
		activedCell = activeRow.createCell(1);
		activedCell.setCellValue(title);
		activedCell = activeRow.createCell(2);
		activedCell.setCellValue(author);
		activedCell = activeRow.createCell(3);
		activedCell.setCellValue(publisher);
		activedCell = activeRow.createCell(4);
		activedCell.setCellValue(publicationDate);
		activedCell = activeRow.createCell(5);
		activedCell.setCellValue(priceOnTag);
		activedCell = activeRow.createCell(6);
		activedCell.setCellValue(link);

		// to create an instance of FileOutpuStream to write data into excel

		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(books);
		} catch (FileNotFoundException e1) {
			System.out.println("fail to create a instance of FileOutputStream.");
			e1.printStackTrace();
		}

		try {
			workbook.write(fileOutputStream);
		} catch (IOException e) {
			System.out.println("Fail to write into file in xlsx type.");
			e.printStackTrace();
		}

		try {
			fileOutputStream.close();
		} catch (IOException e1) {
			System.out.println("Failed to close fileOutputStream.");
			e1.printStackTrace();
		}
		try {
			workbook.close();
		} catch (IOException e) {
			System.out.println("failed to close the workbook.");
			e.printStackTrace();
		}

	}
}
