package edu.buffalo.grabimage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;


import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

public class ProcessImage {
	final String TAG = "ProcessImage";


	public String getImageToken(String path) throws FileNotFoundException{
		// TODO Auto-generated method stub


		String url =
				"https://camfind.p.mashape.com/image_requests";


		//Instantiate an HttpClient
		HttpClient client = new HttpClient();

		//GetMethod
		//Instantiate a GET HTTP method
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("X-Mashape-Key", "kAxxLceQK9mshrd8CkeY27BjhV4xp1n1b75jsnFKLOny32qPyf");

		//Define name-value pairs to set into the QueryString
		//FileRequestEntity fre = new FileRequestEntity(new File("F:/Hackathon/Images/aaa.png"), "text/plain");
		//post.setRequestEntity(fre);

		// method.setRequestEntity(fre);
		//NameValuePair nvp2= new NameValuePair("image_request[locale]", "en_US");
		//NameValuePair nvp3= new NameValuePair("email","email@email.com");

		// method.setQueryString(new NameValuePair[]{nvp2});

		Part[] parts = {
				new StringPart("image_request[locale]", "en_US"), 
				new FilePart("image_request[image]", new File(path)) 
		};


		method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

		try{
			int statusCode = client.executeMethod(method);

			Log.i(TAG, "Status Code = "+statusCode);
			// Log.i(TAG, "QueryString>>> "+method.getQueryString());
			Log.i(TAG, "Status Text>>>"
					+HttpStatus.getStatusText(statusCode));

			//Get data as a String
			Log.i(TAG, method.getResponseBodyAsString());

			String response = method.getResponseBodyAsString();
			method.releaseConnection();
			if(null != response){
				String[] responseParts = response.split("\"");
				String token = null;
				if(responseParts.length > 4)
					token = responseParts[3];

				//HttpClient client1 = new HttpClient();

				String url1 = "https://camfind.p.mashape.com/image_responses/";
				//sb.append(url1);
				//sb.append(token);
				String finalUrl =  url1 + token;

				return finalUrl;
			}

			//release connection

		}
		catch(IOException e) {
			e.printStackTrace();
		} 
		return null;
	}


	public String processImage(String finalUrl)
	{
		if (null != finalUrl) 
		{
			try {
				//Instantiate an HttpClient
				HttpClient client = new HttpClient();

				Log.i(TAG, "Url : "+ finalUrl);
				Log.i(TAG, "encoded : " + finalUrl);
				HttpMethod getMethod = new GetMethod(finalUrl);
				getMethod.setRequestHeader("X-Mashape-Key", "kAxxLceQK9mshrd8CkeY27BjhV4xp1n1b75jsnFKLOny32qPyf");

				int statusCoderesp = client.executeMethod(getMethod);
				Log.i(TAG,"Status Code = "+statusCoderesp);
				//Log.i(TAG, "QueryString>>> "+getMethod.getQueryString());
				Log.i(TAG, "Status Text>>>"
						+HttpStatus.getStatusText(statusCoderesp));

				//Get data as a String
				Log.i(TAG, getMethod.getResponseBodyAsString());
				String finalResp = getMethod.getResponseBodyAsString();

				if(null != finalResp){
					String[] finalRespParts = finalResp.split("\"");
					if(finalRespParts.length > 7){
						return finalRespParts[7];
					}
				}
				getMethod.releaseConnection();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
