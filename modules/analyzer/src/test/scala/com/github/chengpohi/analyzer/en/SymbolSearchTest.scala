package com.github.chengpohi.analyzer.en

import java.nio.file.Paths

import com.github.chengpohi.analyzer.MTTest
import org.apache.lucene.document._
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig, Term}
import org.apache.lucene.search.{IndexSearcher, TermQuery}
import org.apache.lucene.store.FSDirectory

/**
  * Created by chengpohi on 20/02/2017.
  */
class SymbolSearchTest extends MTTest {
  val INDEX_DIRECTORY = "./target/tmp"

  it should "be able to index symbols and search symbols" in {
    val analyzer = new MTEnAnalyzer()
    val directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY))
    val iwc = new IndexWriterConfig(analyzer)
    iwc.setOpenMode(OpenMode.CREATE)
    val writer = new IndexWriter(directory, iwc)

    val text = "c++ and C#, plus plus and sharp"

    val doc = new Document()
    val fieldname = "fieldname"
    doc.add(new TextField(fieldname, text, Field.Store.YES))

    writer.addDocument(doc)
    writer.commit()
    writer.close()

    val reader = DirectoryReader.open(directory)
    val searcher = new IndexSearcher(reader)

    val query = new TermQuery(new Term(fieldname, "c++"))
    val hits = searcher.search(query, 1)

    hits.totalHits should be(1)
    hits.scoreDocs.foreach(i => {
      val doc1 = searcher.doc(i.doc).get(fieldname)
      println(doc1)
    })

    val query2 = new TermQuery(new Term(fieldname, "c#"))
    val hits2 = searcher.search(query2, 1)

    hits2.totalHits should be(1)
  }
}
