package com.sweettoto.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.RandomStringUtils;

public class Test {

	public Test() {
		
	}
	
	@org.junit.Test
	public void test1(){
		System.out.println("mybooks1.xlsx".matches("^(\\w)*\\.xlsx$"));
		
//		System.out.println("ebook35001-40000_39840_zcover.jpg".substring("http://images.china-pub.com/ebook35001-40000/39840/zcover.jpg".lastIndexOf('/')+1));
		
		System.out.println(new RandomStringUtils().randomAlphanumeric(10));
	}

	@org.junit.Test
	public void test2(){
		String publicationDate="出版日期：2005 年1月";
		String priceOnTag="￥30.00";
		String title="UNIX环境高级编程(第2版)(09年度畅销榜TOP50)(08年度畅销榜TOP50)  ;  Advanced Programming in the UNIX Environment";
		
		title=title.replace("[按需印刷]", "");
		title=title.replace("(特价书)", "");
		title=title.replace("原书名：", " ");
		
		//regular express doesn't support Chinese Characters.
		title=title.replaceAll("\\(.+畅销.+\\)", "");
		title=title.trim();
		System.out.println(title);
		//it does support!
		System.out.println("--"+title.contains("高级"));
		
		publicationDate=publicationDate.replace("出版日期：", "");
		
		priceOnTag=priceOnTag.replaceAll("[^0-9\\.]", "");
		
		
		System.out.println(publicationDate);
		System.out.println(priceOnTag);
		
		String path="china-pub_books_1610042045_excel.xlsx";
		System.out.println(path.matches("^([^\\.])+\\.xlsx$"));
		
		System.out.println("\""+title.replaceAll("\"", "\"\"\"")+"\"");
		
		
		Calendar aCalendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss XXX");
		System.out.println(simpleDateFormat.format(aCalendar.getTime()));
		
		
	}
	
	@org.junit.Test
	//test regular expresses
	public void test3(){
		//level 0
		String link="www.china-pub.com/Browse/";
		System.out.println("level 0 --> : "+ link.matches("^(http://)?www.china-pub.com/Browse/$"));
		System.out.println("level 0 <--1 : "+ link.matches("http://www.china-pub.com/(\\w)+/$"));
		System.out.println("level 0 <--2 : "+ link.contains("/cache/browse"));
		System.out.println("level 0 <--3 : "+ link.matches("http://product.china-pub.com/(\\d)+$"));
		
		//level 1
		link="http://www.china-pub.com/math/";
		System.out.println("level 1 --> : "+ link.matches("http://www.china-pub.com/(\\w)+/$"));
		System.out.println("level 1 <--0 : "+ link.matches("^(http://)?www.china-pub.com$"));
		System.out.println("level 1 <--2 : "+ link.contains("/cache/browse"));
		System.out.println("level 1 <--3 : "+ link.matches("http://product.china-pub.com/(\\d)+$"));
		//level 2
		link="http://product.china-pub.com/cache/browse2/59/1_2_59-05_0.html";
		System.out.println("level 2 --> : "+ link.contains("/cache/browse"));
		System.out.println("level 2 <--0 : "+ link.matches("^(http://)?www.china-pub.com$"));
		System.out.println("level 2 <--1 : "+ link.matches("http://www.china-pub.com/(\\w)+/$"));
		System.out.println("level 2 <--3 : "+ link.matches("http://product.china-pub.com/(\\d)+$"));
		//level 3
		link="http://product.china-pub.com/4972764";
		System.out.println("level 3 --> : "+ link.matches("http://product.china-pub.com/(\\d)+$"));
		
		System.out.println("level 3 <--0 : "+ link.matches("^(http://)?www.china-pub.com$"));
		System.out.println("level 3 <--1 : "+ link.matches("http://www.china-pub.com/(\\w)+/$"));
		System.out.println("level 3 <--2 : "+ link.contains("/cache/browse"));
		
	}
	
	
	@org.junit.Test
	public void test4(){
		
		File output=new File("Output2/www.china-pub.com/");
		if(!output.exists()){
			System.out.println(output.mkdirs());
		}
		System.out.println(output.getAbsolutePath());
		File excel=new File(output.getAbsolutePath()+"/test3.xlsx");
		if(!excel.exists()){
			try {
				excel.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		System.out.println("absolute path: "+output.getAbsolutePath());
	}
	
	@org.junit.Test
	public void testPrintWriter(){
		File file = new File("testPrintWriter.txt");
		PrintWriter printWriter=null;
		
		try {
			printWriter= new PrintWriter(file);
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
		
		printWriter.write("信息论、编码与密码学  ;原书名：Information Theory,Coding and Cryptography");
		
		printWriter.close();
		
	}
}
