package com.glen.crawler;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class SpiderLeg
{
	
	 public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
	        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        });
	 
	        SSLContext context = SSLContext.getInstance("TLS");
	        context.init(null, new X509TrustManager[]{new X509TrustManager() {
	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }
	 
	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }
	 
	            public X509Certificate[] getAcceptedIssuers() {
	                return new X509Certificate[0];
	            }
	        }}, new SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	    }
	 
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;
    private List<String> returnPhrases=null;
    public List<String> getReturnPhrases() {
		return returnPhrases;
	}


	/**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     * 
     * @param url
     *            - The URL to visit
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url)
    {
    	System.out.println("Crawl Called");
        try
        {
        	try {
				enableSSLSocket();
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            		
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
           // System.out.println("htmlDocument set-"+htmlDocument.title());
            if(connection.response().statusCode() == 200) // 200 is the HTTP OK status code
                                                          // indicating that everything is great.
            {
                //System.out.println("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html"))
            {
               // System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
           // System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage)
            {
            	//System.out.println(link);
                this.links.add(link.absUrl("href"));
            }
            return true;
        }
        catch(IOException ioe)
        {
            // We were not successful in our HTTP request
            return false;
        }
    }


    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     * 
     * @param searchWord
     *            - The word or string to look for
     * @return whether or not the word was found
     */
    public boolean searchForWord(String searchWord)
    {
    	 System.out.println("this.links.size()->"+this.links.size());
    	 returnPhrases= new ArrayList<String>();
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null)
        {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
       // System.out.println("Searching for the word " + searchWord + "...");
        String bodyText2 = this.htmlDocument.body().text();
      //  String bodyText = this.htmlDocument.body().getElementsByAttributeValueContaining("class","story-body-text story-content").text();
        String bodyText="";
        try{
       //bodyText = this.htmlDocument.body().getElementById("mw-content-text").text(); //.getElementsByAttributeValueContaining("div","story-body-text story-content").text();mw-content-text
        //bodyText = this.htmlDocument.body().getElementsByTag("LI").text();
        	bodyText = this.htmlDocument.body().getElementsByTag("DIV").text();
   
        }catch (Exception e)
        {e.printStackTrace();}
        
        //System.out.println("Follow bodyText.");
       
      //  System.out.println("CLASS="+ this.htmlDocument.body().getElementsByAttribute("class").val());
       // System.out.println(this.htmlDocument.body().getElementsByAttributeValueContaining("class","story-body-text story-content"));
     // if( this.htmlDocument.body().getElementsByAttribute("p").attr("class").equals("story-body-text story-content"))
    //  {
    	 // System.out.println( this.htmlDocument.body().getElementsByAttribute("p").attr("class"));
    	 //System.out.println("Matches body text...");
     // }
      
      
      //String paragraph = "My 1st sentence. \"Does it work for questions?\" My third sentence.";
      String paragraph = bodyText;
      Reader reader = new StringReader(paragraph);
      DocumentPreprocessor dp = new DocumentPreprocessor(reader);
      List<String> sentenceList = new ArrayList<String>();

      for (List<HasWord> sentence : dp) {
         // SentenceUtils not Sentence
         String sentenceString =  Sentence.listToString(sentence);
         sentenceList.add(sentenceString);
      }

      int count=0;
      for (String sentence : sentenceList) {
    	  count++;
         System.out.println(sentence);
         returnPhrases.add(sentence);
         if(count>500){break;}
      }
     
   /*   String [] sentences= bodyText.split("\\.");
      int count=0;
     for(String s : sentences)
      {
    	  if(count>500){break;}
    	  returnPhrases.add(s);
    	  //System.out.println(s);
    	  count++;
      }*/
        
        Elements links = htmlDocument.select("a[href]");
        Elements media = htmlDocument.select("[src]");
        Elements imports = htmlDocument.select("link[href]");
        
        for (Element link : links) {
          //  System.out.println(link.attr("abs:href")+" "+link.text());
        }
       
       count=1; 
        /*for (Element src : media) {
            if (src.tagName().equals("img")){
            	 System.out.println( src.tagName()+","+ src.attr("abs:src")+","+src.attr("width")+","+ src.attr("height")+","+
                         src.attr("alt"));
          //  storeImageIntoFS(src.attr("abs:src"),count+".jpg","/storedImages/");
            count++;
            }
            else{
            System.out.println( src.tagName()+","+ src.attr("abs:src"));}
        }*/
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }


    public List<String> getLinks()
    {
        return this.links;
    }
    
    public static String storeImageIntoFS(String imageUrl, String fileName, String relativePath) {
        String imagePath = null;
        try {
            byte[] bytes = Jsoup.connect(imageUrl).ignoreContentType(true).execute().bodyAsBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            String rootTargetDirectory = "c://Ford" + "/"+relativePath;
            imagePath = rootTargetDirectory + "/"+fileName;
            saveByteBufferImage(buffer, rootTargetDirectory, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public static void saveByteBufferImage(ByteBuffer imageDataBytes, String rootTargetDirectory, String savedFileName) {
       String uploadInputFile = rootTargetDirectory + "/"+savedFileName;

       File rootTargetDir = new File(rootTargetDirectory);
       if (!rootTargetDir.exists()) {
           boolean created = rootTargetDir.mkdirs();
           if (!created) {
               System.out.println("Error while creating directory for location- "+rootTargetDirectory);
           }
       }
       String[] fileNameParts = savedFileName.split("\\.");
       String format = fileNameParts[fileNameParts.length-1];
     //  System.out.println(format);
       File file = new File(uploadInputFile);
       BufferedImage bufferedImage;

       InputStream in = new ByteArrayInputStream(imageDataBytes.array());
       try {
           bufferedImage = ImageIO.read(in);
           ImageIO.write(bufferedImage, format, file);
       } catch (IOException e) {
           e.printStackTrace();
       }
       catch (Exception e1) {
           e1.printStackTrace();
       }
       finally
       {
    	   try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
       }
    }

}