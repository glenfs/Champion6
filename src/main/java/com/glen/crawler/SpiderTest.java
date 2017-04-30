package com.glen.crawler;

public class SpiderTest
{
    /**
     * This is our test. It creates a spider (which creates spider legs) and crawls the web.
     * 
     * @param args
     *            - not used
     */
    public static void main(String[] args)
    {
        Spider spider = new Spider();
        //spider.search("https://en.wikipedia.org/wiki/Computer", "machine language");
      //spider.search("https://www.nytimes.com", "parenting");
      //  spider.search("https://en.wikipedia.org/wiki/Main_Page", "parenting");
        //spider.search("https://en.wikiquote.org/wiki/Category:Lists", "parenting");
       // spider.search("https://en.wikiquote.org/wiki/List_of_films_(A%E2%80%93C)", "parenting");
      //  spider.search("https://en.wikiquote.org/wiki/English_proverbs", "parenting");
        spider.search("http://www.oprah.com/omagazine/Oprah-Gets-Interviewed-by-O-Readers", "parenting");
    }
}
