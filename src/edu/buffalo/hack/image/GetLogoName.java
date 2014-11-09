package edu.buffalo.hack.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class GetLogoName {

	//public String getLogoName(String path) throws FileNotFoundException{
	public static void main(String args[]) throws FileNotFoundException{

		
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
		                new FilePart("image_request[image]", new File("apple.jpe"))
		        };
		        
		        method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

		        try{
		            int statusCode = client.executeMethod(method);

		            System.out.println("Status Code = "+statusCode);
		           // System.out.println("QueryString>>> "+method.getQueryString());
		            System.out.println("Status Text>>>"
		                  +HttpStatus.getStatusText(statusCode));

		            //Get data as a String
		            System.out.println(method.getResponseBodyAsString());

		            String response = method.getResponseBodyAsString();
		            method.releaseConnection();
		           if(null != response){
		        	   String[] responseParts = response.split("\"");
		        	   String token = null;
		        	   if(responseParts.length > 4)
		        		   token = responseParts[3];
		        	   
		        	   //HttpClient client1 = new HttpClient();
		        	   StringBuilder sb = new StringBuilder();
		        	   
		        	   String url1 = "https://camfind.p.mashape.com/image_responses/";
		        	   sb.append(url1);
		        	   sb.append(token);
		        	   
		        	   HttpMethod getMethod = new GetMethod(sb.toString());
		        	   getMethod.setRequestHeader("X-Mashape-Key", "kAxxLceQK9mshrd8CkeY27BjhV4xp1n1b75jsnFKLOny32qPyf");
		        	   
		        	   int statusCoderesp = client.executeMethod(getMethod);
			            System.out.println("Status Code = "+statusCoderesp);
			            //System.out.println("QueryString>>> "+getMethod.getQueryString());
			            System.out.println("Status Text>>>"
			                  +HttpStatus.getStatusText(statusCoderesp));

			            //Get data as a String
			            System.out.println(getMethod.getResponseBodyAsString());
			            String finalResp = getMethod.getResponseBodyAsString();
			            
			            if(null != finalResp){
			            	String[] finalRespParts = finalResp.split("\"");
			            	if(finalRespParts.length > 7){
			            		System.out.println(finalRespParts[7]);
			            	}
			            }
			            getMethod.releaseConnection();
		           }

		            //release connection
		            
		        }
		        catch(IOException e) {
		            e.printStackTrace();
		        }
		        
		        //return null;
		    }
		
	}


