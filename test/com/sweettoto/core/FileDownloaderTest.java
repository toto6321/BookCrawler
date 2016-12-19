package com.sweettoto.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sweettoto.util.FileDownloader;

public class FileDownloaderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		//parameters
		String url="http://images.china-pub.com/ebook4970001-4975000/4972764/zcoverjpg";
//		String filePath=url.substring(url.lastIndexOf('/')+1);
		
		
		if(new FileDownloader(url).downlaodFileFromUrl()){
			System.out.println("download successfully");
		}
		
		
	}

}
