package com.sweettoto.core;

import java.util.Map;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class ConsolePipeline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {
		// to print data on console

		// to print the url of source page
		//System.out.println("Page: " + resultItems.getRequest().getUrl());
		/*
		 * // to retrieve data from the result if(resultItems.get("ISBN") !=
		 * null){ System.out.println("ISBN: " +
		 * resultItems.get("ISBN").toString().trim());
		 * System.out.println("title: " +
		 * resultItems.get("TITLE").toString().trim());
		 * System.out.println("author: " +
		 * resultItems.get("AUTHOR").toString().trim());
		 * System.out.println("publisher: " +
		 * resultItems.get("PUBLISHER").toString().trim());
		 * System.out.println("publicationDate: " +
		 * resultItems.get("PUBLICATIONDATE").toString().trim());
		 * System.out.println("priceOnTag: " +
		 * resultItems.get("PRICEONTAG").toString());
		 * System.out.println("link: " +
		 * resultItems.get("LINK").toString().trim()); }
		 */

		for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
		System.out.println();

	}

}
