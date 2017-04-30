package com.glen.batch;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SentenceToWordReducer extends
Reducer<Text, Text, Text, Text> 
{
	public void reduce(Text key, Iterable<Text> values, Context context) 
			  throws IOException, InterruptedException 
	{
			   String out="";
			   int count=0;
			    for (Text val : values) {
			    	if(out.equals(""))
			    	{
			    		out=val.toString();
			    		count++;
			    	}
			    	else{
			    		out=out+"|"+val;
			    		count++;
			    	}
			    	if(count>=10)
			    	{
			    		break;
			    	}
			    }
			    context.write(key, new Text(out));
	}
}