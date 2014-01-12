package com.poe.lewen.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.poe.lewen.bean.rsp_login;

public class SaxService4Login {

	public rsp_login getRSP(InputSource iStream) {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			poeHandler handler = new poeHandler();
			if (iStream != null) {
				// 开始解析
				parser.parse(iStream, handler);
			}

			return handler.getData();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	private class poeHandler extends DefaultHandler {

		private rsp_login rsp = null;
		private String tag = null;

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.startDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			super.startElement(uri, localName, qName, attributes);

			if ("JoyMon".equals(localName)) {
				rsp = new rsp_login();
			}
			
			tag = localName;
			
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			super.characters(ch, start, length);
			if (tag != null) {
				String data = new String(ch, start, length);
					if ("type".equals(tag)) {
						rsp.setType(rsp.getType()!=null?rsp.getType()+data.trim():data.trim());
					} else if ("cmd".equals(tag)) {
						rsp.setCmd(rsp.getCmd()!=null?rsp.getCmd()+data.trim():data.trim());
					} else if ("userId".equals(tag)) {
						rsp.setUserId(rsp.getUserId()!=null?rsp.getUserId()+data.trim():data.trim());
					} else if ("roleId".equals(tag)) {
						rsp.setRoleId(rsp.getRoleId()!=null?rsp.getRoleId()+data.trim():data.trim());
					} else if ("userLoginRet".equals(tag)) {
						rsp.setUserLoginRet(rsp.getUserLoginRet()!=null?rsp.getUserLoginRet()+data.trim():data.trim());
					} else if ("ierrorCode".equals(tag)) {
						rsp.setIerrorCode(rsp.getIerrorCode()!=null?rsp.getIerrorCode()+data.trim():data.trim());
					} else if ("menuShow".equals(tag)) {
						rsp.setMenuShow(rsp.getMenuShow()!=null?rsp.getMenuShow()+data.trim():data.trim());
					} else if ("err".equals(tag)) {
						rsp.setErr(rsp.getErr()!=null?rsp.getErr()+data.trim():data.trim());
					} 
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			super.endElement(uri, localName, qName);
			
//			if("JoyMon".equals(localName)){
//				System.out.println(rsp.getUserId());
//				
//				return rsp;
//			}
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}

		public rsp_login getData() {
			return rsp;
		}
	}
}
