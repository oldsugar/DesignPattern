package com.dfe.oldsugar.test;

import org.junit.Test;

import com.dfe.oldsugar.lucene.Lucene_02_IndexUtil;

public class Test_Lucene_02_IndexUtil {

	@Test
	public void testIndex() {
		Lucene_02_IndexUtil lucene_02_IndexUtil = new Lucene_02_IndexUtil();
		lucene_02_IndexUtil.createIndex();
	}
	
	@Test
	public void testQuery() {
		Lucene_02_IndexUtil lucene_02_IndexUtil = new Lucene_02_IndexUtil();
		lucene_02_IndexUtil.query();
	}
	
	@Test
	public void testDelete(){
		Lucene_02_IndexUtil lucene_02_IndexUtil = new Lucene_02_IndexUtil();
		lucene_02_IndexUtil.deleteIndex();
	}
	
	@Test
	public void testForceDelete(){
		Lucene_02_IndexUtil lucene_02_IndexUtil = new Lucene_02_IndexUtil();
		lucene_02_IndexUtil.forceDeleteIndex();
	}
	
	@Test
	public void testRecover(){
		Lucene_02_IndexUtil lucene_02_IndexUtil = new Lucene_02_IndexUtil();
		lucene_02_IndexUtil.recover();
	}
	@Test
	public void testUpdate(){
		Lucene_02_IndexUtil lucene_02_IndexUtil = new Lucene_02_IndexUtil();
		lucene_02_IndexUtil.update();
	}
	
}

