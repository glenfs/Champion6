package com.glen.crawler;

import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class POSTag {
	static StanfordCoreNLP pipeline;
	static Properties properties;
	  static POSTag instance;
private POSTag(){}

public static POSTag getInstance()
{
	if(instance==null)
	{
		instance=new POSTag();
		properties = new Properties();
		 properties.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		 pipeline = new StanfordCoreNLP(properties);
	}
	return instance;
}

public StanfordCoreNLP getPipeline() {
		return pipeline;
	}

	public  void setPipeline(StanfordCoreNLP pipeline) {
		POSTag.pipeline = pipeline;
	}
}
