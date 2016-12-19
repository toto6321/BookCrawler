package com.sweettoto.core;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author code4crafter@gmail.com <br>
 */
public class SinaBlogPageProcessor implements PageProcessor {

    public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";

    public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";

    private Site site = Site
            .me()
            .setDomain("blog.sina.com.cn")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
    	//output the page.getHtml() into a file
    			File page_getHtml_sina=new java.io.File("page_getHtml_sina");
    			if(!page_getHtml_sina.exists()){
    				try {
    					System.out.println("File doesn't exist and will be created later.");
    					page_getHtml_sina.createNewFile();
    				} catch (IOException e) {
    					System.out.println("File doesn't exist and failed to be created.");
    					e.printStackTrace();
    				}
    			}
    			FileWriter fileWriter=null;
    			try {
    				fileWriter=new FileWriter(page_getHtml_sina);
    			} catch (IOException e) {
    				System.out.println("failed to create a FileWriter instance.");
    				e.printStackTrace();
    			}
    			try {
    				fileWriter.write(page.getHtml().toString());
    			} catch (IOException e) {
    				System.out.println("fialed to write.");
    				e.printStackTrace();
    			}
    	
    	
        //列表页
        if (page.getUrl().regex(URL_LIST).match()) {
        	//print to show
        	System.out.println("page.getUrl().regex(URL_LIST).match() = true");
            
        	page.addTargetRequests(page.getHtml().xpath("//div[@class=\"articleList\"]").links().regex(URL_POST).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            //文章页
        } else {
            page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2"));
            page.putField("content", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']"));
            page.putField("date",
                    page.getHtml().xpath("//div[@id='articlebody']//span[@class='time SG_txtc']").regex("\\((.*)\\)"));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new SinaBlogPageProcessor()).addUrl("http://blog.sina.com.cn/s/articlelist_1487828712_0_1.html")
                .run();
    }
}