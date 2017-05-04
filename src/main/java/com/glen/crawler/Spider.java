package com.glen.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider
{
  private static final int MAX_PAGES_TO_SEARCH = 10000;
  private Set<String> pagesVisited = new HashSet<String>();
  private List<String> pagesToVisit = new LinkedList<String>();
  private File file;
  private FileWriter fw=null;
  private	BufferedWriter bw = null;
	
  public BufferedWriter getBw() {
	return bw;
}

Spider()
  {
	file = new File("C:\\Ford\\OutputFromCrawl.txt");
		
	try {
		if (!file.exists()) {
			file.createNewFile();
		}
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()),"UTF-8"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  /**
   * Our main launching point for the Spider's functionality. Internally it creates spider legs
   * that make an HTTP request and parse the response (the web page).
   * 
   * @param url
   *            - The starting point of the spider
   * @param searchWord
   *            - The word or string that you are searching for
   */
  public void search(String url, String searchWord)
  {
	    SpiderLeg  leg;
	  System.out.println("search called.");
     while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
      {
    	 leg=null;
    	 leg = new SpiderLeg();
          String currentUrl = null;
        
          if(this.pagesToVisit.isEmpty())
          {
              currentUrl = url;
              this.pagesVisited.add(url);
          }
          else
          {
              currentUrl = this.nextUrl();
          }
          leg.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
                                 // SpiderLeg
          boolean success = leg.searchForWord(searchWord);
          for(String s: leg.getReturnPhrases())
          {
        	  try {
        		//  System.out.println(s);
        		 if(s.matches(".*\\d+.*") || s.split(" ").length<=4 || s.split(" ").length>=20 )
        		 {
        			 continue;
        		 }
        		 s=s.replaceAll("`", "");
				bw.write(s.replaceAll("(\\t|\\r?\\n)+", " "));
				bw.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
          }
          if(success)
          {
              System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
            //  break;
          }
          this.pagesToVisit.addAll(leg.getLinks());
      }
    
      try {
		fw.close();
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
  }

  /**
   * Returns the next URL to visit (in the order that they were found). We also do a check to make
   * sure this method doesn't return a URL that has already been visited.
   * 
   * @return
   */
  private String nextUrl()
  {
      String nextUrl;
      do
      {
          nextUrl = this.pagesToVisit.remove(0);
      } while(this.pagesVisited.contains(nextUrl));
      this.pagesVisited.add(nextUrl);
      return nextUrl;
  }
}
