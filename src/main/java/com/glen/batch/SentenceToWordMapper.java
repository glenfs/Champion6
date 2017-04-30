package com.glen.batch;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SentenceToWordMapper  extends Mapper<LongWritable, Text, Text, Text>
{
	private Text outKey;
	
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	{
		String line=value.toString();
		String words[] =line.split(" ");
		outKey=new Text();
		/*
		 * Add the filter logic to discard any conjunction and other non words. using some NLP libs.
		 */
		
		for(String word: words)
		{
			outKey.set(word);
			context.write(outKey, value);
		}
	}
}
