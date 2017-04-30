package com.glen.indexer;

import java.io.IOException;

public class IndexerRunnable implements Runnable
{
	String name;
	Indexer indexBuilder;
	String filePath;
	
	public IndexerRunnable(Indexer builder, String path)
	{
		indexBuilder=builder;
		filePath=path;
		name=path;
	}

	@Override
	public void run() {
		try {
			indexBuilder.rebuildIndexes(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
