package com.dfe.oldsugar.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.index.IndexReader.FieldOption;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 


/** Shows facets aggregation by an expression. */
public class Lucene_02_IndexUtil {
	private String[] ids = {"1", "2", "3", "4", "5", "6"};
	private String[] emails = {"liubin@qq.com", "wujunyue@163.com", "liuyanle@hotmail.com", "yanghengkun@127.com", "tangjianhua@dongfang-china.com", "110@55.com"};
	private String[] contents = {"binge", "jiauanngshiy", "boshi", "dongfang", "benben", "报警"};
	private int[] attachments = {1, 2, 3, 4, 5, 6};
	private String[] names = {"1324", "qwer", "asdf", "zxcv", "tyui", "ghjk"};
	
	private Directory directory = null;
	
	public Lucene_02_IndexUtil() {
		try {
			directory = FSDirectory.open(new File("G:\\testLucene\\index02"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createIndex() {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(directory,
					new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			//先清空索引
			indexWriter.deleteAll();
			Document document = null;
			for (int i = 0; i < ids.length; i++) {
				document = new Document();
				document.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				document.add(new Field("email", emails[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("contents", contents[i], Field.Store.NO, Field.Index.ANALYZED));
				document.add(new Field("name", names[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
//				document.add(new Field("attachments", attachments[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
				indexWriter.addDocument(document);
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void query() {
		IndexReader indexReader = null;
		try {
			indexReader = IndexReader.open(directory);
			//用过IndexReader可以有效地获取到文档的数量
			System.out.println("文件数量：" + indexReader.numDocs());
			System.out.println("最大文件数：" + indexReader.maxDoc());
			System.out.println("回收站文件数：" + indexReader.numDeletedDocs());
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexReader != null) {
				try {
					indexReader.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void deleteIndex() {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			//参数为一个选择，可以是一个query;也可以是一个term,term为精确查找的一个值
			//此时删除的文档，并不会完全被删除，而是存储在一个回收站里，可以恢复
			indexWriter.deleteDocuments(new Term("id", "1"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//强制删除
	public void forceDeleteIndex() {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			//参数为一个选择，可以是一个query;也可以是一个term,term为精确查找的一个值
			//此时删除的文档，并不会完全被删除，而是存储在一个回收站里，可以恢复
			indexWriter.forceMergeDeletes();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//恢复删除
	public void recover() {
		//使用IndexReader进行
		try {
			IndexReader indexReader = IndexReader.open(directory, false);
			indexReader.undeleteAll();
			indexReader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//优化 和 合并索引
	public void merge() {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			//会把索引合并为两段，这两段中被删除的数据会被清空
			//Lucene在3.5版本后不建议使用
			indexWriter.forceMerge(2);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//更新索引
	public void update() {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			/*
			 * Lucene并没有提供更新，这里的更新操作其实是如下两个操作
			 * ①  删除
			 * ②  添加
			 */
			/*
			 * 以下情况，重新建索引之后结果为
			 * 	文件数量：6
			 *	最大文件数：7
			 *	回收站文件数：1
			 */
			Document document = new Document();
			document.add(new Field("id", "11", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			document.add(new Field("email", emails[0], Field.Store.YES, Field.Index.NOT_ANALYZED));
			document.add(new Field("contents", contents[0], Field.Store.NO, Field.Index.ANALYZED));
			document.add(new Field("name", names[0], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			indexWriter.updateDocument(new Term("id", "1"), document);
			
			/*
			 * 以下情况，重新建索引之后结果为
			 * 	文件数量：7
			 *	最大文件数：7
			 *	回收站文件数：0
			 */
			/*Document document = new Document();
			document.add(new Field("id", "1", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			document.add(new Field("email", emails[0], Field.Store.YES, Field.Index.NOT_ANALYZED));
			document.add(new Field("contents", contents[0], Field.Store.NO, Field.Index.ANALYZED));
			document.add(new Field("name", names[0], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			indexWriter.updateDocument(new Term("id", "11"), document);*/
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter != null) {
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
