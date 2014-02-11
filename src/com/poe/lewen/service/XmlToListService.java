package com.poe.lewen.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import com.poe.lewen.bean.channel;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.bean.history_video;
import com.poe.lewen.bean.resutl_parise;
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
	public static List<channelOnLine> GetVideoAddress(String str)throws Exception{
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
					if("parise".equals(name)){
						talkpic.setPraise(parser.nextText());
					}
					if("watchNum".equals(name)){
						talkpic.setWatch(parser.nextText());
					}
					if("historyWatch".equals(name)){
						talkpic.setHistory_watch(parser.nextText());
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
		
		return lists;
	}
	
	/*
	 * 解析失败返回NUll
	 */
	public static history_video  GetPlayVideoHistory(String str)throws Exception{
		if(str==null||"".equals(str))
			return null;
		history_video newInfo = null;
		XmlPullParser parser = Xml.newPullParser();
		InputStream  inputStream   =   new   ByteArrayInputStream(str.getBytes());
		parser.setInput(inputStream, "utf-8");
		int eventType = parser.getEventType();
		while(eventType!=XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				newInfo = new history_video();
				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if(newInfo!=null){
					
					if("deviceId".equals(name))
						newInfo.setDeviceId(parser.nextText());
					if("channelId".equals(name)){
						newInfo.setChannelId(parser.nextText());
					}
					if("playAddr".equals(name)){
						newInfo.setPlayaddr(parser.nextText());
					}
					if("ierrorCode".equals(name)){
						newInfo.setErr(parser.nextText());
					}
					if("err".equals(name)){
						newInfo.setErrdesc(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		
		return newInfo;
	}
	
	/*
	 * 赞次通道的count总数
	 */
	public static resutl_parise  GetCountOfZan(String str)throws Exception{
		if(str==null||"".equals(str))
			return null;
		resutl_parise newInfo = null;
		XmlPullParser parser = Xml.newPullParser();
		InputStream  inputStream   =   new   ByteArrayInputStream(str.getBytes());
		parser.setInput(inputStream, "utf-8");
		int eventType = parser.getEventType();
		while(eventType!=XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				newInfo = new resutl_parise();
				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				
				if(newInfo!=null){
					
					if("parise".equals(name))
						newInfo.setParise_count(parser.nextText());
					if("err	".equals(name)){
						newInfo.setErr(parser.nextText());
					}
					if("errdesc".equals(name)){
						newInfo.setErrdesc(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		
		return newInfo;
	}
	
}
