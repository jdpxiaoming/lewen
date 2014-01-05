package com.poe.lewen.util;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
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
		private boolean hasAttr = true;

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
						rsp.setType(data.trim().equals("null")?"":rsp.getType()+data.trim());
					} else if ("cmd".equals(tag)) {
						rsp.setCmd(data.trim().equals("null")?"":rsp.getCmd()+data.trim());
					} else if ("userId".equals(tag)) {
						rsp.setUserId(data.equals("null")?"":rsp.getUserId()+data.trim());
					} else if ("roleId".equals(tag)) {
						rsp.setRoleId(data.equals("null")?"":rsp.getRoleId()+data.trim());
					} else if ("userLoginRet".equals(tag)) {
						rsp.setUserLoginRet(data.equals("null")?"":rsp.getUserLoginRet()+data.trim());
					} else if ("ierrorCode".equals(tag)) {
						rsp.setIerrorCode(data.equals("null")?"":rsp.getIerrorCode()+data.trim());
					} else if ("menuShow".equals(tag)) {
						rsp.setMenuShow(data.equals("null")?"":rsp.getMenuShow()+data.trim());
					} else if ("err".equals(tag)) {
						rsp.setErr(data.equals("null")?"":rsp.getErr()+data.trim());
					} 
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			super.endElement(uri, localName, qName);
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
