package com.sweettoto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;
import org.mozilla.universalchardet.UniversalDetector;

public class DataFormatTest {
	@Test
	public void DataFormatClassTest() {
		DataFormat dFormat = new DataFormat();
		String inputFilePath = null;
		inputFilePath = "/Volumes/Toto/totosweet/Documents/OnTheWay/Sweettoto/Databases/database data/2016-11-11_13-21-30  +0800/2016-11-11_13-21-30  +0800_EXCEL.xlsx";
		// inputFilePath = "2016-11-19_12-17-29_+0800_EXCEL_111111_建筑.xlsx";
		dFormat.dataFormat(inputFilePath);

		File file = new File(inputFilePath);
		if (file.exists()) {
			System.out.println(file.isFile());
			System.out.println(file.getAbsolutePath());
			System.out.println(file.length());
		} else {
			System.out.println("file doesn't exist.");
		}
	}

	@Test
	public void testFormatAuthor(){
		String author =null;
//		author="\"(英) 达尔文 著";
		author="\" 佚名 著";
		System.out.println(author);
		// replace "【" and "】" with "[" and "]" respectively
		author = author.replaceAll("【", "[");
		author = author.replaceAll("】", "]");
		// replace the parenthesis which contain the nationality with "[ ]"
		String[] authors = author.split(";", 2);
		String[] authors0 = authors[0].split("\\)", 2);
		authors0[0] = authors0[0].replaceAll("\\(", "[");
//		authors0[0]=authors0[0].substring(authors0[0].indexOf('['));
		String[] authors0_0 = authors0[0].split("\\[", 2);
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
			author = authors[0] + ";"+authors[1];
		} else {
			author = authors[0];
		}
		System.out.println(author);
	}
	
	
	
	
	
	public void printBytes(byte[] array, String name) {
		for (int k = 0; k < array.length; k++) {
			System.out.println(name + "[" + k + "] = " + "0x");
			// UnicodeFormatter.byteToHex(array[k]));
		}
	}

	@Test
	public void testGetEncoding() throws UnsupportedEncodingException {
		// String original = new String("A" + "\u00ea" + "\u00f1" + "\u00fc" +
		// "C");
		String original = "王元杰 杨波 周亚宁 纪�F�F";
		char[] chars = original.toCharArray();

		System.out.println("chars are :");
		for (int i = 0; i < chars.length; i++) {
			System.out.print(chars[i] + " ");
		}
		System.out.println("\n");

		// to detect which charset by juniversalcharset
		UniversalDetector universalDetector = new UniversalDetector(null);
		byte[][] charGetByte = null;
		for (int i = 0; i < chars.length; i++) {
			char[] temp = { chars[i] };
			byte[] bs = new String(temp).getBytes();
			universalDetector.handleData(bs, 0, bs.length);
			System.out.println(new String(bs) + " " + universalDetector.getDetectedCharset());
		}
		universalDetector.dataEnd();

		// get the array of byte of the string
		byte[] bytes = null;
		bytes = original.getBytes();
		System.out.println("getBytes(): ");
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(Integer.toHexString(bytes[i] & 0xff) + " ");
		}
		System.out.println("\n");

		System.out.println("string's length: " + original.length());
		System.out.println("bytes' length: " + bytes.length);

		String roundTrip = null;
		try {
			String restoredString = original.substring(10, original.length() - 1);
			byte[] restoredBytes = restoredString.getBytes();
			roundTrip = new String(restoredBytes, "EUC-TW");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\nroundTrip = " + roundTrip);
		// System.out.println();
		// printBytes(utf8Bytes, "utf8Bytes");
		// System.out.println();
		// printBytes(defaultBytes, "defaultBytes");

	}

	@Test
	public void testEncodeCharset() {
		String string1,string2,string3;
		string1=string2=string3=null;
		string1=new String("你好");
		byte[] bytes1,bytes2,bytes3;
		bytes1=bytes2=bytes3=null;
		try {
			bytes1=string1.getBytes("gbk");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			string2=new String(bytes1, "gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			bytes2=string2.getBytes("utf8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			string3=new String(bytes2, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		bytes3=string3.getBytes();
		
		
		// show
		System.out.println("original string: "+string1);
		System.out.println("original bytes:  ");
		for (int i = 0; i < bytes1.length; i++) {
			System.out.print(Integer.toHexString(bytes1[i]&0x00ff)+" ");
		}System.out.println("");
		
		System.out.println("changing charset: "+string2);
		System.out.println("variant1 bytes:   ");
		for (int i = 0; i < bytes2.length; i++) {
			System.out.print(Integer.toHexString(bytes2[i]&0x00ff)+" ");
		}System.out.println("");
		
		System.out.println("double changing charset: "+string2);
		System.out.println("variant2 bytes:   ");
		for (int i = 0; i < bytes3.length; i++) {
			System.out.print(Integer.toHexString(bytes3[i]&0x00ff)+" ");
		}System.out.println("");
		
		
		
	}

	@Test
	public void testDataformat() {
		// for publisher
		String publisher = "1本社1";
		publisher = publisher.replaceAll("本社", "机械工业出版社");
		System.out.println(publisher);
	}

	@Test
	public void testFilePath() {
		String filePath = null;
		filePath = "/Volumes/Toto/totosweet/Documents/OnTheWay/Sweettoto/Databases/database data/2016-11-11_13-21-30  +0800/2016-11-11_13-21-30  +0800_EXCEL.xlsx";
		// filePath="2016-11-19_12-17-29_+0800/2016-11-19_12-17-29_+0800_EXCEL.xlsx";
		File file = new File(filePath);
		if (file.exists()) {
			System.out.println(file.getAbsolutePath());
		}
	}

	@Test
	public void testCreateFile() {
		String filePath = null;
		filePath = "2016-11-11_13-21-30  +0800_EXCEL_政治/军事.xlsx";
		// filePath="2016-11-19_12-17-29_+0800/2016-11-19_12-17-29_+0800_EXCEL.xlsx";
		System.out.println(File.pathSeparator);
		System.out.println(File.pathSeparatorChar);
		System.out.println(File.separator);
		System.out.println(File.separatorChar);
		filePath = filePath.replaceAll(File.separator, "|");
		// filePath=filePath.replaceAll("\\\\", "|");
		File file = new File(filePath);
		if (file.exists()) {
			System.out.println(file.getAbsolutePath());
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(file.getTotalSpace() + "bytes");
		}
	}

	public ArrayList<String> FormatData(ArrayList<String> book) {
		String author = book.get(0);
		if (author.matches("^\\(.\\).*")) {
			author = author.replaceAll("【", "[");
			author = author.replaceAll("】", "]");
			String[] authors = author.split(";", 2);
			String[] authors0 = authors[0].split("\\)", 2);
			System.out.println(authors0[0] + "\n" + authors0[1]);
			authors0[0] = authors0[0].replaceAll("\\(", "[");
			authors[0] = authors0[0] + "]" + authors0[1];
			author = authors[0] + ";" + authors[1];
			// System.out.println(author);
		}
		book.set(0, author);
		return book;
	}

	@Test
	public void testGetName() throws IOException {
		String filePath = null;
		filePath = "2016-11-11_13-21-30  +0800_EXCEL.xlsx";
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			System.out.println(file.getName());
			System.out.println(file.getName().matches(".*\\.xlsx$"));
		}
		String isbn = "71111";
		System.out.println(isbn.matches("(\\w){10}|(\\w){13}"));

		// String author="(美) 阿伦・拉奥(Arun Rao) [美] 皮埃罗・斯加鲁菲(Piero Scaruffi
		// )【美】安・汉德利(Ann Handley) ; 闫景立 侯爱华 闫勇(等译)";
		String author = "(美)梅若李・亚当斯(Marilee Adams) ; 秦瑛(等译)";
		ArrayList<String> book = new ArrayList<String>();
		book.add(author);
		// book = FormatData(book);
		System.out.println(book.get(0));

	}

	@Test
	public void testCoverFilter() {
		String coverPath = null;
		// coverPath="Workbook1.xlsx";
		// coverPath="test";
		coverPath = "00279358.jpg";
		File cover = new File(coverPath);
		if (cover.exists()) {
			System.out.println(cover.length() + " Bytes");
		}
	}

	@Test
	public void testRemoveRow() {
		String filePath = "1.xlsx";
		File input = new File(filePath);
		Workbook workbook = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		FileInputStream fileInputStream = null;
		if (input.exists()) {
			try {
				fileInputStream = new FileInputStream(input);
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
			// sheet=workbook.getSheetAt(0);
		} else {
			try {
				input.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			workbook = new SXSSFWorkbook();
			// sheet = workbook.createSheet();
		}

		// in case of that there are several sheets
		int numberOfSheets = workbook.getNumberOfSheets();
		System.out.println(numberOfSheets);
		for (int currentSheetIndex = 0; currentSheetIndex < numberOfSheets; currentSheetIndex++) {
			sheet = workbook.getSheetAt(currentSheetIndex);
			int lastNumberOfRow = sheet.getLastRowNum();
			System.out.println("the last number of row is " + lastNumberOfRow);
			// read all rows
			String isbn, title, author, publisher, publicationDate, priceOnTag, classfication, coverPath;
			// for (int currentRowIndex = 1; currentRowIndex < lastNumberOfRow;
			// currentRowIndex++) {
			// row = sheet.getRow(currentRowIndex);
			// int currentColumnIndex = 0;
			// for (currentColumnIndex = 0; currentColumnIndex <
			// row.getLastCellNum(); currentColumnIndex++) {
			// cell = row.getCell(currentColumnIndex);
			// System.out.print(cell.getStringCellValue() + "\t");
			// }
			// System.out.println("\n");
			// // isbn=cell.getStringCellValue();
			// //
			// // if (cell == null) {
			// // continue;
			// // } else {
			// // switch (cell.getCellType()) {
			// // case Cell.CELL_TYPE_NUMERIC: {
			// // isbn = Double.toString(cell.getNumericCellValue());
			// // getClass();
			// // System.out.println("type of the cell is numeric." +
			// // isbn);
			// // }
			// // break;
			// // case Cell.CELL_TYPE_STRING: {
			// // isbn = cell.getStringCellValue();
			// // System.out.println("type of the cell is String." + isbn);
			// // }
			// // ;
			// // break;
			// // default: {
			// // isbn = "";
			// // System.out.println("type of the cell is others." + isbn);
			// // }
			// // }
			// //
			// // isbn = Double.toString(cell.getNumericCellValue());
			// // if (!isbn.matches("(\\w){10}|(\\w){13}")) {
			// // sheet.removeRow(row);
			// // }
			//
			// }

		}

		try {
			fileInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(input);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			workbook.write(fileOutputStream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			fileOutputStream.close();
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
