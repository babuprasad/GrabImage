package edu.buffalo.hack.image;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class TestResp {

	public static void main(String[] args) throws HttpException, IOException{
		
		HttpClient client = new HttpClient();
		 HttpMethod getMethod = new GetMethod("https://camfind.p.mashape.com/image_responses/"+"EoojPj774p0WqXhjI-Ns5g");
  	   getMethod.setRequestHeader("X-Mashape-Key", "kAxxLceQK9mshrd8CkeY27BjhV4xp1n1b75jsnFKLOny32qPyf");
  	   
  	   int statusCoderesp = client.executeMethod(getMethod);
          System.out.println("Status Code = "+statusCoderesp);
          //System.out.println("QueryString>>> "+getMethod.getQueryString());
          System.out.println("Status Text>>>"
                +HttpStatus.getStatusText(statusCoderesp));

          //Get data as a String
          System.out.println(getMethod.getResponseBodyAsString());
	}
}
