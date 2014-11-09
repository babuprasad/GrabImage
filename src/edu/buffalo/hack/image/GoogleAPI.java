package edu.buffalo.hack.image;
//package hack.image;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//public class GoogleAPI {
//	
//	public static void main(String args[]){
//		try{
//		HttpClient client = new DefaultHttpClient();
//	      String url="https://www.google.co.in/searchbyimage/upload";
//	      //String imageFile="c:\\temp\\shirt.jpg";
//	      String imageFile = "C:/Users/Anirudha/Documents/Masters/Hackathon/test.jpg";
//	      HttpPost post = new HttpPost(url);
//
//	      MultipartEntity entity = new MultipartEntity();
//	      entity.addPart("encoded_image", new FileBody(new File(imageFile)));
//	      entity.addPart("image_url",new StringBody(""));
//	      entity.addPart("image_content",new StringBody(""));
//	      entity.addPart("filename",new StringBody(""));
//	      entity.addPart("h1",new StringBody("en"));
//	      entity.addPart("bih",new StringBody("179"));
//	      entity.addPart("biw",new StringBody("1600"));
//
//	      post.setEntity(entity);
//	      HttpResponse response = client.execute(post);
//	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));         
//
//	      String line = "";
//	      while ((line = rd.readLine()) != null) {
////	        if (line.indexOf("HREF")>0)
////	      System.out.println(line.substring(8));
////	        else
//	        	System.out.println(line);
//	      }
//
//	    }catch (ClientProtocolException cpx){
//	      cpx.printStackTrace();
//	    }catch (IOException ioex){
//	      ioex.printStackTrace();
//	    }
//		
//	}
//	
//
//}
