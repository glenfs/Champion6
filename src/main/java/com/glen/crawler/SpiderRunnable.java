package com.glen.crawler;


public class SpiderRunnable implements Runnable{
	String filePath;
	 Spider spider;
	 
	SpiderRunnable(String f, String outFile)
	{
		filePath=f;
		spider=new Spider(outFile);
	}
	
	@Override
	public void run() {
			spider.search(filePath, "xyz");
	}

}
