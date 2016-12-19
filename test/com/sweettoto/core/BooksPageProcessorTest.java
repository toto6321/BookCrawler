package com.sweettoto.core;

import org.junit.Test;

public class BooksPageProcessorTest {

	@Test
	public void test() {
		System.out.println("http://product.china-pub.com/94".matches("http://product\\.china-pub\\.com/cache/browse2/31/(.)+\\.html"));
	}

}
