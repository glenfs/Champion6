package com.glen.batch;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SentenceToWordReducer extends
Reducer<Text, Text, Text, Text> 
{
	ArrayList<String> sentencesAdded;
	public void reduce(Text key, Iterable<Text> values, Context context) 
			  throws IOException, InterruptedException 
	{
		sentencesAdded=new ArrayList<String>();
			   String out="";
			   int count=0;
			    for (Text val : values) {
			    	if(!sentencesAdded.contains(val.toString().trim()))
			    	{
			    		sentencesAdded.add(val.toString().trim());
			    	
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
			    }
			    context.write(key, new Text(out));
	}
}