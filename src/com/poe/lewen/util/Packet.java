package com.poe.lewen.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import android.os.Handler;
import android.util.Log;
import com.poe.lewen.MyApplication;
import com.poe.lewen.bean.Constant;
import com.poe.lewen.bean.channel;
import com.poe.lewen.bean.channelOnLine;
import com.poe.lewen.service.XmlToListService;
import com.poe.lewen.socket.Loger;
import com.poe.lewen.socket.TCPSocketCallback;
import com.poe.lewen.socket.TCPSocketConnect;

/**
 * typedef struct tagLoginReq { char userName[32]; char userPsw[64];//需要Md5加密
 * int userType; char loginIp[32]; char pcName[32]; }LoginReq;
 */
public class Packet {
	
	public static Handler handler = null;
	
	static TCPSocketConnect connect = null ;
	
	private byte[] buf = null;
	
	private static int login_req = -1;//0 :登录 1：获取播放列表 2：获取直播流 3：心跳保持
	
	private static boolean isConnected = false;
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
	
	/*
	 * 初始化 tcp连接
	 */
  private static 	void init(){
	  
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

				//反馈工具类
				if(null!=handler)
				Packet.handler.sendMessage(Packet.handler.obtainMessage(login_req, str));
			}

		
			
			@Override
			public void tcp_disconnect() {
				// TODO Auto-generated method stub
				isConnected = false;
				Loger.i("tcp_disconnect()");
			}
			
			@Override
			public void tcp_connected() {
				isConnected = true;
				Loger.i("tcp_connect()");
			}
		});
		
		connect.setAddress(Constant.str_login_ip, Constant.login_port);
		new Thread(connect).start();
	}

  //******************************************
	/**
	 * 登录
	 */
	public static void login(final String userName, final String passwd,Handler handler) {
		Packet.handler =handler;
		
		if(!isConnected){
			init();
		}
		if(login_req!=0){
			String tmp = XMLUtil.MakeXML(userName, passwd);
			byte[] req =new Packet(Constant.REQ_LOGIN, tmp.length(), 1, tmp).getBuf();
			connect.write(req);
			Log.e("req", bytesToHexString(req));
			login_req = 0;
		}
	}
	
	//************************************
	/**
	 * 获得播放组织结构
	 * @param handler
	 */
	public static void getPlayingList(Handler handler) {
		
		Packet.handler =handler;
		if(!isConnected){
			init();
		}
		
		//发送请求：获取 播放列表
		String tmp =  XMLUtil.MakeXML4List(MyApplication.rsp_login.getUserId());
		byte[] req =new Packet(Constant.REQ_GET_ORG_STRUCTURE, tmp.length(), 1, tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
		login_req = 1;
	}
	
	//***************
	//获取某个摄像头的具体播放地址信息
	public static void getVideoAddress(String nodeId,Handler handler){
		Packet.handler =handler;
		if(!isConnected){
			init();
		}
		
		//发送请求：获取 第一个直播地址
		String tmp =  XMLUtil.MakeXML4PlayAddress(nodeId);
		byte[] req =new Packet(Constant.REQ_GET_VIDEO_ADDR, tmp.length(), 1, tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
		login_req = 2;
		
	}
	
	private static void doSomeThing(String str) {
		
		switch(login_req){
		case 0://login
			try {
				MyApplication.rsp_login = XmlToListService.GetLogin(str);
				if(MyApplication.rsp_login!=null){
					System.out.println(MyApplication.rsp_login.getUserId());
//					//发送请求：获取 播放列表
//					String tmp =  XMLUtil.MakeXML4List(MyApplication.rsp_login.getUserId());
//					byte[] req =new Packet(Constant.REQ_GET_ORG_STRUCTURE, tmp.length(), 1, tmp).getBuf();
//					connect.write(req);
//					Log.e("req", bytesToHexString(req));
//					login_req = 1;
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
	
	public static boolean isConnected(){
		return isConnected;
	}
	
	public static void close(){
		if(connect!=null){
			connect.disconnect();
		}
	}
	
}
