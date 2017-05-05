package com.glen.crawler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newCachedThreadPool();
    	SpiderRunnable runner1=new SpiderRunnable("https://en.wikiquote.org/wiki/List_of_literary_works","literary.txt");
		executor.execute(runner1);
			SpiderRunnable runner1a=new SpiderRunnable("https://en.wikiquote.org/wiki/List_of_Manga/Anime_shows_in_English","mangaAnime.txt");
		executor.execute(runner1a);
		
		SpiderRunnable runner2=new SpiderRunnable("https://en.wikiquote.org/wiki/List_of_people_by_name","peopleQuotes.txt");
		executor.execute(runner2);
		
		SpiderRunnable runner2a=new SpiderRunnable("https://en.wikiquote.org/wiki/List_of_radio_shows","radioShows.txt");
		executor.execute(runner2a);
		
		SpiderRunnable runner3=new SpiderRunnable("https://en.wikiquote.org/wiki/List_of_films_(A%E2%80%93C)","films.txt");
		executor.execute(runner3);
		SpiderRunnable runner3a=new SpiderRunnable("https://en.wikiquote.org/wiki/List_of_television_shows","tvShows.txt");
		executor.execute(runner3a);
		
		SpiderRunnable runner4=new SpiderRunnable("https://en.wikiquote.org/wiki/List_of_theatrical_plays_and_musicals","playsMusical.txt");
		executor.execute(runner4);
		
		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		//Spider spider = new Spider();
        //spider.search("https://en.wikipedia.org/wiki/Computer", "machine language");
      //spider.search("https://www.nytimes.com", "parenting");
      //  spider.search("https://en.wikipedia.org/wiki/Main_Page", "parenting");
     //   spider.search("https://en.wikiquote.org/wiki/Category:Lists", "parenting");
     //   spider.search("https://en.wikiquote.org/wiki/List_of_films_(A%E2%80%93C)", "parenting");
      //  spider.search("https://en.wikiquote.org/wiki/English_proverbs", "parenting");
     //   spider.search("http://www.oprah.com/omagazine/Oprah-Gets-Interviewed-by-O-Readers", "parenting");
    }
}
