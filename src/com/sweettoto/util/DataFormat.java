package com.sweettoto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class DataFormat {
	// outputXLSXPath1 for the item which has all the attribute
	// (weight=63)
	private String outputXLSXPath_111111 = null;
	// weight > 59
	private String outputXLSXPath_1111xx = null;
	// weight > 47
	private String outputXLSXPath_11xxxx = null;
	// weight > 31
	private String outputXLSXPath_1xxxxx = null;
	// weight > 19
	private String outputXLSXPath_x1xxxx = null;

	public DataFormat() {
	}

	public DataFormat(String outputPath1, String outputPath2, String outputPath3, String outputPath4,
			String outputPath5) {
		this.outputXLSXPath_111111 = outputPath1;
		this.outputXLSXPath_1111xx = outputPath2;
		this.outputXLSXPath_11xxxx = outputPath3;
		this.outputXLSXPath_1xxxxx = outputPath4;
		this.outputXLSXPath_x1xxxx = outputPath5;
	}

	public void dataFormat(String inputFile) {
		if (!inputFile.matches(".*\\.xlsx$")) {
			System.out.println("We only deal with the excel file in type of '.xlsx'.");
		} else {
			File inputXLSX = new File(inputFile);
			if (!inputXLSX.exists()) {
				System.out.println(
						"The file doesn't exist or we can't read it.\n\tPlease select an valid file in type of '.xlsx'.");
			} else if (!inputXLSX.canRead()) {
				System.out.println("We cannot read it. Please make sure you have the authority to read.");
			} else {
				// first of all, to specify the output files' Paths
				if (outputXLSXPath_111111 == null || outputXLSXPath_111111.isEmpty()) {
					outputXLSXPath_111111 = inputXLSX.getAbsolutePath().replaceAll(".xlsx", "").concat("_111111.xlsx");
				}
				if (outputXLSXPath_1111xx == null || outputXLSXPath_1111xx.isEmpty()) {
					outputXLSXPath_1111xx = inputXLSX.getAbsolutePath().replaceAll(".xlsx", "").concat("_1111xx.xlsx");
				}
				if (outputXLSXPath_11xxxx == null || outputXLSXPath_11xxxx.isEmpty()) {
					outputXLSXPath_11xxxx = inputXLSX.getAbsolutePath().replaceAll(".xlsx", "").concat("_11xxxx.xlsx");
				}
				if (outputXLSXPath_1xxxxx == null || outputXLSXPath_11xxxx.isEmpty()) {
					outputXLSXPath_1xxxxx = inputXLSX.getAbsolutePath().replaceAll(".xlsx", "").concat("_1xxxxx.xlsx");
				}
				if (outputXLSXPath_x1xxxx == null || outputXLSXPath_11xxxx.isEmpty()) {
					outputXLSXPath_x1xxxx = inputXLSX.getAbsolutePath().replaceAll(".xlsx", "").concat("_x1xxxx.xlsx");
				}

				// to read original data and write to target files after
				// formating
				FileInputStream fileInputStream = null;
				// to create a fileInputStream
				try {
					fileInputStream = new FileInputStream(inputXLSX);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				// to create a workbook instance
				Workbook workbook = null;
				try {
					workbook = WorkbookFactory.create(fileInputStream);
				} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
					e.printStackTrace();
				}

				// to be ready to invoke the function writeToFile(String,
				// ArrayList)
				String targetOutputPath = null;

				// in case of that there are several sheets
				int numberOfSheets = workbook.getNumberOfSheets();
				for (int currentSheetIndex = 0; currentSheetIndex < numberOfSheets; currentSheetIndex++) {
					Sheet sheet = workbook.getSheetAt(currentSheetIndex);
					int lastNumberOfRow = sheet.getLastRowNum();
					// read all rows
					for (int currentRowIndex = 1; currentRowIndex < lastNumberOfRow + 1; currentRowIndex++) {
						Row row = sheet.getRow(currentRowIndex);
						ArrayList<String> book = new ArrayList<String>();
						for (int currentColumnIndex = 0; currentColumnIndex < row
								.getLastCellNum(); currentColumnIndex++) {
							Cell cell = row.getCell(currentColumnIndex);
							book.add(cell.getStringCellValue());
						}

						// to format data
						// book = format(book);

						// for isbn
						String isbn = book.get(0);
						book.set(0, isbn);

						// for title
						String title = book.get(1);
						title = title.replaceAll("[特价书]", "");
						book.set(1, title);

						// for author
						String author = book.get(2);
						author = author.replaceAll("本社", "机械工业出版社");
						// replace "【" and "】" with "[" and "]" respectively
						author = author.replaceAll("【", "[");
						author = author.replaceAll("】", "]");
						// replace the parenthesis which contain the nationality
						// with "[ ]"
						String[] authors = author.split(";", 2);
						String[] authors0 = authors[0].split("\\)", 2);
						authors0[0] = authors0[0].replaceAll("\\(", "[");

						if (authors0.length > 1) {
							authors[0] = authors0[0] + "]" + authors0[1];
						} else {
							authors[0] = authors0[0];
						}

						if (authors.length > 1) {
							author = authors[0] + ";" + authors[1];
						} else {
							author = authors[0];
						}

						book.set(2, author);

						// for publisher
						String publisher = book.get(3);
						publisher = publisher.replaceAll("本社", "机械工业出版社");
						book.set(3, publisher);

						// for publicationDate
						String publicationDate = book.get(4);
						book.set(4, publicationDate);

						// for priceOnTag
						String priceOnTag = book.get(5);
						book.set(5, priceOnTag);

						// for classification
						String classification = book.get(6);
						book.set(6, classification);

						// for coverPath
						String coverPath = "./Covers/" + book.get(7);
						File cover = new File(coverPath);
						if (cover.exists() && cover.length() == 0) {
							cover.delete();
							coverPath = "";
							book.set(7, coverPath);
						}

						// the isbn, title, author and publisher of a book
						// respectively distribute to 2^(3-0), 2^(3-1), 2^(3-2)
						// and 2^(3-3)
						// evaluate how much useful the piece of data is by the
						// weight.
						int weight = 0;
						for (int i = 0; i < 6; i++) {
							if (i == 0) {
								if (book.get(0).matches("(\\w){10}") || book.get(0).matches("(\\w){13}")) {
									weight += 32;
								}
							} else if (i == 3) {
								if (book.get(3).contains("出版社")) {
									weight += 4;
								}
							} else if (!book.get(i).isEmpty()) {
								weight += Math.pow(2, 5 - i);
							}
						}

						// System.out.println(currentRowIndex + "\t" + weight);
						if (weight >= 63) {
							targetOutputPath = outputXLSXPath_111111;
							outputToXLSX(targetOutputPath, book);

							// output to some files varying from classification
							// !!especially we must take care for the
							// classification containing "/" which not allowed
							// in file path in linux
							String classification_suffix = book.get(6);
							classification_suffix = classification_suffix.replaceAll(File.separator, "|");
							targetOutputPath = inputXLSX.getAbsolutePath().replaceAll(".xlsx", "")
									.concat("_111111_" + classification_suffix + ".xlsx");
							outputToXLSX(targetOutputPath, book);
						}
						// for valid data
						else if (weight > 59) {
							// output to the file for invalid data
							targetOutputPath = outputXLSXPath_1111xx;
							// output to a file including classification.
							outputToXLSX(targetOutputPath, book);
						} else if (weight > 47) {
							// output to the file for invalid data
							targetOutputPath = outputXLSXPath_11xxxx;
							// output to a file including classification.
							outputToXLSX(targetOutputPath, book);
						} else if (weight > 31) {
							targetOutputPath = outputXLSXPath_1xxxxx;
							outputToXLSX(targetOutputPath, book);
						} else if (weight > 19) {
							targetOutputPath = outputXLSXPath_x1xxxx;
							outputToXLSX(targetOutputPath, book);
						} else {
							continue;
						}
					}
				}

				try {
					fileInputStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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

	private ArrayList<String> format(ArrayList<String> book) {
		// for isbn
		String isbn = book.get(0);
		book.set(0, isbn);

		// for title
		String title = book.get(1);
		book.set(1, title);

		// for author
		String author = book.get(2);
		// replace "【" and "】" with "[" and "]" respectively
		author = author.replaceAll("【", "[");
		author = author.replaceAll("】", "]");
		// replace the parenthesis which contain the nationality with "[ ]"
		String[] authors = author.split(";", 2);
		String[] authors0 = authors[0].split("\\)", 2);
		authors0[0] = authors0[0].replaceAll("\\(", "[");
		// authors0[0]=authors0[0].substring(authors0[0].indexOf('['));
		String[] authors0_0 = authors0[0].split("[", 2);
		if (authors0_0.length > 1) {
			authors0[0] = "["+authors0_0[1];
		}else {
			authors0[0]=authors0_0[0];
		}
		if (authors0.length > 1) {
			authors[0] = authors0[0] + "]" + authors0[1];
		} else {
			authors[0] = authors0[0];
		}

		if (authors.length > 1) {
			author = authors[0] + ";" + authors[1];
		} else {
			author = authors[0];
		}

		book.set(2, author);

		// for publisher
		String publisher = book.get(3);
		book.set(3, publisher);

		// for publicationDate
		String publicationDate = book.get(4);
		book.set(4, publicationDate);

		// for priceOnTag
		String priceOnTag = book.get(5);
		book.set(5, priceOnTag);

		// for classification
		String classification = book.get(6);
		book.set(6, classification);

		// for coverPath
		String coverPath = "./Covers/" + book.get(7);
		File cover = new File(coverPath);
		if (cover.exists() && cover.length() == 0) {
			cover.delete();
			coverPath = "";
			book.set(7, coverPath);
		}

		return book;
	}

	private void outputToXLSX(String filePath, ArrayList<String> book) {
		// to create two fileOutputStream instances for output
		Workbook workbook = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		File outputXLSXFile = new File(filePath);
		if (outputXLSXFile.exists()) {
			try {
				fileInputStream = new FileInputStream(outputXLSXFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				workbook = WorkbookFactory.create(fileInputStream);
			} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
				e.printStackTrace();
			}
			sheet = workbook.getSheetAt(0);
		} else {
			try {
				outputXLSXFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			workbook = new SXSSFWorkbook();
			sheet = workbook.createSheet();
			// for the first append data to the excel file, we should add a
			// header.

			row = sheet.createRow(0);
			cell = row.createCell(0);
			cell.setCellValue("ISBN");
			cell = row.createCell(1);
			cell.setCellValue("TITLE");
			cell = row.createCell(2);
			cell.setCellValue("AUTHOR");
			cell = row.createCell(3);
			cell.setCellValue("PUBLISHER");
			cell = row.createCell(4);
			cell.setCellValue("PUBLICATIONDATE");
			cell = row.createCell(5);
			cell.setCellValue("PRICEONTAG");
			cell = row.createCell(6);
			cell.setCellValue("CLASSIFICATION");
			cell = row.createCell(7);
			cell.setCellValue("COVERPATH");
			cell = row.createCell(8);
			cell.setCellValue("LINK");
			cell = row.createCell(9);
			cell.setCellValue("COVERURL");
			cell = row.createCell(10);
			cell.setCellValue("ADDEDDATE");
		}

		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum >= 0) {
			row = sheet.createRow(lastRowNum + 1);
			for (int i = 0; i < book.size(); i++) {
				cell = row.createCell(i);
				cell.setCellValue(book.get(i));
			}
		}

		if (fileInputStream != null) {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			fileOutputStream = new FileOutputStream(outputXLSXFile);
		} catch (FileNotFoundException e) {
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

	public String getOutputXLSXPath_111111() {
		return outputXLSXPath_111111;
	}

	public void setOutputXLSXPath_111111(String outputXLSXPath_111111) {
		this.outputXLSXPath_111111 = outputXLSXPath_111111;
	}

	public String getOutputXLSXPath_1111xx() {
		return outputXLSXPath_1111xx;
	}

	public void setOutputXLSXPath_1111xx(String outputXLSXPath_1111xx) {
		this.outputXLSXPath_1111xx = outputXLSXPath_1111xx;
	}

	public String getOutputXLSXPath_11xxxx() {
		return outputXLSXPath_11xxxx;
	}

	public void setOutputXLSXPath_11xxxx(String outputXLSXPath_11xxxx) {
		this.outputXLSXPath_11xxxx = outputXLSXPath_11xxxx;
	}

	public String getOutputXLSXPath_1xxxxx() {
		return outputXLSXPath_1xxxxx;
	}

	public void setOutputXLSXPath_1xxxxx(String outputXLSXPath_1xxxxx) {
		this.outputXLSXPath_1xxxxx = outputXLSXPath_1xxxxx;
	}

	public String getOutputXLSXPath_x1xxxx() {
		return outputXLSXPath_x1xxxx;
	}

	public void setOutputXLSXPath_x1xxxx(String outputXLSXPath_x1xxxx) {
		this.outputXLSXPath_x1xxxx = outputXLSXPath_x1xxxx;
	}

}
