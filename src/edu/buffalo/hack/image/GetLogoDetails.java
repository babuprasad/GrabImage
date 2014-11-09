package edu.buffalo.hack.image;

import java.io.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class GetLogoDetails {
	
	@SuppressWarnings({ "resource", "deprecation" })
	public String getXML(String input) throws Exception {
	
		HttpClient client = new DefaultHttpClient();
		//HttpGet request = new HttpGet("http://www.amazon.com/Apple-MacBook-MGX72LL-13-3-Inch-Display/dp/B0096VDM8G/ref=sr_1_2?ie=UTF8&qid=1415430331&sr=8-2&keywords=apple+laptop");
		HttpGet request = new HttpGet(input);
		HttpResponse response = client.execute(request);

		// Get the response
		BufferedReader rd = new BufferedReader
		  (new InputStreamReader(response.getEntity().getContent()));
		    
		String line = "";
		line = rd.readLine();
		//line+=rd.readLine();
		//System.out.println(line);
		return line;
			
	}
	
	@SuppressWarnings({ "resource", "deprecation" })
	public String getImage(String inputUrl,String title) throws Exception {
		
		HttpClient client = new DefaultHttpClient();
		//HttpGet request = new HttpGet("http://www.amazon.com/Apple-MacBook-MGX72LL-13-3-Inch-Display/dp/B0096VDM8G/ref=sr_1_2?ie=UTF8&qid=1415430331&sr=8-2&keywords=apple+laptop");
		HttpGet request = new HttpGet(inputUrl);
		HttpResponse response = client.execute(request);

		// Get the response
		BufferedReader rd = new BufferedReader
		  (new InputStreamReader(response.getEntity().getContent()));
		    
		boolean breakFlag = false;
		String line1 = "";
		String line2  = "";
		String line = "";
		String image = "";
		String check = "<img alt=";//Red Bull Energy Drink, 8.4 oz Cans - 4 Count (Pack of 6)
		//System.out.println("check:"+check);
		//System.out.println("input:"+title);
		while ((line = rd.readLine()) != null) {
		 if(line.contains(check))
		 {
			 //System.out.println("Success:"+line);
			 if(line.contains(title)){
				 //System.out.println("Super success.");
				 //break;
				 
				 int start = line.indexOf("src=");
				 int end = line.indexOf("_.jpg");
			     line1 = line.substring(start+5, end+5);
			     if(breakFlag)
			    	 break;
			     else
			    	 breakFlag = true;
			 }
				 
		 }
		 else
		 {
			 if(line.contains("<li><span class=\"a-list-item\">") && line.contains("</span></li>"))
			 {
				 line2 = line.substring(36, line.length()-12);
				 if(breakFlag)
					 break;
				 else
					 breakFlag = true;
			 }
			 
				 
		 }
		}
		
		rd.close();
		
//		int start = line.indexOf("src=");
//		int end = line.indexOf("_.jpg");
		//System.out.println("start:"+start);
		//System.out.println("end:"+end);
		return(line1 + "###"+line2);
		
		
			
	}

}
