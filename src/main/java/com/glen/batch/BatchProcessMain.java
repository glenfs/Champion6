package com.glen.batch;

import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;


public class BatchProcessMain extends Configured implements Tool 
{
	public static Logger logger = Logger.getLogger(BatchProcessMain.class);
	
	public int run(String[] args) throws Exception 
	{
		Job job=new JobCreator(args,getConf()).loadJob();
		int returnVal= job.waitForCompletion(true) ? 0 : 1;

		return returnVal;
	}
	
	public static void main(String[] args) 
	{
		if(args[0].equalsIgnoreCase("indexer"))
		{
			//Use Indexer Call here.
		}
		else if(args[0].equalsIgnoreCase("SENTENCETOWORD"))
		{
			long startTime = new Date().getTime();
			
			try {
				ToolRunner.run(new Configuration(), new BatchProcessMain(), args);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		long endTime = new Date().getTime();
		logger.info("Elapsed Time:" + (endTime - startTime));
		}
	}

}
