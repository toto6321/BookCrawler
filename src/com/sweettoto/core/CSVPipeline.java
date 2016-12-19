package com.sweettoto.core;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class CSVPipeline implements Pipeline {

	private String filePath;

	/**
	 * path is the path of the file in csv, ended with .csv such as output.csv
	 * 
	 * @param path
	 */
	public CSVPipeline(String path) {
		filePath = path;

	}

	public CSVPipeline() {
		this.filePath = "MyBooks1_csv.csv";
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		// Not util the "ISBN" is not null should the program to store data run
		// ahead.
		if (resultItems.get("ISBN") != null) {
			if (!filePath.matches("^.+\\.csv$")) {
				filePath = "MyBooks1_csv.csv";
			}
			File mybooks1 = new File(filePath);
			if (!mybooks1.exists()) {
				try {
					mybooks1.createNewFile();
				} catch (IOException e) {
					System.out.println("There is something wrong when creating a new file in csv.");
					e.printStackTrace();
				}
			}
			/*
			 * FileOutputStream fileOutputStream=null; try {
			 * fileOutputStream=new FileOutputStream(mybooks1,true); } catch
			 * (FileNotFoundException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 */

			FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter(filePath, true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			String isbn = resultItems.get("ISBN").toString().trim();
			String title = resultItems.get("TITLE").toString().trim();
			String author = resultItems.get("AUTHOR").toString().trim();
			String publisher = resultItems.get("PUBLISHER").toString().trim();
			String publicationDate = resultItems.get("PUBLICATIONDATE").toString().trim();
			String priceOnTag = resultItems.get("PRICEONTAG").toString().trim();
			String classification = resultItems.get("CLASSIFICATION");
			String link = resultItems.get("LINK").toString();
			String coverURL = resultItems.get("COVERURL").toString();
			String coverPath = String.format(isbn + coverURL.substring(coverURL.lastIndexOf('.')));

			// extra data
			String timestamp = resultItems.get("DATEADDED");
			String ISBN_13 = null;
			if (isbn.length() == 13) {
				ISBN_13 = isbn;
			} else {
				ISBN_13 = "978" + isbn;
			}

			// to format for CSV(Comma Separated Values Format)
			// to quote the double quotation mark with enclosed double quotation
			// marks
			// heads are: ISBN, TITLE, AUHTOR, PUBLISHER, PUBLICATION,
			// PRICEONTAG, CLASSIFICATION, COVERPATH, LINK, COVERURL, ADDEDDATE,
			// ISBN-13
			try {
				fileWriter.write(csvFormat(isbn) + "," + csvFormat(title) + "," + csvFormat(author) + ","
						+ csvFormat(publisher) + "," + csvFormat(publicationDate) + "," + csvFormat(priceOnTag) + ","
						+ csvFormat(classification) + "," + csvFormat(coverPath) + "," + csvFormat(link) + ","
						+ csvFormat(coverURL) + "," + csvFormat(timestamp) + "," + csvFormat(ISBN_13) + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String csvFormat(String key) {
		key = "\"" + key.replaceAll("\"", "\"\"\"") + "\"";
		return key;
	}

}
