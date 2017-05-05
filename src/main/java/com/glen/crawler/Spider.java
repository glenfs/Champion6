package com.glen.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.TokenSequenceMatcher;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Spider
{
  private static final int MAX_PAGES_TO_SEARCH = 10000;
  private Set<String> pagesVisited = new HashSet<String>();
  private List<String> pagesToVisit = new LinkedList<String>();
  private File file;
  private FileWriter fw=null;
  private	BufferedWriter bw = null;
  private String fileName;
  String regex;
  StanfordCoreNLP pipeline;
  public BufferedWriter getBw() {
	return bw;
}

Spider()
  {
	pipeline = POSTag.getInstance().getPipeline();
	file = new File("C:\\Ford\\OutputFromCrawl.txt");
	regex = "([{pos:/VB|JJ|JJR|JJS/}])";
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

	Spider(String fName)
	{
		fileName=fName;
		pipeline = POSTag.getInstance().getPipeline();
		file = new File(fileName);
		regex = "([{pos:/VB|JJ|JJR|JJS/}])";
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
          String input;
          for(String s: leg.getReturnPhrases())
          {
        	  try {
          		//  System.out.println(s);
          		 if(s.matches(".*\\d+.*") || s.split(" ").length<=4 || s.split(" ").length>=20 ||s.contains("-LRB-") ||s.contains("-RRB-")
          				 ||s.contains("Creative Commons Attribution") ||s.contains("wiki"))
          		 {
          			 continue;
          		 }
          		 s=s.replaceAll("`", "");
        	  //  System.out.println(s);
        		input=s;
   	    	 	edu.stanford.nlp.pipeline.Annotation annotation = pipeline.process(input);
   	    	 	List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
   	    	 	List<String> output = new ArrayList<>();
   	    	 	String regex = "([{pos:/VB|JJ|JJR|JJS/}])"; //Noun
   	    	 	for (CoreMap sentence : sentences) {
   	    	 		List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
   	    	 		TokenSequencePattern pattern = TokenSequencePattern.compile(regex);
   	    	 		TokenSequenceMatcher matcher = pattern.getMatcher(tokens);
   	    	 		while (matcher.find()) {
   	    	 			output.add(matcher.group());
   	    	 		}
   	    	 	}
        	  if(output.size()==0){continue;}
        	  //System.out.println(output);
        		
				bw.write(s.replaceAll("(\\t|\\r?\\n)+", " "));
				bw.newLine();
			} catch (IOException e) {
				System.out.println("Exception String:"+s);
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
