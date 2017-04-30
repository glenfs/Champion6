package com.glen.batch;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class JobCreator {
	
	private  Configuration configuration;
	private int DEFAULTNUMREDUCERS=10;
	private String[] args;

	public JobCreator(String[] args,Configuration configuration) throws IOException, URISyntaxException {
		this.configuration=configuration;
		this.args=args;
	}

	public Configuration getConf(){
		return this.configuration;
	}

	public Job loadJob() throws IOException, URISyntaxException {

		String ignoreCaseJobType =  args[0].toUpperCase().trim();
		
		if(ignoreCaseJobType.equals("SENTENCETOWORD"))
		{
			return  getSentenseToWordJob();
		}
		else
		{
			return  getSentenseToWordJob();
		}
	}
	
	private Job getSentenseToWordJob() throws IOException {
		Configuration config = getConf();
		org.apache.hadoop.mapreduce.Job job = org.apache.hadoop.mapreduce.Job.getInstance(config);
		job.setJobName("TYPAHD-PRDPAYLOAD");

		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		job.setMapperClass(SentenceToWordMapper.class);
		job.setReducerClass(SentenceToWordReducer.class);
		job.setNumReduceTasks(DEFAULTNUMREDUCERS);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		return job;
	}

}
