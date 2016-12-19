package com.sweettoto.core;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 
 * @author sweettoto
 * @lastModified 2016/10/07 02:11
 */

public class BooksPageProcessor_copy_1610070211 implements PageProcessor {
	private final static String SITE_DOMAIN = "http://china-pub.com/";
//	private final static String START_URL = "http://product.china-pub.com/cache/browse2/31/2_1_31-01-05-10_0.html";
	// private final static String START_URL =
	// "http://www.china-pub.com/Browse/";
	private final static String START_URL="http://product.china-pub.com/3770057";
	private final static String HELP_URL_PATTERN = "http://product\\.china-pub\\.com/cache/browse2/31/(.)+\\.html$";
	private final static String TARGET_URL_PATTERN = "http://product\\.china-pub\\.com/(.)+$";

	// keep a static parameter to count
	private static long count = 0;
	private Site site = Site.me().setDomain(SITE_DOMAIN).setSleepTime(3000).setRetryTimes(3);

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {

		String url = page.getUrl().get();
		// to find the site

		// directories in uppest level
		if (url.matches("^(http://)?www.china-pub.com/Browse/$")) {
			List<String> links = page.getHtml().xpath("//div[@id='wrap']//a/@href").all();
			System.out.println("---0---links found in directories in uppest level------");
			System.out.println(links.toString());
			page.addTargetRequests(links);
		}
		// directories in level one
		else if (url.matches("http://www.china-pub.com/(\\w)+/$")) {

			// add the link behind the classification label
			List<String> links1 = page.getHtml().xpath("//div[@class='left_t']/div[@class='sort_second']/ul/li/a/@href")
					.all();
			System.out.println("---1----links found in directories in level one------");
			System.out.println(links1.toString());
			page.addTargetRequests(links1);

			// add the link behind the more classification label
			List<String> links2 = page.getHtml().xpath("//div[@class='left_t']/div[@class='sort_second']/p/a/@href")
					.all();
			System.out.println("---1----links found in directories in level one------");
			System.out.println(links2.toString());
			page.addTargetRequests(links2);
		}
		// sites in second level
		// 列表页
		else if (url.contains("cache/browse2")) {
			List<String> links3 = page.getHtml()
					.xpath("//div[@class=\"search_result\"]//td/a[@target=\"_blank\"]/@href").all();
			System.out.println("---3---" + links3.toString() + "------");
			page.addTargetRequests(links3);

			List<String> links4 = page.getHtml().xpath("//div[@class=\"pro_pag\"]//td/a/@href").all();
			System.out.println("---3---" + links4.toString() + "------");
			page.addTargetRequests(links4);
		}
		// sites in third level
		// 详情页
		else if (url.matches("http://product.china-pub.com/(\\d)+$")) {
			System.out.println(url);
			String isbn = null, title = null, author = null, publisher = null, publicationDate = null,
					priceOnTag = null, link = null;

			// check if we can get all the li node, if so, we can map the li
			// node by its text.
			System.out.println(page.getHtml().xpath("//div[@id='con_a_1']/div[1]/ul[1]/li"));
			List<Selectable> li = page.getHtml().xpath("//div[@id='con_a_1']/div[1]/ul[1]/li").nodes();
			for (Object element : li) {
				Selectable selectable = (Selectable) element;
				System.out.println(selectable.toString());
			}

			// model1 for books in translations
			if (page.getHtml().xpath("//div[@id='con_a_1']/div[1]/ul[2]/li[5]/strong/text()") != null
					& page.getHtml().xpath("//div[@class='pro_r_deta']/ul[2]/li[5]/strong/text()").toString() != null) {
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

			// model2 for book whose information contain "丛书名"
			else if (page.getHtml().xpath("//div[@id='con_a_1']/div[1]/ul/li[4]/strong/text()") != null
					& page.getHtml().xpath("//div[@id='con_a_1']/div[1]/ul/li[4]/strong/text()").toString() != null) {
				isbn = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[4]/strong/text()").toString();
				title = page.getHtml().xpath("//div[@id=\"main\"]//div[@class=\"pro_book\"]/h1/text()").toString();
				author = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[1]/a/strong/text()").toString();
				publisher = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[3]/a/text()").toString();
				publicationDate = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[6]/text()").toString();
				// Selectable t2=page.getHtml()
				// .xpath("//div[@id=\"main\"]//div[@class=\"pro_buy_intr\"]/ul/li/span[@class=\"pro_buy_pri\"]");
				priceOnTag = page.getHtml()
						.xpath("//div[@id=\"main\"]//div[@class=\"pro_buy_intr\"]/ul/li/span[@class=\"pro_buy_pri\"]/text()")
						.toString();
				link = page.getUrl().get();
			}
			// model3 for books
			else if (page.getHtml().xpath("//div[@id='con_a_1']/div[1]/ul/li[3]/strong/text()") != null
					& page.getHtml().xpath("//div[@id='con_a_1']/div[1]/ul/li[3]/strong/text()").toString() != null) {
				isbn = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[3]/strong/text()").toString();
				title = page.getHtml().xpath("//div[@id=\"main\"]//div[@class=\"pro_book\"]/h1/text()").toString();
				author = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[1]/a/strong/text()").toString();
				publisher = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[2]/a/text()").toString();
				publicationDate = page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li[5]/text()").toString();
				// Selectable t2=page.getHtml()
				// .xpath("//div[@id=\"main\"]//div[@class=\"pro_buy_intr\"]/ul/li/span[@class=\"pro_buy_pri\"]");
				priceOnTag = page.getHtml()
						.xpath("//div[@id=\"main\"]//div[@class=\"pro_buy_intr\"]/ul/li/span[@class=\"pro_buy_pri\"]/text()")
						.toString();
				link = page.getUrl().get();
			}
			// else {
			// Selectable aSelectable =
			// page.getHtml().xpath("//div[@class='pro_r_deta']/ul/li");
			// System.out.println("this element is " +
			// aSelectable.smartContent().get());
			// }

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
				title = title.replace("[按需印刷]", "");
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

			// add a custom timestamp
			Calendar aCalendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd/ HH:mm:ss XXX");
			String timestamp = simpleDateFormat.format(aCalendar.getTime());
			if (isbn != null & isbn != "") {
				page.putField("ISBN", isbn);
				page.putField("TITLE", title);
				page.putField("AUTHOR", author);
				page.putField("PUBLISHER", publisher);
				page.putField("PUBLICATIONDATE", publicationDate);
				page.putField("PRICEONTAG", priceOnTag);
				page.putField("LINK", link);
				page.putField("DATEADDED", timestamp);

				// count
				page.putField("COUNT", ++count);
			} else {
				page.setSkip(true);
			}
		}

	}

	public static void main(String[] args) {

		// to use FileCacheQueueScheduler(), you should create two file at first
		File filePath = new File("Output/http://china-pub.com/");
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		File urls = new File(".urls.txt");
		if (!urls.exists()) {
			try {
				urls.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File cursor = new File(".cursor.txt");
		if (!urls.exists()) {
			try {
				cursor.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Calendar aCalendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss  Z");
		String timestamp = simpleDateFormat.format(aCalendar.getTime());

		// to store data into Json, Excel and csv files.
		// to create a directory to collect data output.
		File output = new File("Output/www.china-pub.com/" + timestamp);
		if (!output.exists()) {
			System.out.println("is successfully to create the directory? \n" + output.mkdirs());
		}

		Spider.create(new BooksPageProcessor_copy_1610070211()).addUrl(START_URL)
				.setScheduler(new FileCacheQueueScheduler("Output/"))
				// .addPipeline(new JsonFilePipeline(
				// output.getAbsolutePath() + "/" + timestamp + "_JSON"))
				.addPipeline(new XLSXPipeline(output.getAbsolutePath() + "/" + timestamp + "_EXCEL.xlsx"))
				// .addPipeline(new CSVPipeline(
				// output.getAbsolutePath() + "/" +
				// simpleDateFormat.format(aCalendar.getTime()) + "_CSV.csv"))
				.addPipeline(new ConsolePipeline()).run();
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
		// Spider.create(new
		// BooksPageProcessor()).addUrl(START_URL).addPipeline(new
		// ConsolePipeline())
		// .addPipeline(new CSVPipeline("testCSV.csv")).run();

		// Spider.create(new BooksPageProcessor())
		// .setScheduler(new QueueScheduler().setDuplicateRemover(new
		// BloomFilterDuplicateRemover(1000000)))
		// .addUrl(START_URL).addPipeline(new ConsolePipeline()).run();
	}

	public static String getHelpUrlPattern() {
		return HELP_URL_PATTERN;
	}

	public static String getTargetUrlPattern() {
		return TARGET_URL_PATTERN;
	}
}
