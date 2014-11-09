package edu.buffalo.hack.image;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class SaxParser {
	
	//ArrayList<String> output = new ArrayList<String>();	
	int count = 0;
	StringBuilder builder = new StringBuilder();
	List<HashMap<String,String>> listHashMaps = new ArrayList<HashMap<String,String>>();

	 public List<HashMap<String,String>> getLogoDetails(String input) {
		 
		    try {
		 
		    System.out.println("Within Parser input:"+input);
		    	
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			DefaultHandler handler = new DefaultHandler() {
		 
			//boolean bMoreUrl = false;
			boolean bDetailPageURL = false;
			boolean bTitle = false;
			//boolean bsalary = false;
			HashMap<String,String> hm = new HashMap<String,String>();
			public void startElement(String uri, String localName,String qName, 
		                Attributes attributes) throws SAXException {
		 
				//System.out.println("Start Element :" + qName);
		 
//				if (qName.equalsIgnoreCase("MoreSearchResultsUrl")) {
//					bMoreUrl = true;
//				}
		 
				if (qName.equalsIgnoreCase("DetailPageURL")) {
					bDetailPageURL = true;
				}
		 
				if (qName.equalsIgnoreCase("Title")) {
					bTitle = true;
				}
		 
				/*if (qName.equalsIgnoreCase("SALARY")) {
					bsalary = true;
				}*/
		 
			}
		 
			public void endElement(String uri, String localName,
				String qName) throws SAXException {
		 
				/*if(qName.equalsIgnoreCase("MoreSearchResultsUrl")) {
					System.out.println("IN end element:"+builder.toString());
				}*/
				//System.out.println("End Element :" + qName);
		 
			}
		 
			public void characters(char ch[], int start, int length) throws SAXException {
		 
				if(count <5)
				{
//					if (bMoreUrl) {
//						System.out.println("More Url : " + new String(ch, start, length));
//						//builder.append(new String(ch, start, length));
//						String s = new String(ch, start, length);
//						//System.out.println("s old:"+s);
//						//s = s.replace("$amp;", "&amp;");
//						
//						//xml = xml.replace("&amp;", "$amp;");
//						//System.out.println("s new:"+s);
//						output.add(s);
//						bMoreUrl = false;
//						count++;
//					}
			 
					if (bDetailPageURL) {
						System.out.println("Detail : " + new String(ch, start, length));
						//output.add(new String(ch, start, length));
						bDetailPageURL = false;
						hm.put("detailUrl", new String(ch, start, length));
						//count++;
					}
			 
					if (bTitle) {
						System.out.println("Title : " + new String(ch, start, length));
						//output.add(new String(ch, start, length));
						bTitle = false;
						hm.put("title", new String(ch, start, length));
						listHashMaps.add(hm);
						hm = new HashMap<String,String>();
						count++;
					}
			 
					/*if (bsalary) {
						System.out.println("Salary : " + new String(ch, start, length));
						bsalary = false;
					}*/
				}
//				else
//				{
//					throw new SAXException("Object details found !");
//				}
			}
		 
		     };
		 
		       //saxParser.parse("C:/Users/Anirudha/Desktop/test.xml", handler);
		     //saxParser.parse(new File("test.xml"), handler);
		     
		     //saxParser.parse(input, handler);
//		     try {
				saxParser.parse(new InputSource(new StringReader(input)), handler);
//			} catch (SAXException e) {
//				e.printStackTrace();
//			}
		     
		     
//		     for(int i = 0; i < output.size(); i++)
//		    	 System.out.println(i+":"+output.get(i));
//		 
		     } catch (Exception e) {
		       e.printStackTrace();
		     }
			return listHashMaps;
		 
		   }
	
}
