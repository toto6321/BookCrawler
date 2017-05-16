package com.sweettoto.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.RandomStringUtils;

public class FileDownloader {
	private String url = null;
	private String filePath = null;

	@SuppressWarnings("static-access")
	public FileDownloader(String url) {
		this.url = url;

		if (url.matches(".+/.*\\.(\\w)+")) {
			this.filePath = url.substring(url.lastIndexOf('/') + 1);
		}else if(url.matches(".+\\.(\\w)+")){
			this.filePath=String.format((new RandomStringUtils().randomAlphanumeric(10))+url.substring(url.lastIndexOf('.')));
		}else{
			Exception exception=new Exception("url invalid!");
			exception.printStackTrace();
		}
	}

	public FileDownloader(String url, String filePath) {
		this.url = url;
		this.filePath = filePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilePath() {
		return filePath;
	}

	/**
	 * this is method is designed to setting what directory the file will be in
	 * 
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean downlaodFileFromUrl() {
		URL urlInstance = null;
		try {
			urlInstance = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) urlInstance.openConnection();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		// to create a instance of FileOutputStream to read from url
		InputStream inputStream = null;
		try {
			inputStream = httpURLConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			urlInstance.openConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// to create the target file to output
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// create a instance of FileOutputStream to prepare to store file.
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// set a parameter to record
		int readPosition = 0;

		byte[] bytes = new byte[4096];
		int readReturn = 0;
		while (readReturn != -1) {
			// read from given url
			try {
				readReturn = inputStream.read(bytes, 0, 4096);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// write into target file
			if (readReturn != -1) {
				try {
					fileOutputStream.write(bytes, 0, readReturn);
					fileOutputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// close stream
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpURLConnection.disconnect();

		return true;
	}

}
