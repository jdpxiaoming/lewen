package com.poe.lewen.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.poe.lewen.bean.channel;

public class SaxService4Channel {

	public List<channel> getRSP(InputSource iStream) {

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

		private List<channel> list_channel = null;
		private channel chanel = null;
		private String tag = null;

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.startDocument();
			list_channel =new ArrayList<channel>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			super.startElement(uri, localName, qName, attributes);

			if ("node".equals(localName)) {
				chanel = new channel();
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
					if ("id".equals(tag)) {
						chanel.setId(data);
					} else if ("name".equals(tag)) {
						chanel.setName(chanel.getName()!=null?chanel.getName()+data.trim():data.trim());
					} else if ("parent".equals(tag)) {
						chanel.setParent_id(data);
					}
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			super.endElement(uri, localName, qName);
			if("node".equals(localName)){
				if(null!=chanel){
					list_channel.add(	chanel);
					chanel = null;
				}
			}
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();

		}

		public List<channel> getData() {
			return list_channel;
		}
	}
}
