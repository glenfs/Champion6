package com.glen.indexer;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;

/**
 * @author: Glen Soans.
 * @Date: 28-APR-2017
 * Description: This program builds Lucence index 
 */

public class GenericIndexer {
	public static Logger logger = Logger.getLogger(GenericIndexer.class);
	
    
    /**
     * @param args the command line arguments
     * Args order: 1. <Path><Filename>
     */
    public static void runIndexer(String inputPath, String outIndexPath) {
     String indexpath = outIndexPath;
      try {
        	Indexer  indexer;
        	indexer = new Indexer(indexpath);
        	//indexer.rebuildIndexes(inputPath,true);
        	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
    				.newCachedThreadPool();
    		File dir = new File(inputPath);
    		
    		File[] directoryListing = dir.listFiles();
    			if (directoryListing != null) {
    			for (File child : directoryListing) {
    				IndexerRunnable indexRunner = new IndexerRunnable(
    						indexer, child.toString());
    				executor.execute(indexRunner);
    			}
    		}
    		executor.shutdown();
    		executor.awaitTermination(3, TimeUnit.HOURS);
    		indexer.closeIndexWriter();
      	} catch (Exception e) {
    	  e.printStackTrace();
        logger.log(Level.ERROR, "Exception caught."+e.getMessage());
      }
      
    }//end of the method runIndexer.
    
    public static void main(String[] args)
    {
    	System.out.println("Starting the process");
    	String inputPath=args[0];
    	String outputPath=args[1];
    	System.out.println(inputPath);
    	System.out.println(outputPath);
    	
    	runIndexer(inputPath,outputPath);
    }
}
