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
			// 1������Directory
//			directory = new RAMDirectory();
			directory = FSDirectory.open(new File("G:\\testLucene\\index"));
			
			// 2������IndexWriter
			indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35,
					new StandardAnalyzer(Version.LUCENE_35));
			
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			//3������Document����,�൱�����ݿ��һ����¼
			Document document = null;
			//4��ΪDocument�������Field
			File files = new File("G:\\testLucene\\testDocs");
			for (File file : files.listFiles()) {
				document = new Document();
				//Field�൱��һ����¼�е�һ���ֶ�
				document.add(new Field("content", new FileReader(file)));
				/**
				 * �洢��ѡ��
				 * Field.Store.YES����ʾ��������е�������ȫ�洢���ļ��У�����������ı��Ļ�ԭ
				 * Field.Store.NO����ʾ��������ݲ��洢���ļ��У������Ա���������ʱ�����޷���ȫ��ԭ
				 * 
				 * ����ѡ��
				 * Field.Index.ANALYZED:���зִʺ������������ڱ��⡢���ݵȣ�
				 * Field.Index.NOT_ANALYZED:�������������ǲ����зִʣ������֤�š�������ID�ȣ������ھ�ȷ����
				 * Field.Index.ANALYZED_NOT_NORMS:���зִʣ������洢norms��Ϣ�����norms�а����˴���������ʱ���Ȩֵ��Ϣ��
				 * Field.Index.NOT_ANALYZED_NOT_NORMS:�������зִ�Ҳ���洢norms��Ϣ
				 * Field.Index.NO:����������
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
	 * ����
	 */
	public void searchStr() {
		Directory directory = null;
		
		try {
			//1������Directory
			directory = FSDirectory.open(new File("G:\\testLucene\\index"));
			//2������IndexReader
			IndexReader indexReader = IndexReader.open(directory);
			//3������IndexReader����IndexSearcher
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			//4����������Query
			//4.1 ����QueryParser��ȷ�������ļ������ݡ��ڶ���������ʾ��������
			QueryParser queryParser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			//����Query����ʾ�������а����ö��ַ������ĵ�
			Query query = queryParser.parse("md5=ec060c49dd939097fcfd98cacbf8e6a2");
			//5������searcher����������TopDocs
			TopDocs topDocs = indexSearcher.search(query, 10);
			//6������TopDocs ��ȡScoreDoc����
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc scoreDoc : scoreDocs) {
				//7������indexSearcher �� ScoreDoc ���󣬻�ȡ�����Document����
				Document document = indexSearcher.doc(scoreDoc.doc);
				//8������Document��ȡ��Ҫ��ֵ
				System.out.print("�ļ�����" + document.get("fileName"));
				System.out.println("�ļ�·����" + document.get("path"));
			}
			//9���ر�IndexReader
			indexReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
