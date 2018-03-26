package com.dfe.oldsugar.lucene;

import org.junit.Test;

public class TestLucene_01 {
	
	@Test
	public void testCreateIndex() {
		Lucene_01 lucene_01 = new Lucene_01();
		lucene_01.createIndex();
	}
	
	@Test
	public void testSearchStr() {
		Lucene_01 lucene_01 = new Lucene_01();
		lucene_01.searchStr();;
	}

}
