package com.sweettoto.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class XLSXPipeline implements Pipeline {
	private String path = null;
	private File books = null;
	private Workbook workbook = null;
	private Sheet sheet = null;

	public XLSXPipeline() {
		this.path = "MyBooks1_excel.xlsx";
	}

	public XLSXPipeline(String path) {
		this.path = path;
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		// Not util the "ISBN" is not null should the program to store data run
		// ahead.
		if (resultItems.get("ISBN") != null & resultItems.get("ISBN") != "") {

			int lastRowNumber = -1;

			// to store data into a xlsx
			if (!path.matches("^.+\\.xlsx$")) {
				path = "Mybooks1_excel.xlsx";
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
				System.out.println(path + " doesn't exist before. We will create it later.");
				try {
					books.createNewFile();
				} catch (IOException e) {
					System.out.println("failed to create " + path);
					e.printStackTrace();
				}

				workbook = new SXSSFWorkbook();
				sheet = workbook.createSheet();
				Row header = sheet.createRow(0);
				Cell[] heads = new Cell[12];
				for (int i = 0; i < heads.length; i++) {
					heads[i] = header.createCell(i);
				}
				
				heads[0].setCellValue("ISBN");
				heads[1].setCellValue("TITLE");
				heads[2].setCellValue("AUTHOR");
				heads[3].setCellValue("PUBLISHER");
				heads[4].setCellValue("PUBLICATIONDATE");
				heads[5].setCellValue("PRICEONTAG");
				heads[6].setCellValue("CLASSIFICATION");
				heads[7].setCellValue("COVERPATH");
				heads[8].setCellValue("LINK");
				heads[9].setCellValue("COVERURL"); 
				heads[10].setCellValue("ADDEDDATE");
				heads[11].setCellValue("ISBN-13");

				lastRowNumber++;
			}

			Row activeRow = sheet.createRow(lastRowNumber + 1);

			String isbn = resultItems.get("ISBN").toString().trim();
			String title =resultItems.get("TITLE").toString().trim();
			String author = resultItems.get("AUTHOR").toString().trim();
			String publisher = resultItems.get("PUBLISHER").toString().trim();
			String publicationDate = resultItems.get("PUBLICATIONDATE").toString().trim();
			String priceOnTag = resultItems.get("PRICEONTAG").toString().trim();
			String classification=resultItems.get("CLASSIFICATION");
			String link = resultItems.get("LINK").toString();
			String coverURL=resultItems.get("COVERURL").toString();
			
			String coverPath=String.format(isbn+coverURL.substring(coverURL.lastIndexOf('.')));
			
			//extra data
			String timestamp=resultItems.get("DATEADDED");
			String ISBN_13=null;
			if(isbn.length()==13){
				ISBN_13=isbn;
			}else {
				ISBN_13="978"+isbn;
			}
			
			
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
			activedCell.setCellValue(classification);

			//coverPath should be null if the cover doesn't exist.		
			activedCell = activeRow.createCell(7);
			activedCell.setCellValue(coverPath);
			
			activedCell = activeRow.createCell(8);
			activedCell.setCellValue(link);
			activedCell = activeRow.createCell(9);
			activedCell.setCellValue(coverURL);
			activedCell = activeRow.createCell(10);
			activedCell.setCellValue(timestamp);
			activedCell = activeRow.createCell(11);
			activedCell.setCellValue(ISBN_13);

			
			
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

}
