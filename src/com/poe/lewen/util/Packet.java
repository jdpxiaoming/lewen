package com.poe.lewen.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.os.Handler;
import android.util.Log;
import com.poe.lewen.MyApplication;
import com.poe.lewen.bean.Constant;
import com.poe.lewen.socket.Loger;
import com.poe.lewen.socket.TCPSocketCallback;
import com.poe.lewen.socket.TCPSocketConnect;

/**
 * typedef struct tagLoginReq { char userName[32]; char userPsw[64];//需要Md5加密
 * int userType; char loginIp[32]; char pcName[32]; }LoginReq;
 */
public class Packet {

	public static Handler handler = null;

	static TCPSocketConnect connect = null;

	private byte[] buf = null;

	private static int login_req = -1;// 0 :登录 1：获取播放列表 2：获取直播流 3：心跳保持

	private static boolean isConnected = false;

	public static String userName;// 缓存登陆 的用户名

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
	 * 
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
	 * 
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
	private static void init() {

		connect = new TCPSocketConnect(new TCPSocketCallback() {

			@Override
			public void tcp_receive(byte[] buffer) {
				String str = "";
				ByteArrayOutputStream ba = new ByteArrayOutputStream();
				try {
					ba.write(buffer);
					str = new String(ba.toByteArray(), "GBK");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				System.out.println(str);

				// 反馈工具类
				if (null != handler)
					Packet.handler.sendMessage(Packet.handler.obtainMessage(login_req, str));
			}

			@Override
			public void tcp_disconnect() {
				isConnected = false;
				Loger.i("tcp_disconnect()");
			}

			@Override
			public void tcp_connected() {
				isConnected = true;
				Loger.i("tcp_connect()");
			}
		});
		
		System.out.println(MyApplication.getPreferenceData("host"));
		System.out.println(MyApplication.getPreferenceData("port"));
		connect.setAddress(MyApplication.getPreferenceData("host"), Integer.parseInt(MyApplication.getPreferenceData("port")));
		
		new Thread(connect).start();
		
	}

	// ******************************************
	/**
	 * 登录
	 */
	public static void login(final String userName, final String passwd, Handler handler) {
		Packet.handler = handler;
		Packet.userName = userName;

		// close();
		if (!isConnected) {
			init();
		}

		if (login_req != 0) {
			String tmp = XMLUtil.MakeXML(userName, passwd);
			byte[] req = new Packet(Constant.REQ_LOGIN, tmp.length(), 1, tmp).getBuf();
			connect.write(req);
			Log.e("req", bytesToHexString(req));
			login_req = 0;
		}
	}

	// ************************************
	/**
	 * 获得播放组织结构
	 * 
	 * @param handler
	 */
	public static void getPlayingList(Handler handler) {

		Packet.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 播放列表
		String tmp = XMLUtil.MakeXML4List(MyApplication.rsp_login.getUserId());
		byte[] req = new Packet(Constant.REQ_GET_ORG_STRUCTURE, tmp.length(), 1, tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
		login_req = 1;
	}

	// ************************************
	/**
	 * 添加收藏
	 * 
	 * @param handler
	 */
	public static void SaveChannel(Handler handler, String channelName, String channelNo, String channelId) {

		Packet.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 播放列表
//		String str = "";
		String tmp = XMLUtil.MakeXML4SaveAdd(MyApplication.rsp_login.getUserId(), userName, channelName, channelNo, channelId);
//		try {
//			tmp = new String(tmp.getBytes("utf-8"), "GBK");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}

		System.out.println(tmp);
		byte[] req = new Packet(Constant.REQ_ADD_FAVORITES, tmp.length(), 1, tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
		login_req = 3;
	}

	// ************************************
	/**
	 * 获得收藏组织结构
	 * 
	 * @param handler
	 */
	public static void getSaveList(Handler handler) {

		Packet.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 播放列表
		String tmp = XMLUtil.MakeXML4SaveList(MyApplication.rsp_login.getUserId(), Packet.userName);

		try {
			tmp = new String(tmp.getBytes("utf-8"), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(tmp);
		byte[] req = new Packet(Constant.REQ_LIST_FAVORITES, tmp.length(), 1, tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
		login_req = 1;

	}

	// ***************
	// 获取某个摄像头的具体播放地址信息
	public static void getVideoAddress(String nodeId, Handler handler) {
		Packet.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.MakeXML4PlayAddress(nodeId);
		byte[] req = new Packet(Constant.REQ_GET_VIDEO_ADDR, tmp.length(), 1, tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
		login_req = 2;

	}

	/**
	 * bytes 转化为 0x 16进制格式
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {

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

	public static boolean isConnected() {
		return isConnected;
	}

	public static void close() {
		if (connect != null) {
			connect.disconnect();
		}
	}

}
