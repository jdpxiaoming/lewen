package com.poe.lewen.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import com.poe.lewen.bean.channel;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.bean.rsp_login;
import android.util.Xml;

public class XmlToListService {
	
	public static rsp_login GetLogin(String str)throws Exception{
		if(str==null||"".equals(str))
			return null;
		rsp_login rsp = null;
		XmlPullParser parser = Xml.newPullParser();
		InputStream  inputStream   =   new   ByteArrayInputStream(str.getBytes());
		parser.setInput(inputStream, "utf-8");
		int eventType = parser.getEventType();
		while(eventType!=XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				rsp = new rsp_login();
				break;
			case XmlPullParser.START_TAG:
				String tag = parser.getName();
				if(rsp!=null){
					if ("type".equals(tag)) {
						rsp.setType(parser.nextText());
					} else if ("cmd".equals(tag)) {
						rsp.setCmd(parser.nextText());
					} else if ("userId".equals(tag)) {
						rsp.setUserId(parser.nextText());
					} else if ("roleId".equals(tag)) {
						rsp.setRoleId(parser.nextText());
					} else if ("userLoginRet".equals(tag)) {
						rsp.setUserLoginRet(parser.nextText());
					} else if ("ierrorCode".equals(tag)) {
						rsp.setIerrorCode(parser.nextText());
					} else if ("menuShow".equals(tag)) {
						rsp.setMenuShow(parser.nextText());
					} else if ("err".equals(tag)) {
						rsp.setErr(parser.nextText());
					} 
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		return rsp;
	}
	
	public static List<channel> GetChannelList(String str)throws Exception{
		if(str==null||"".equals(str))
			return null;
		List<channel> news = null;
		channel newInfo = null;
		XmlPullParser parser = Xml.newPullParser();
		InputStream  inputStream   =   new   ByteArrayInputStream(str.getBytes());
		parser.setInput(inputStream, "utf-8");
		int eventType = parser.getEventType();
		while(eventType!=XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				news = new ArrayList<channel>();
				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if("node".equals(name)){
					newInfo = new channel();
				}
				if(newInfo!=null){
					
					if("id".equals(name))
						newInfo.setId(parser.nextText());
					if("name".equals(name)){
						newInfo.setName(newInfo.getName()!=null?newInfo.getName()+parser.nextText():parser.nextText());
					}
					if("parent".equals(name)){
						newInfo.setParent_id(parser.nextText());
					}
//					if("channelId".equals(name)){
//						newInfo.setChannel_id(parser.nextText());
//					}
					
				}
				break;
			case XmlPullParser.END_TAG:
				if("node".equals(parser.getName())){
					news.add(newInfo);
					newInfo = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return news;
	}
	
	/**
	 * 获取指定点的播放地址
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static channelOnLine GetVideoAddress(String str)throws Exception{
		if(str==null||"".equals(str))
			return null;
		List<channelOnLine> lists=new ArrayList<channelOnLine>();
		channelOnLine talkpic = null;
		XmlPullParser parser = Xml.newPullParser();
		InputStream  inputStream   =   new   ByteArrayInputStream(str.getBytes());
		parser.setInput(inputStream, "utf-8");
		int eventType = parser.getEventType();
		while(eventType!=XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if("channel".equals(name)){
					talkpic = new channelOnLine();
				}
				if(talkpic!=null){
					if("device_name".equals(name))
						talkpic.setDevice_name(parser.nextText());
					if("device_type".equals(name)){
						talkpic.setDevice_type(parser.nextText());
					}
					if ("userName".equals(name)) {
						talkpic.setUserName(parser.nextText());
					}
					if("userPsw".equals(name)){
						talkpic.setUserPsw(parser.nextText());
					}
					if("device_ipAddr".equals(name)){
						talkpic.setDevice_ipAddr(parser.nextText());
					}
					if("player_Addr".equals(name)){
						talkpic.setPlayer_Addr(parser.nextText());
					}
					if("device_portNo".equals(name)){
						talkpic.setDevice_portNo(parser.nextText());
					}
					if("channelNo".equals(name)){
						talkpic.setChannelNo(parser.nextText());
					}
					if("channelName".equals(name)){
						talkpic.setChannelName(parser.nextText());
					}
					if("playerAddrType".equals(name)){
						talkpic.setPlayerAddrType(parser.nextText());
					}
					if("device_id".equals(name)){
						talkpic.setDevice_id(parser.nextText());
					}
					if("channelId".equals(name)){
						talkpic.setChannelId(parser.nextText());
					}
					
				}
				break;
			case XmlPullParser.END_TAG:
				if("channel".equals(parser.getName())){
//					talkpic = new channelOnLine();
					lists.add(talkpic);
				}
				break;
			}
			eventType = parser.next();
		}
		
		return lists.get(0);
	}
	
//	
//	public static List<Announcement> GetAnn(String str)throws Exception{
//		if(str==null||"".equals(str))
//			return null;
//		List<Announcement> anns = null;
//		Announcement ann = null;
//		XmlPullParser parser = Xml.newPullParser();
//		InputStream  inputStream   =   new   ByteArrayInputStream(str.getBytes());
//		parser.setInput(inputStream, "utf-8");
//		int eventType = parser.getEventType();
//		while(eventType!=XmlPullParser.END_DOCUMENT){
//			switch (eventType) {
//			case XmlPullParser.START_DOCUMENT:
//				anns = new ArrayList<Announcement>();
//				break;
//			case XmlPullParser.START_TAG:
//				String name = parser.getName();
//				if("article".equals(name)){
//					ann = new Announcement();
//				}
//				if(ann!=null){
//					if("id".equals(name))
//						ann.setId(Integer.parseInt(parser.nextText()));
//					if("title".equals(name)){
//						ann.setTitle(parser.nextText());
//					}
//					if("content".equals(name)){
//						ann.setContent(parser.nextText());
//					}
//					if("pub_date".equals(name)){
//						ann.setDatetime(parser.nextText());
//					}
//					if("imgUris".equals(name)){
//						ann.setImgurls(parser.nextText());
//						if(!"".equals(ann.getImgurls())){
//							String string[];
//							int i = 0;
//					        StringTokenizer tokenizer = new StringTokenizer(ann.getImgurls(), "|");
//					        string = new String[tokenizer.countTokens()];
//					         while (tokenizer.hasMoreTokens()) {
//					            string[i] = new String();
//					            string[i] = tokenizer.nextToken();
//					            i++;
//					        }
//					         ann.setImgarr(string);
//						}
//					}
//					if("url".equals(name)){
//						ann.setUrl(parser.nextText());
//					}
//				}
//				break;
//			case XmlPullParser.END_TAG:
//				if("article".equals(parser.getName())){
//					anns.add(ann);
//					ann = null;
//				}
//				break;
//			}
//			eventType = parser.next();
//		}
//		return anns;
//	}
//	
//	public static List<HuoDong> GetAc(String str)throws Exception{
//		if(str==null||"".equals(str))
//			return null;
//		List<HuoDong> huodongs = null;
//		HuoDong huodong = null;
//		XmlPullParser parser = Xml.newPullParser();
//		InputStream  inputStream   =   new   ByteArrayInputStream(str.getBytes());
//		parser.setInput(inputStream, "utf-8");
//		int eventType = parser.getEventType();
//		while(eventType!=XmlPullParser.END_DOCUMENT){
//			switch (eventType) {
//			case XmlPullParser.START_DOCUMENT:
//				huodongs = new ArrayList<HuoDong>();
//				break;
//			case XmlPullParser.START_TAG:
//				String name = parser.getName();
//				if("article".equals(name)){
//					huodong = new HuoDong();
//				}
//				if(huodong!=null){
//					if("id".equals(name))
//						huodong.setId(Integer.parseInt(parser.nextText()));
//					if("title".equals(name)){
//						huodong.setTitle(parser.nextText());
//					}
//					if ("content".equals(name)) {
//						huodong.setContent(parser.nextText());
//					}
//					if("pub_date".equals(name)){
//						huodong.setDatetime(parser.nextText());
//					}
//					if("imgUris".equals(name)){
//						huodong.setImgurls(parser.nextText());
//						if(!"".equals(huodong.getImgurls())){
//							String string[];
//							int i = 0;
//					        StringTokenizer tokenizer = new StringTokenizer(huodong.getImgurls(), "|");
//					        string = new String[tokenizer.countTokens()];
//					         while (tokenizer.hasMoreTokens()) {
//					            string[i] = new String();
//					            string[i] = tokenizer.nextToken();
//					            i++;
//					        }
//					        huodong.setImgarr(string);
//						}
//					}
//					if("url".equals(name)){
//						huodong.setUrl(parser.nextText());
//					}
//				}
//				break;
//			case XmlPullParser.END_TAG:
//				if("article".equals(parser.getName())){
//					huodongs.add(huodong);
//					huodong = null;
//				}
//				break;
//			}
//			eventType = parser.next();
//		}
//		return huodongs;
//	}
//	
//	public static List<Language> GetLan(String str)throws Exception{
//		if(str==null||"".equals(str))
//			return null;
//		List<Language> lans = null;
//		Language lan = null;
//		XmlPullParser parser = Xml.newPullParser();
//		InputStream  inputStream   =   new   ByteArrayInputStream(str.getBytes());
//		parser.setInput(inputStream, "utf-8");
//		int eventType = parser.getEventType();
//		while(eventType!=XmlPullParser.END_DOCUMENT){
//			switch (eventType) {
//			case XmlPullParser.START_DOCUMENT:
//				lans = new ArrayList<Language>();
//				break;
//			case XmlPullParser.START_TAG:
//				String name = parser.getName();
//				if("article".equals(name)){
//					lan = new Language();
//				}
//				if(lan!=null){
//					if("id".equals(name))
//						lan.setId(Integer.parseInt(parser.nextText()));
//					if("title".equals(name)){
//						lan.setTitle(parser.nextText());
//					}
//					if ("content".equals(name)) {
//						lan.setContent(parser.nextText());
//					}
//					if("desc".equals(name)){
//						lan.setDesc(parser.nextText());
//					}
//					if("pub_date".equals(name)){
//						lan.setDatetime(parser.nextText());
//					}
//					if("imgUris".equals(name)){
//						lan.setImgurls(parser.nextText());
//						if(!"".equals(lan.getImgurls())){
//							String string[];
//							int i = 0;
//					        StringTokenizer tokenizer = new StringTokenizer(lan.getImgurls(), "|");
//					        string = new String[tokenizer.countTokens()];
//					         while (tokenizer.hasMoreTokens()) {
//					            string[i] = new String();
//					            string[i] = tokenizer.nextToken();
//					            i++;
//					        }
//					        lan.setImgarr(string);
//						}
//					}
//					if("url".equals(name)){
//						lan.setUrl(parser.nextText());
//					}
//				}
//				break;
//			case XmlPullParser.END_TAG:
//				if("article".equals(parser.getName())){
//					lans.add(lan);
//					lan = null;
//				}
//				break;
//			}
//			eventType = parser.next();
//		}
//		return lans;
//	}
}
