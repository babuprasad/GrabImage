package edu.buffalo.hack.image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;

import android.R.string;
import android.util.Log;

public class ObjectRecognition {
	
	private final String TAG = "Runner";


	public ArrayList<ObjectDetails> getObjectDetails(String productName) throws Exception {
		
		String xml = "";
		List<HashMap<String,String>> logoDetails = new ArrayList<HashMap<String,String>>();
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
		//xml = xml.replace("&amp;", "#ABM");
		
		if(xml.contains("AWS.ECommerceService.NoExactMatches"))
		{
			System.out.println("We did not find any matches for your request.");
			return null;
		}
		SaxParser sp = new SaxParser();
		logoDetails = sp.getLogoDetails(xml);
		
		if(!logoDetails.isEmpty() && logoDetails.size() > 0)
        {

                for(int i = 0; i < logoDetails.size(); i++)
                {
                        try {
                                HashMap<String,String> hm = logoDetails.get(i);
                                imageUrl = getLogoDetails.getImage(hm.get("detailUrl"),hm.get("title"));
                                System.out.println("ImageURL:" + imageUrl);
                                String[] s = imageUrl.split("###");
                                if(s.length > 1)
                                {
                                	if(s.length == 1)
                                        hm.put("content", "");
                                	else
                                        hm.put("content", s[1]);
                                	hm.put("image", s[0]);
                                }
                                else
                                {
                                	hm.put("content","");
                                	hm.put("image","");
                                }
                                
                                
                        } catch (Exception e) {
                                e.printStackTrace();
                                logoDetails.remove(i);
                        }
                        
                }
        }
         else
         {
                 return null;
         }

		System.out.println("Final output size:" + logoDetails.size());
		
//		if(logoDetails.size() < 2)
//			return null;
		
//		imageUrl = getLogoDetails.getImage(logoDetails.get(1),logoDetails.get(2));
//		
//		logoDetails.add(imageUrl);
//		
//		String s  = logoDetails.get(0);
//		s = s.replace("#ABM", "&");
//		logoDetails.set(0, s);
//							
//		System.out.println("Final output:");
//		System.out.println("url:"+url);
//		System.out.println("xml:"+xml);
//		System.out.println("imageUrl:"+imageUrl);
//		for(int i = 0; i < logoDetails.size(); i++)
//			System.out.println(i+":"+logoDetails.get(i));
		ArrayList<ObjectDetails> objectDetailsList = new ArrayList<ObjectDetails>();
		
		for (int i = 0; i < logoDetails.size(); i++) {
			
			ObjectDetails objectDetails = new ObjectDetails();
			HashMap<String, String> map = logoDetails.get(i);
			objectDetails.objectTitle = map.get("title");
			objectDetails.objectImage = map.get("image");
			objectDetails.objectUrl = map.get("detailUrl");
			objectDetails.objectContent = map.get("content");
							
			Log.i(TAG, "Title :" + objectDetails.objectTitle);
			Log.i(TAG, "Url : " + objectDetails.objectUrl);
			Log.i(TAG, "Content : "+ objectDetails.objectContent);
			
			objectDetailsList.add(objectDetails);

		}
		
		
//		ObjectDetails object = new ObjectDetails();
//		object.objectTitle = logoDetails.get(2);
//		object.objectUrl = logoDetails.get(1);
//		object.objectImage = logoDetails.get(3);
//		object.relevantUrl = logoDetails.get(0);
//		imageDetails.put("relatedUrl", logoDetails.get(0));
//		imageDetails.put("detailUrl", logoDetails.get(1));
//		imageDetails.put("title", logoDetails.get(2));
//		imageDetails.put("image", logoDetails.get(3));
		
		return objectDetailsList;

	}

}
