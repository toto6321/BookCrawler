package com.sweettoto.util;

import org.junit.Test;

public class ExportToInsertSqlStatementTest {

	@Test
	public void test() {
		String inputFilePath="/Volumes/Toto/totosweet/Documents/OnTheWay/Sweettoto/Databases/database data/2016-11-11_13-21-30  +0800/2016-11-11_13-21-30  +0800_EXCEL_111111_通信.xlsx";
		ExportToInsertSqlstatement exportToInsertSqlstatement = new ExportToInsertSqlstatement();
		exportToInsertSqlstatement.tranlate(inputFilePath);
		// exportToInsertSqlstatement.
	}

}
