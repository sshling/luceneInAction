package cn.test.ch01;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

// From chapter 1  #ant Searcher

public class Searcher {

    public static void main(String[] args) throws IllegalArgumentException,
            IOException, ParseException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java " + Searcher.class.getName()
                    + " <index dir> <query>");
        }

        String indexDir = args[0];               //1索引文件所在目录
        String q = args[1];                      //2查询字符串

        search(indexDir, q);
    }

    public static void search(String indexDir, String q)
            throws IOException, ParseException {

        Directory dir = FSDirectory.open(new File(indexDir)); //3
        IndexSearcher is = new IndexSearcher(dir);   //3打开Index

        QueryParser parser = new QueryParser(Version.LUCENE_30, // 4
                "contents",  //4 解析查询串
                new StandardAnalyzer(          //4
                        Version.LUCENE_30));  //4
        Query query = parser.parse(q);              //4
        long start = System.currentTimeMillis();
        TopDocs hits = is.search(query, 10); //5
        long end = System.currentTimeMillis();

        System.err.println("Found " + hits.totalHits +   //6
                " document(s) (in " + (end - start) +        // 6
                " milliseconds) that matched query '" +     // 6
                q + "':");                                   // 6

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);               //7得到索引的文档
            System.out.println(doc.get("fullpath"));  //8
        }

        is.close();                                //9
    }
}

/*
#1 Parse provided index directory
#2 Parse provided query string
#3 Open index
#4 Parse query
#5 Search index
#6 Write search stats
#7 Retrieve matching document
#8 Display filename
#9 Close IndexSearcher
核心类：
􀂃 IndexWriter
􀂃 Directory
􀂃 Analyzer
􀂃 Document
􀂃 Field
P27

*/
