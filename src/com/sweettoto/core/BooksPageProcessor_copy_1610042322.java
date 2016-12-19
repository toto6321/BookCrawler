package com.sweettoto.core;


import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 
 * @author sweettoto
 * @lastModified 2016/10/04 22:20
 */

public class BooksPageProcessor_copy_1610042322 implements PageProcessor {
	private final static String SITE_DOMAIN = "http://china-pub.com/";
	private final static String START_URL = "http://product.china-pub.com/cache/browse2/31/2_1_31-01-05-10_0.html";
	// private final static String START_URL =
	// "http://product.china-pub.com/4799";
	private final static String HELP_URL_PATTERN = "http://product\\.china-pub\\.com/cache/browse2/31/(.)+\\.html$";
	private final static String TARGET_URL_PATTERN = "http://product\\.china-pub\\.com/(.)+$";

	// configure the site
	// private Site site =
	// Site.me().setDomain(SITE_DOMAIN).setSleepTime(3000).setRetryTimes(3);
	private Site site = Site.me().setDomain(SITE_DOMAIN).setSleepTime(3000).setRetryTimes(3);

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		String url = page.getUrl().get();
		if (url.contains("cache/browse2")) {
			// 列表页
			page.addTargetRequests(
					page.getHtml().xpath("//div[@class=\"search_result\"]//td/a[@target=\"_blank\"]/@href").all());
			page.addTargetRequests(page.getHtml().xpath("//div[@class=\"pro_pag\"]//td/a/@href").all());
			System.out.println(url);
		}
		// 详情页

		else {
			System.out.println(url);
			String isbn = null, title = null, author = null, publisher = null, publicationDate = null,
					priceOnTag = null, link = null;

			// model1 for books in translations
			if (page.getHtml().xpath("//div[@class='pro_r_deta']/ul[2]/li[5]/strong/text()") != null) {
				// to store
				// to format the data at first
				String temp = null;
				// String title2=null;
				isbn = page.getHtml().xpath("//div[@class='pro_r_deta']/ul[2]/li[5]/strong/text()").toString();

				title = page.getHtml().xpath("//div[@id=\"main\"]//div[@class=\"pro_book\"]/h1/text()").toString();
				temp = page.getHtml().xpath("//div[@class='pro_r_deta']/ul[1]/li/text()").toString();
				if (temp != null & temp != "") {
					title = title + " ; " + temp;
				}

				author = page.getHtml().xpath("//div[@class='pro_r_deta']/ul[2]/li[1]/a/strong/text()").toString();
				temp = page.getHtml().xpath("//div[@class='pro_r_deta']/ul[2]/li[2]/a/text()").toString();
				if (temp != null & temp != "") {
					author = author + " ; " + temp + "(等译)";
				}

				publisher = page.getHtml().xpath("//div[@class='pro_r_deta']/ul[2]/li[4]/a/text()").toString();
				publicationDate = page.getHtml().xpath("//div[@class='pro_r_deta']/ul[2]/li[7]/text()").toString();
				priceOnTag = page.getHtml()
						.xpath("//div[@id=\"main\"]//div[@class=\"pro_buy_intr\"]/ul/li/span[@class=\"pro_buy_pri\"]/text()")
						.toString();
				link = page.getUrl().get();
			}
			// model2 for others
			else if (page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[4]/strong/text()") != null) {
				isbn = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[4]/strong/text()").toString();
				title = page.getHtml().xpath("//div[@id=\"main\"]//div[@class=\"pro_book\"]/h1/text()").toString();
				author = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[1]/text()").toString();
				publisher = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[3]/text()").toString();
				publicationDate = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[6]/text()").toString();
				priceOnTag = page.getHtml()
						.xpath("//div[@id=\"main\"]//div[@class=\"pro_buy_intr\"]/ul/li/span[@class=\"pro_buy_pri\"]/text()")
						.toString();
			}

			// pass the parameters whether the isbn is null so that JsonFilePipe
			// will keep a complete backup.

			// to store data after formatted, and fill them with a default value
			// when the parameter points to null.
			if (isbn != null) {
				isbn = isbn.trim();
			} else {
				isbn = "";
			}

			if (title != null) {
				title = title.trim();
				title = title.replace("(特价书)", "");
				title = title.replace("原书名：", " ");
				// title=title.replaceAll("\\(.*畅销\\)", "");
			} else {
				title = "";
			}

			if (author != null) {
				author = author.trim();
				author = author.replaceAll("（", "(");
				author = author.replaceAll("）", ")");
			} else {
				author = "";
			}

			if (publisher != null) {
				publisher = publisher.trim();
			} else {
				publisher = "";
			}

			if (publicationDate != null) {
				publicationDate = publicationDate.trim();
				publicationDate = publicationDate.replace("出版日期：", "");
			} else {
				publicationDate = "";
			}

			if (priceOnTag != null) {
				priceOnTag = priceOnTag.trim();
				priceOnTag = priceOnTag.replaceAll("[^0-9\\.]", "");
			} else {
				priceOnTag = "";
			}

			if (link != null) {
				link = link.trim();
			} else {
				link = "";
			}

			if (isbn != "") {
				page.putField("ISBN", isbn);
				page.putField("TITLE", title);
				page.putField("AUTHOR", author);
				page.putField("PUBLISHER", publisher);
				page.putField("PUBLICATIONDATE", publicationDate);
				page.putField("PRICEONTAG", priceOnTag);
				page.putField("LINK", link);
			}else{
				page.setSkip(true);
			}

		}
	}

	public static void main(String[] args) {
		// store data into Json, Excel and csv files.
		// Spider.create(new BooksPageProcessor()).addUrl(START_URL)
		// .addPipeline(new JsonFilePipeline("china-pub_books_1610042045_JSON"))
		// .addPipeline(new
		// XLSXPipeline("china-pub_books_1610042045_EXCEL.xlsx"))
		// .addPipeline(new
		// CSVPipeline("china-pub_books_1610042045_CVS.cvs")).run();
		// Spider.create(new
		// BooksPageProcessor()).addUrl(START_URL).addPipeline(new
		// XLSXPipeline()).run();
		// Spider.create(new
		// BooksPageProcessor()).addUrl(START_URL).addPipeline(new
		// ConsolePipeline())
		// .addPipeline(new CSVPipeline("mybookS1.csv")).addPipeline(new
		// JsonFilePipeline("jsonFile")).run();
		// Spider.create(new
		// BooksPageProcessor()).addUrl(START_URL).addPipeline(new
		// ConsolePipeline())
		// .addPipeline(new XLSXPipeline("MyBooks_excel1.xlsx")).addPipeline(new
		// CSVPipeline("MyBooks1_csv.csv"))
		// .run();
		// Spider.create(new
		// BooksPageProcessor()).addUrl(START_URL).addPipeline(new
		// CSVPipeline("MyBooks1_csv.csv"))
		// .run();
		Spider.create(new BooksPageProcessor_copy_1610042322()).addUrl(START_URL).addPipeline(new ConsolePipeline())
				.addPipeline(new CSVPipeline("testCSV.csv")).run();
		// Spider.create(new
		// BooksPageProcessor()).addUrl(START_URL).addPipeline(new
		// XLSXPipeline("test_161004.xlsx"))
		// .run();
	}

	public static String getHelpUrlPattern() {
		return HELP_URL_PATTERN;
	}

	public static String getTargetUrlPattern() {
		return TARGET_URL_PATTERN;
	}
}
