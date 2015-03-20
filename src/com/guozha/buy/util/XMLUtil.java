package com.guozha.buy.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 解析和封装XML的工具类
 * @author PeggyTong
 *
 */
public class XMLUtil {
    
    /**
     * 解析constant.XML
     * @param instream
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static Map<String, Map<String, String>> getConfigXml(InputStream instream) 
    		throws XmlPullParserException, IOException{
    	Map<String, Map<String, String>> constantMap = null;
    	XmlPullParser parser = Xml.newPullParser();
    	parser.setInput(instream, "UTF-8");
    	int eventType = parser.getEventType();
    	String constantKey = null;
    	Map<String, String> constantValue = null;
    	while(eventType != XmlPullParser.END_DOCUMENT){
    		switch (eventType) {
			case XmlPullParser.START_DOCUMENT: //如果是文档开始事件
				constantMap = new HashMap<String, Map<String,String>>();
				break;
			case XmlPullParser.START_TAG:   	//如果遇到标签开始
				if("constant".equals(parser.getName())){
				   constantValue = new HashMap<String, String>();
				   constantKey = parser.getAttributeValue(0);
				}
				
				if("constantName".equals(parser.getName())){
					if(constantValue != null){
						constantValue.put(parser.getAttributeValue(0), parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:         //判断当前事件是否是标签元素结束事件
				if("constant".equals(parser.getName())){
					
					if(constantKey != null){
						constantMap.put(constantKey, constantValue);
						constantKey = null;
						constantValue = null;
					}
				}
				break;
			}
    		eventType = parser.next();
    	}
    	return constantMap;
    }
}
