package com.dfe.oldsugar.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
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
public class Lucene_01 {

	public void createIndex() {
		Directory directory = null;

		IndexWriterConfig indexWriterConfig = null;
		IndexWriter indexWriter = null;
		try {
			// 1、创建Directory
//			directory = new RAMDirectory();
			directory = FSDirectory.open(new File("G:\\testLucene\\index"));
			
			// 2、创建IndexWriter
			indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35,
					new StandardAnalyzer(Version.LUCENE_35));
			
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			//3、创建Document对象,相当于数据库的一条记录
			Document document = null;
			//4、为Document对象添加Field
			File files = new File("G:\\testLucene\\testDocs");
			for (File file : files.listFiles()) {
				document = new Document();
				//Field相当于一条记录中的一个字段
				document.add(new Field("content", new FileReader(file)));
				/**
				 * 存储域选项
				 * Field.Store.YES：表示把这个域中的内容完全存储到文件中，方便进行我文本的还原
				 * Field.Store.NO：表示把域的内容不存储到文件中，但可以被索引。此时内容无法完全还原
				 * 
				 * 索引选项
				 * Field.Index.ANALYZED:进行分词和索引，适用于标题、内容等；
				 * Field.Index.NOT_ANALYZED:进行索引，但是不进行分词，如身份证号、姓名、ID等，适用于精确搜索
				 * Field.Index.ANALYZED_NOT_NORMS:进行分词，但不存储norms信息，这个norms中包括了创建索引的时间和权值信息等
				 * Field.Index.NOT_ANALYZED_NOT_NORMS:即部进行分词也不存储norms信息
				 * Field.Index.NO:部进行索引
				 */
				document.add(new Field("fileName", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				indexWriter.addDocument(document);
			}
		} catch (CorruptIndexException e) {
		} catch (LockObtainFailedException e) {
		} catch (IOException e) {
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

	/**
	 * 搜索
	 */
	public void searchStr() {
		Directory directory = null;
		
		try {
			//1、创建Directory
			directory = FSDirectory.open(new File("G:\\testLucene\\index"));
			//2、创建IndexReader
			IndexReader indexReader = IndexReader.open(directory);
			//3、根据IndexReader创建IndexSearcher
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			//4、创建搜索Query
			//4.1 创建QueryParser来确定搜索文件的内容。第二个参数表示搜索的域
			QueryParser queryParser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			//创建Query，表示搜索域中包含置顶字符串的文档
			Query query = queryParser.parse("md5=ec060c49dd939097fcfd98cacbf8e6a2");
			//5、根据searcher搜索，返回TopDocs
			TopDocs topDocs = indexSearcher.search(query, 10);
			//6、根据TopDocs 获取ScoreDoc对象
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc scoreDoc : scoreDocs) {
				//7、根据indexSearcher 和 ScoreDoc 对象，获取具体的Document对象
				Document document = indexSearcher.doc(scoreDoc.doc);
				//8、根据Document获取需要的值
				System.out.print("文件名：" + document.get("fileName"));
				System.out.println("文件路径：" + document.get("path"));
			}
			//9、关闭IndexReader
			indexReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
