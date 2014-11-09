package edu.buffalo.hack.image;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class GetLogoUrl {

	public String getUrl(String input) throws FileNotFoundException {
		
		String output = "";
		String url = "https://yambal-easy-amazon-product-advertising.p.mashape.com/getItemSearchURL/?CountryCode=US&Keywords=";
		url += input;

		String key = "nJsLEnpYaZmshVPYtiCXS18wLTR3p1spiyBjsnOGprDURyiUVz";

		// Instantiate an HttpClient
		HttpClient client = new HttpClient();

		// GetMethod
		// Instantiate a GET HTTP method
		GetMethod method = new GetMethod(url);
		method.setRequestHeader("X-Mashape-Key", key);

		int statusCode;
		try {
			statusCode = client.executeMethod(method);

			//System.out.println("Status Code = " + statusCode);
			
			//System.out.println("Status Text>>>"
			//		+ HttpStatus.getStatusText(statusCode));

			// Get data as a String
			//System.out.println(method.getResponseBodyAsString());

			String response = method.getResponseBodyAsString();
			method.releaseConnection();
			if (null != response) {
				String[] responseParts = response.split("\"");
				String token = null;
				output = responseParts[15];	
				output = output.replace("\\", "");
				//System.out.println("url:"+output);
				
			}
			

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
		

	}
}
