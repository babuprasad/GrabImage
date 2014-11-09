package edu.buffalo.hack.image;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.string;
import android.util.Log;

public class ObjectRecognition {
	
	private final String TAG = "Runner";


	public ObjectDetails getObjectDetails(String productName) throws Exception {
		
		String xml = "";
		ArrayList<String> logoDetails = new ArrayList<String>();
		HashMap<String,String> imageDetails = new HashMap<String,String>();
		String imageUrl = "";
		String url = "";
		GetLogoUrl getLogoUrl = new GetLogoUrl();
		productName = productName.replaceAll(" ", "\\+");
		Log.i(TAG,"Repalced product name :" + productName);
		url = getLogoUrl.getUrl(productName);
		
		System.out.println("url:"+url);
		
		//String url = "http://ecs.amazonaws.com/onca/xml?AWSAccessKeyId=1ST3PS0HZ31ET7X9D4G2&AssociateTag=yambal058-22&Keywords=red%20bull&Operation=ItemSearch&SearchIndex=All&Service=AWSECommerceService&Timestamp=2014-11-08T13%3A39%3A58Z&Signature=WJh7lZMRxs9xyvpytgViVbgHEMAg73GyU1eSGegroNY%3D";
		
		GetLogoDetails getLogoDetails = new GetLogoDetails();
		xml = getLogoDetails.getXML(url);
		System.out.println("xml:"+xml);
		xml = xml.replace("&amp;", "#ABM");
		
		
		SaxParser sp = new SaxParser();
		logoDetails = sp.getLogoDetails(xml);

		if(logoDetails.size() < 2)
			return null;
		
		imageUrl = getLogoDetails.getImage(logoDetails.get(1),logoDetails.get(2));
		
		logoDetails.add(imageUrl);
		
		String s  = logoDetails.get(0);
		s = s.replace("#ABM", "&");
		logoDetails.set(0, s);
							
		System.out.println("Final output:");
		System.out.println("url:"+url);
		System.out.println("xml:"+xml);
		System.out.println("imageUrl:"+imageUrl);
//		for(int i = 0; i < logoDetails.size(); i++)
//			System.out.println(i+":"+logoDetails.get(i));
		
		ObjectDetails object = new ObjectDetails();
		object.objectTitle = logoDetails.get(2);
		object.objectUrl = logoDetails.get(1);
		object.objectImage = logoDetails.get(3);
		object.relevantUrl = logoDetails.get(0);
//		imageDetails.put("relatedUrl", logoDetails.get(0));
//		imageDetails.put("detailUrl", logoDetails.get(1));
//		imageDetails.put("title", logoDetails.get(2));
//		imageDetails.put("image", logoDetails.get(3));
		
		return object;

	}

}
