package com.poe.lewen.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;
import org.xml.sax.InputSource;

import android.os.Handler;
import android.util.Log;
import com.poe.lewen.MyApplication;
import com.poe.lewen.bean.Constant;
import com.poe.lewen.bean.channel;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.service.SaxService4Channel;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.socket.Loger;
import com.poe.lewen.socket.TCPSocketCallback;
import com.poe.lewen.socket.TCPSocketConnect;

/**
 * typedef struct tagLoginReq { char userName[32]; char userPsw[64];//需要Md5加密
 * int userType; char loginIp[32]; char pcName[32]; }LoginReq;
 */
public class Packet {
	
	static TCPSocketConnect connect = null ;
	
	private byte[] buf = null;
	
	private static int login_req = -1;//0 :登录 1：获取播放列表 2：获取直播流 3：心跳保持
	
	/**
	 * 将int转为低字节在前，高字节在后的byte数组
	 */
	private static byte[] toLH(long n) {

		 byte[] b = new byte[4];
		 b[0] = (byte) (n & 0xff);
		 b[1] = (byte) (n >> 8 & 0xff);
		 b[2] = (byte) (n >> 16 & 0xff);
		 b[3] = (byte) (n >> 24 & 0xff);
		 
		return b;
	}
	
	/**
	 * int 4bytes
	 * @param n
	 * @return
	 */
	private static byte[] toLH(int n) {

		 byte[] b = new byte[4];
		 b[0] = (byte) (n & 0xff);
		 b[1] = (byte) (n >> 8 & 0xff);
		 b[2] = (byte) (n >> 16 & 0xff);
		 b[3] = (byte) (n >> 24 & 0xff);
		 
		return b;
	}
	/**
	 * unsigned int
	 * @param n
	 * @return
	 */
	private static byte[] toLH2(int n) {

		 byte[] b = new byte[2];
		 b[0] = (byte) (n & 0xff);
		 b[1] = (byte) (n >> 8 & 0xff);

		return b;
	}

	/**
	 * 将float转为低字节在前，高字节在后的byte数组
	 */
	private static byte[] toLH(float f) {

		return toLH(Float.floatToRawIntBits(f));

	}

	/**
	 * 构造并转换
	 */
	public Packet(int iType, int iLen, int verInterface, String packetBody) {
		byte[] temp = null;

		buf = new byte[packetBody.getBytes().length + 8];
		temp = toLH2(iType);
		System.arraycopy(temp, 0, buf, 0, temp.length);

		temp = toLH2(iLen);
		System.arraycopy(temp, 0, buf, 2, temp.length);

		temp = toLH(verInterface);
		System.arraycopy(temp, 0, buf, 4, temp.length);

		System.arraycopy(packetBody.getBytes(), 0, buf, 8, packetBody.length());

	}

	/**
	 * 返回要发送的数组
	 */
	public byte[] getBuf() {
		return buf;
	}

	/**
	 * 登录
	 */
	public static void login(final String userName, final String passwd) {
		
		connect = new TCPSocketConnect(new TCPSocketCallback() {
			
			@Override
			public void tcp_receive(byte[] buffer)  {
				String str = "";
				ByteArrayOutputStream ba =new ByteArrayOutputStream();
				try {
					ba.write(buffer);
					 str =new String(ba.toByteArray(),"GBK");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				System.out.println(str);
				
				switch(login_req){
				case 0://login
					try {
						MyApplication.rsp_login = XmlToListService.GetLogin(str);
						if(MyApplication.rsp_login!=null){
							System.out.println(MyApplication.rsp_login.getUserId());
							
							//发送请求：获取 播放列表
							String tmp =  XMLUtil.MakeXML4List(MyApplication.rsp_login.getUserId());
							byte[] req =new Packet(Constant.REQ_GET_ORG_STRUCTURE, tmp.length(), 1, tmp).getBuf();
							connect.write(req);
							Log.e("req", bytesToHexString(req));
							login_req = 1;
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 1://play list
					//获取播放列表
					try {
						List<channel> list =XmlToListService.GetChannelList(str);
						if(list!=null){
							for(channel c: list){
								System.out.println(c.getName());
							}
							
							//发送请求：获取 第一个直播地址
							String tmp =  XMLUtil.MakeXML4PlayAddress(list.get(4).getId());
							byte[] req =new Packet(Constant.REQ_GET_VIDEO_ADDR, tmp.length(), 1, tmp).getBuf();
							connect.write(req);
							Log.e("req", bytesToHexString(req));
							login_req = 2;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 2://real rtmp address
					try {
						channelOnLine cline1 =XmlToListService.GetVideoAddress(str);
						if(null!=cline1){
							System.out.println(cline1.getPlayer_Addr());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				
			}
			
			@Override
			public void tcp_disconnect() {
				// TODO Auto-generated method stub
				Loger.i("tcp_disconnect()");
			}
			
			@Override
			public void tcp_connected() {
				Loger.i("tcp_connect()");
				//login 数据
				String tmp = XMLUtil.MakeXML(userName, passwd);
				byte[] req =new Packet(Constant.REQ_LOGIN, tmp.length(), 1, tmp).getBuf();
				connect.write(req);
				Log.e("req", bytesToHexString(req));
				login_req = 0;
				
			}
		});
		
		connect.setAddress(Constant.str_login_ip, Constant.login_port);
		new Thread(connect).start();
		
	}

	private static InputSource getInputSource(Socket sock) throws UnsupportedEncodingException, IOException {
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream(),"GBK");
		InputSource inputSource = new InputSource(streamReader);
		return inputSource;
	}

	

	/**
	 * 登录
	 */
	public static void getPlayingList(String userId) {

		try {
			String tmp =  XMLUtil.MakeXML4List(userId);
			Socket sock = MyApplication.getInstance().getSocket();
			byte[] req =new Packet(0XE007, tmp.length(), 1, tmp).getBuf();
			Log.e("req", bytesToHexString(req));
			sock.getOutputStream().write(req);

			//解析文件流 inputstream
			SaxService4Channel sax = new SaxService4Channel();
			InputSource inputSource = getInputSource(sock);
			List<channel> list =sax.getRSP(inputSource); 
			
			//show 
			if(list!=null&&list.size()>0){
				for(channel c :list){
					Log.e("rsp","频道:  "+ c.getName());
				}
			}
//			 sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * bytes 转化为 0x 16进制格式 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src){  
		
	    StringBuilder stringBuilder = new StringBuilder("");  
	    
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
	
	public static void close(){
		if(connect!=null){
			connect.disconnect();
		}
	}
	
}
