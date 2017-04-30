package com.glen.indexer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author: gxs8916
 * @Date: 28-Aug-2015
 * Description: Indexer program. This creates the actual Lucent Index on a Input File.
 */

public class Indexer {

    private IndexWriter indexWriter ;//= null;
    private Indexer instance;
    
    //public static Logger logger = Logger.getLogger(Indexer.class);
    public static Logger logger = Logger.getLogger(Indexer.class);
    /** Creates a new instance of Indexer */
    public Indexer(String dirName) {
    	 Directory indexDir;
  		try {
  			  indexDir = FSDirectory.open(new File(dirName));
  			  IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, new StandardAnalyzer(Version.LUCENE_47));
  		      config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
  		      indexWriter = new IndexWriter(indexDir, config);

  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			 logger.log(Level.ERROR, "Exception caught."+e.getMessage());
  		}
    }

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        return indexWriter;
   }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
    
    public void rebuildIndexes(String inputFile) throws IOException {
    	  String line = null;
    	
    	  FileReader fr = null;

          try {
              // FileReader reads text files in the default encoding.
               fr = 
                  new FileReader(inputFile);

              // Always wrap FileReader in BufferedReader.
              BufferedReader bufferedReader = 
                  new BufferedReader(fr);

              while((line = bufferedReader.readLine()) != null) {
            	  IndexWriter writer = getIndexWriter(false);
            	  Document doc = new Document();
            	 
            	 if(!line.trim().equals("")){
            	  String[] dataField=line.split(",");
            	  
        		 // doc.add(new StringField ("ipA", ipField[0], Field.Store.YES));
          		doc.add(new StringField ("word",  dataField[0].trim(), Field.Store.YES));
              	doc.add(new StringField("sentences",dataField[1],Field.Store.YES));
            	  writer.addDocument(doc);
            	 }
              }    
              
              // Always close files.
              bufferedReader.close();            
          }
          catch(FileNotFoundException ex) {
        	  ex.printStackTrace(); 
        	  logger.log(Level.ERROR, "Exception caught."+ex.getMessage());
        	  fr.close();          
        	 // closeIndexWriter();
        	  
          }
          catch(IOException ex) {
             ex.printStackTrace();
             logger.log(Level.ERROR, "Exception caught."+ex.getMessage());
             fr.close();          
            // closeIndexWriter();
          }
        
   }
    
 
}
