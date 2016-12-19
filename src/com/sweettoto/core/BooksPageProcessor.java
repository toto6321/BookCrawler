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
import us.codecraft.webmagic.selector.Selectable;

/**
 * 
 * @author sweettoto
 * @lastModified 2016/10/07 04:10
 */

public class BooksPageProcessor implements PageProcessor {
	private final static String SITE_DOMAIN = "http://china-pub.com/";
	private final static String START_URL = "http://product.china-pub.com/1522312";
	// private final static String START_URL =
	// "http://product.china-pub.com/cache/browse2/31/2_1_31-01-05-10_0.html";
	// private final static String START_URL =
	// "http://www.china-pub.com/Browse/";
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
			// System.out.println("---0---links found in directories in uppest
			// level------");
			// System.out.println(links.toString());
			page.addTargetRequests(links);
		}
		// directories in level one
		else if (url.matches("http://www.china-pub.com/(\\w)+/$")) {

			// add the link behind the classification label
			List<String> links1 = page.getHtml().xpath("//div[@class='left_t']/div[@class='sort_second']/ul/li/a/@href")
					.all();
			// System.out.println("---1----links found in directories in level
			// one------");
			// System.out.println(links1.toString());
			page.addTargetRequests(links1);

			// add the link behind the more classification label
			List<String> links2 = page.getHtml().xpath("//div[@class='left_t']/div[@class='sort_second']/p/a/@href")
					.all();
			// System.out.println("---1----links found in directories in level
			// one------");
			// System.out.println(links2.toString());
			page.addTargetRequests(links2);
		}
		// sites in second level
		// 列表页
		else if (url.contains("cache/browse2")) {
			List<String> links3 = page.getHtml()
					.xpath("//div[@class=\"search_result\"]//td/a[@target=\"_blank\"]/@href").all();
			// System.out.println("---3---" + links3.toString() + "------");
			page.addTargetRequests(links3);

			List<String> links4 = page.getHtml().xpath("//div[@class=\"pro_pag\"]//td/a/@href").all();
			// System.out.println("---3---" + links4.toString() + "------");
			page.addTargetRequests(links4);
		}
		// sites in third level
		// 详情页
		else if (url.matches("http://product.china-pub.com/(\\d)+$")) {
			System.out.println(url);
			String isbn = null, title = null, author = null, publisher = null, publicationDate = null,
					priceOnTag = null, link = null, classification = null;
			String originalBookName = null;
			String translator = null;
			String coverURL = null;

			// for all books

			// link
			link = page.getUrl().get();

			// picture
			coverURL = page.getHtml().xpath("//div[@class='pro_book_img']//img/@src").toString();

			// classification
			classification = page.getHtml().xpath("//div[@id='con_a_1']/div[1]/ul/li/span/a/text()").toString();

			// priceOnTag
			priceOnTag = page.getHtml()
					.xpath("//div[@id=\"main\"]//div[@class=\"pro_buy_intr\"]/ul/li/span[@class=\"pro_buy_pri\"]/text()")
					.toString();
			if (priceOnTag == null || priceOnTag == "") {
				List<Selectable> priceOnTagNodes = page.getHtml().xpath("//div[@class='pro_book']//ul/li").nodes();
				for (Object element : priceOnTagNodes) {
					Selectable selectable = (Selectable) element;
					if (selectable.get().contains("定价")) {
						priceOnTag = selectable.toString().replaceAll("[^0-9\\.]", "");
						break;
					} else {
						continue;
					}
				}
			}

			// for ISBN, author, publisher, publicationDate
			List<Selectable> data1 = page.getHtml().xpath("//div[@id='con_a_1']/div[1]/ul/li").nodes();
			for (Object element : data1) {
				Selectable selectable = (Selectable) element;
				// print to compare
				// System.out.println(selectable.toString());
				if (selectable.get().contains("ISBN")) {
					isbn = selectable.xpath("//strong/text()").toString();
				} else if (selectable.get().contains("作者")) {
					author = selectable.xpath("//a/strong/text()").toString();
				} else if (selectable.get().matches(".*出版社.*")) {
					publisher = selectable.xpath("//a/text()").toString();
					if (publisher == null || publisher == "") {
						publisher = selectable.toString();
					}
				} else if (selectable.get().matches(".*出版日期.*")) {
					publicationDate = selectable.toString();
				} else if (selectable.get().contains("原书名")) {
					originalBookName = selectable.toString();
				} else if (selectable.get().contains("译者")) {
					translator = selectable.xpath("//a/text()").toString();
				} else {
					continue;
				}
			}

			// title
			title = page.getHtml().xpath("//div[@id=\"main\"]//div[@class=\"pro_book\"]/h1/text()").toString();
			if (originalBookName != null & originalBookName != "") {
				// System.out.println(originalBookName);
				// System.out.println(originalBookName.length());
				title = title + " ; " + originalBookName;
			}

			// author
			if (translator != null & translator != "") {
				author = author + " ; " + translator + "(等译)";
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
				title = title.replace("<li>", "");
				title = title.replace("</li>", "");
				title = title.replace("(特价书)", "");
				title = title.replace("原书名：", " ");
				title = title.replace("[按需印刷]", "");
				// title=title.replaceAll("\\(.*畅销\\)", "");
			} else {
				title = "";
			}

			if (author != null) {
				author = author.replaceAll("作者", "");
				author = author.replaceAll("（", "(");
				author = author.replaceAll("）", ")");
				author = author.trim();
			} else {
				author = "";
			}

			if (publisher != null) {
				publisher = publisher.replaceAll("出版社：", "");
				publisher = publisher.replaceAll("<li>", "");
				publisher = publisher.replaceAll("</li>", "");
				publisher = publisher.replaceAll("\\*", "");
				publisher = publisher.trim();
			} else {
				publisher = "";
			}

			if (publicationDate != null) {
				publicationDate = publicationDate.replace("出版日期：", "");
				publicationDate = publicationDate.replace("<li>", "");
				publicationDate = publicationDate.replace("</li>", "");
				publicationDate = publicationDate.trim();
			} else {
				publicationDate = "";
			}

			if (priceOnTag != null) {
				priceOnTag = priceOnTag.replaceAll("[^0-9\\.]", "");
				priceOnTag = priceOnTag.trim();
			} else {
				priceOnTag = "";
			}

			if (classification != null) {
				classification = classification.trim();
			} else {
				classification = "";
			}

			if (link != null) {
				link = link.trim();
			} else {
				link = "";
			}

			// add a custom timestamp
			Calendar aCalendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss XXX");
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
				page.putField("COVERURL", coverURL);
				page.putField("CLASSIFICATION", classification);

				// count
				// page.putField("COUNT", ++count);
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

		// Spider.create(new BooksPageProcessor()).addUrl(START_URL)
		// .setScheduler(new FileCacheQueueScheduler("Output/")
		// .setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
		// .addPipeline(new JsonFilePipeline(output.getAbsolutePath() + "/" +
		// timestamp + "_JSON"))
		// .addPipeline(new XLSXPipeline(output.getAbsolutePath() + "/" +
		// timestamp + "_EXCEL.xlsx"))
		// .addPipeline(new CSVPipeline(
		// output.getAbsolutePath() + "/" +
		// simpleDateFormat.format(aCalendar.getTime()) + "_CSV.csv"))
		// .addPipeline(new FileDownloaderPipeline(output.getAbsolutePath() +
		// "/" + "Covers"))
		// .addPipeline(new ConsolePipeline()).run();

		// Spider.create(new
		// BooksPageProcessor()).addUrl(START_URL).addPipeline(new
		// ConsolePipeline())
		// .addPipeline(new FileDownloaderPipeline(output.getAbsolutePath() +
		// "/"
		// + "Covers"))
		// .addPipeline(new XLSXPipeline(output.getAbsolutePath() + "/" +
		// timestamp + "_EXCEL.xlsx")).run();

		// test
		Spider.create(new BooksPageProcessor()).addPipeline(new ConsolePipeline()).test(START_URL);
	}

	public static String getHelpUrlPattern() {
		return HELP_URL_PATTERN;
	}

	public static String getTargetUrlPattern() {
		return TARGET_URL_PATTERN;
	}
}
