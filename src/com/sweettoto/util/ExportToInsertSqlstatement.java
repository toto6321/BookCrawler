/**
 	this is designed to translate the data into a sql file of insert statement
 * 
 */
package com.sweettoto.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author sweettoto
 *
 */
public class ExportToInsertSqlstatement {
	private String path = "";

	public ExportToInsertSqlstatement() {

	}

	public ExportToInsertSqlstatement(String path) {
		this.path = path;
	}

	public boolean tranlate(String inputXLSX) {
		boolean returnValue = false;
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		ArrayList<String> book = null;
	
		Workbook workbook = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		if (inputXLSX.matches(".*\\.xlsx$")) {
			File input = new File(inputXLSX);
			if (input.exists() && input.isFile()) {
				// to decide what the path of the output file
				if (path.isEmpty() || !path.matches(".*\\.sql")) {
					path = input.getAbsolutePath().substring(0, inputXLSX.length() - 5) + "_insert.sql";
				}

				// to start to translate
				// if the input file is empty, we could not create the workbook
				// like that
				if (input.getTotalSpace() > 0) {
					// to create a instance of BufferedInputStream for the input
					// file
					try {
						fileInputStream = new FileInputStream(input);
						bufferedInputStream = new BufferedInputStream(fileInputStream);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					// to create a instance of workbook
					try {
						workbook = WorkbookFactory.create(bufferedInputStream);
					} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
						e.printStackTrace();
					}
					// to retrieve all data from the excel file
					int numberOfSheets = workbook.getNumberOfSheets();
					for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex++) {
						sheet=workbook.getSheetAt(sheetIndex);
						int lastRowNumber = sheet.getLastRowNum();
						for (int rowIndex = 1; rowIndex < lastRowNumber; rowIndex++) {
							row = sheet.getRow(rowIndex);
							int lastCellNumber = row.getLastCellNum();
							book = new ArrayList<String>();
							for (int columnIndex = 0; columnIndex < 9 && columnIndex < lastCellNumber; columnIndex++) {
								cell = row.getCell(columnIndex);
								book.add(cell.getStringCellValue());
							}
							outputToSqlFile(book);
						}
					}
					returnValue = true;
				} else {
					System.out.println(inputXLSX + " is a empty file.");
				}
			} else {
				System.out.println(inputXLSX + " doesn't exist or it is not a file.");
			}
		} else {
			System.out.println(inputXLSX + " is not a valid .xlsx file. ");
		}

		return returnValue;
	}

	private void outputToSqlFile(ArrayList<String> book) {
		File outputFile = new File(path);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(outputFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		// to construct the sql statement.
		try {
			bufferedWriter.write(
					"INSERT INTO BOOK(ISBN,TITLE,AUTHOR,PUBLISHER,PUBLICATIONDATE,PRICEONTAG,CLASSIFICATION,COVERPATH,LINK) VALUES\n\t");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < book.size(); i++) {
			String temp = book.get(i);
			// to include multiple lists of column values, each enclosed within
			// parentheses and separated by commas.
			if (i == 0) {
				temp = "('" + temp + "'";
			} else if (i < book.size() - 1) {
				temp = ",'" + temp + "'";
			} else {
				temp = ",'" + temp + "');\n";
			}
			try {
				bufferedWriter.write(temp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	
	
}
