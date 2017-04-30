package com.glen.indexer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Util {

	public static String getIndexDir()
	{
	
	 String fileName = "SearchEngine.properties";

     // This will reference one line at a time
     String line = null;
     String idxDir = null;
     
		   try {
	            // FileReader reads text files in the default encoding.
	            FileReader fileReader = 
	                new FileReader(fileName);

	            // Always wrap FileReader in BufferedReader.
	            BufferedReader bufferedReader = 
	                new BufferedReader(fileReader);

	            while((line = bufferedReader.readLine()) != null) {
	                //System.out.println(line);
	            	idxDir=line;
	               // setSearchParser(line);
	            }   

	            // Always close files.
	            bufferedReader.close();     
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                fileName + "'");   
	            ex.printStackTrace();
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error reading file '" 
	                + fileName + "'"); 
	            ex.printStackTrace();
	        }
		return idxDir;
	}
}
