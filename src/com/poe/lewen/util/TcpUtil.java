package com.poe.lewen.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
public class TcpUtil {

	public static Handler handler = null;

	static TCPSocketConnect connect = null;

	private static int login_req = -1;// 0 :登录 1：获取播放列表 2：获取直播流 3：心跳保持

	private static boolean isConnected = false;

	public static String userName;// 缓存登陆 的用户名

	private static boolean isLogin = false;

	private static Handler heart_handler = new Handler();

	private static Runnable r_heart;



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
					TcpUtil.handler.sendMessage(TcpUtil.handler.obtainMessage(login_req, str));
			}

			@Override
			public void tcp_disconnect() {
				isConnected = false;
//				if (null != handler)
//					Packet.handler.sendMessage(Packet.handler.obtainMessage(login_req,null));
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

	public static void startPolling() {

		isLogin = true;

		if (r_heart == null) {
			r_heart = new Runnable() {

				@Override
				public void run() {
					if (isLogin) {

						TcpUtil.heartJump(MyApplication.username);
						heart_handler.postDelayed(r_heart, 30 * 1000);
					}
				}
			};
		}

		heart_handler.postDelayed(r_heart, 30 * 1000);

	};

	/*
	 * 停止停跳检测 （socket连接登陆后发送）
	 */
	public static void stopPolling() {
		isLogin = false;
	};

	// ******************************************
	// ******************************************
	/**
	 * 登录
	 */
	public static void login(final String userName, final String passwd, Handler handler) {
		TcpUtil.handler = handler;
		TcpUtil.userName = userName;

		// close();
		if (!isConnected) {
			init();
		}

		if (login_req != 0) {
			String tmp = XMLUtil.MakeXML(userName, passwd);
			System.out.println(tmp);
			System.out.println("login长度：" + tmp.length());
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

		TcpUtil.handler = handler;
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

		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 播放列表
		String tmp = XMLUtil.MakeXML4SaveAdd(MyApplication.rsp_login.getUserId(), userName, channelName, channelNo, channelId);
		System.out.println(tmp);
		System.out.println("req字符串的长度为：" + tmp.length());
		byte[] req = new Packet(Constant.REQ_ADD_FAVORITES, tmp.length(), 1, tmp).getBuf();
		connect.write(req);

		StringBuffer sb = new StringBuffer();
		for (byte b : req) {
			sb.append(b);
		}

		Log.e("req 2进制", req.toString());
		String str = bytesToHexString(req);
		Log.e("req", str);
		System.out.println("byte[]转化为16进制后长度：" + str.length());
		login_req = 1;
	}

	// ************************************
	/**
	 * 获得收藏组织结构
	 * 
	 * @param handler
	 */
	public static void getSaveList(Handler handler) {

		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 播放列表
		String tmp = XMLUtil.MakeXML4SaveList(MyApplication.rsp_login.getUserId(), TcpUtil.userName);

		System.out.println(tmp);
		System.out.println("req字符串的长度为：" + tmp.length());

		byte[] req = new Packet(Constant.REQ_LIST_FAVORITES, tmp.length(), 1, tmp).getBuf();
		connect.write(req);
		Log.e("req 2进制", req.toString());
		String str = bytesToHexString(req);
		Log.e("req", str);
		System.out.println("byte[]转化为16进制后长度：" + str.length());
		login_req = 1;

	}

	// ***************
	// 获取某个摄像头的具体播放地址信息
	public static void getVideoAddress(String nodeId, Handler handler) {
		TcpUtil.handler = handler;
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

	// ***************
	// 历史录像回放
	public static void getVideoHistory(String deviceId, String channelId, String beginTime, String endTime, Handler handler) {
		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.makeXML4PlayVideoHistory(deviceId, channelId, beginTime, endTime);
		System.out.println(tmp);
		byte[] req = new Packet(Constant.REQ_GET_VIDEO_HISTORY_REC, tmp.length(), 1, tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
		login_req = 1;
	}

	
	// ***************
	// 赞次通道
	public static void praiseChannel(String channelId, Handler handler) {
		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.makeXML4Zan(channelId);
		byte[] req = new Packet(Constant.REQ_PRAISE_CHANNEL, tmp.length(), 1, tmp).getBuf();
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

		System.out.println("byte[]转化为16进制前长度：" + src.length);
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

		String str = stringBuilder.toString();
		Log.e("req", str);
		System.out.println("byte[]转化为16进制后长度：" + str.length());

		return str;
	}

	/**
	 * 保证心跳连接
	 * 
	 * @param userName
	 */
	public static void heartJump(String userName) {
		TcpUtil.handler = null;
		if (isConnected) {
			String tmp = XMLUtil.MakeXML4Heart(userName);
			byte[] req = new Packet(Constant.REQ_HEART, tmp.length(), 1, tmp).getBuf();
			connect.write(req);
			Log.e("req", bytesToHexString(req));
			login_req = 2;
		}
	}

	public static boolean isConnected() {
		return isConnected;
	}

	/*
	 * 暂时中断连接、等待下次继续
	 */
	public static void close() {
		System.out.println("退出登录 ：TcpUtil --close()");
		if (connect != null) {
			isConnected = false;
			stopPolling();
			connect.disconnect();
		}
	}

}