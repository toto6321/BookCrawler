package com.sweettoto.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

public class XLSXPipelineTest {
	private String path = null;
	private File books = null;
	private Workbook workbook = null;
	private Sheet sheet = null;

	@Test
	public void testXLSXPipeline() {
		
		//input
		path="mybooks1.xlsx";
		Map<String, Object> resultItems=new LinkedHashMap<>();
		resultItems.put("ISBN", "9787111214045");
		resultItems.put("TITLE", "应用逻辑(原书第2版)  ;原书名：Logic for Applications ");
		resultItems.put("AUTHOR", "(美)Anil Nerode;丁德成 徐亚涛 吴永成 金陈园");
		resultItems.put("PUBLISHER", "机械工业出版社");
		resultItems.put("PUBLICATIONDATE", "出版日期：2007 年7月");
		resultItems.put("PRICEONTAG", "￥38.00");
		resultItems.put("LINK", "http://product.china-pub.com/35575");
		
		
		
		int lastRowNumber = -1;
		// to store data into a xlsx
		if (!path.matches("^(\\w)*\\.xlsx$")) {
			path = "mybooks1.xlsx";
			System.out.println("File Name is invalid with 'mybooks1.xlsx' replaced for default.");
		}
		books = new File(path);
		if (books.exists()) {
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(books);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				workbook = WorkbookFactory.create(fileInputStream);
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sheet = workbook.getSheetAt(0);
			lastRowNumber = sheet.getLastRowNum();

			try {
				fileInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("mybooks1.xlsx doesn't exist before. We will create it later.");
			try {
				books.createNewFile();
			} catch (IOException e) {
				System.out.println("failed to create mybooks1.xlsl");
				e.printStackTrace();
			}

			workbook = new SXSSFWorkbook();
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

		Row activeRow = sheet.createRow(lastRowNumber + 1);

		String isbn = null;
		String title = null;
		String author = null;
		String publisher = null;
		String publicationDate = null;
		String priceOnTag = null;
		String link = null;

		isbn = resultItems.get("ISBN").toString().trim();
		title = resultItems.get("TITLE").toString().trim();
		author = resultItems.get("AUTHOR").toString().trim();
		publisher = resultItems.get("PUBLISHER").toString().trim();
		publicationDate = resultItems.get("PUBLICATIONDATE").toString().trim();
		priceOnTag = resultItems.get("PRICEONTAG").toString().trim();
		link = resultItems.get("LINK").toString().trim();

		// to write data
		Cell activedCell = activeRow.createCell(0);
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
