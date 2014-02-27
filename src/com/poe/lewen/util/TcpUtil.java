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

	private  static String str_cache="";
	/*
	 * 初始化 tcp连接
	 */
	private static void init() {

		connect = new TCPSocketConnect(new TCPSocketCallback() {

			@Override
			public void tcp_receive(byte[] buffer) {

				System.out.println("login_req:" + login_req);
				System.out.println("buffer长度：" + buffer.length);
				if (buffer.length == 0) {
					System.out.println("服务器断开！！！！！！");
				}

				if (login_req == 10) {
					byte[] result;
					String str2 = "";
					System.out.println("语音返回byte:" + getStringFromBuff(buffer));

					ByteArrayOutputStream ba = new ByteArrayOutputStream();
					try {
						ba.write(buffer);
						str2 = new String(ba.toByteArray());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("语音返回strs：" + str2);

					if (str2 != null && str2.contains(">")) {

						int iLast = 0;
						for (int i = 0; i < buffer.length; i++) {// 60 :< 62:>
							if (Integer.parseInt(buffer[i] + "") == 62) {
								// 判断是不是 <JoyMon>结尾 n:110
								if (Integer.parseInt(buffer[i - 1] + "") == 110) {
									iLast = i + 1;
									// break;
									System.out.println("iLast: " + iLast);
								}
							}
						}

						result = new byte[buffer.length - iLast];
						System.arraycopy(buffer, iLast, result, 0,
								buffer.length - iLast);

						System.out.println("处理后的result[]:"
								+ getStringFromBuff(result));

						if (null != handler)
							TcpUtil.handler.sendMessage(TcpUtil.handler
									.obtainMessage(login_req, result));
					}

				} else {
					
					String str = "";
					ByteArrayOutputStream ba = new ByteArrayOutputStream();
					try {
						ba.write(buffer);
						str = new String(ba.toByteArray(), "GBK");
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					str_cache +=str;
					// 检测强制离线、命令
					if (str != null && str.length() > 0) {
						System.out.println("检测强制离线：" + str);

						/**
						 * <JoyMon> <type>rsp</type> <cmd>0XB03A</cmd>
						 * <err>0</err> <errdesc>成功</errdesc> </JoyMon>
						 */
						if (str.contains("cmd")) {
							String cmd = str.substring(
									str.indexOf("<cmd>") + 5,
									str.indexOf("</cmd>"));
							System.out.println("cmd:" + cmd);
							if (cmd.equals("0XB03A")) {
								close();
								MyApplication.rsp_login = null;
								// 强制下线
							}
						}
						
						if(str.contains("</JoyMon>")){
							// 反馈工具类
							if (null != handler)
								TcpUtil.handler.sendMessage(TcpUtil.handler.obtainMessage(login_req, str_cache));
						}
					}
				}

			}

			@Override
			public void tcp_disconnect() {
				// isConnected = false;
				close();
				// if (null != handler)
				// Packet.handler.sendMessage(Packet.handler.obtainMessage(login_req,null));
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
		connect.setAddress(MyApplication.getPreferenceData("host"),
				Integer.parseInt(MyApplication.getPreferenceData("port")));

		new Thread(connect).start();
	}

	public static String getStringFromBuff(byte[] buffer) {
		String str = "";

		for (byte b : buffer) {
			str += b;
		}
		return str;
	}

	// public static byte[] getBuffFromString(String str){
	// return str.getBytes();
	// }

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

	private static void setLoginReq(String method, int request) {
		System.out.println("req:" + request + "from :" + method);
		login_req = request;
		str_cache = "";
	}

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
	public static void login(final String userName, final String passwd,
			Handler handler) {
		setLoginReq("login", 0);
		TcpUtil.handler = handler;
		TcpUtil.userName = userName;

		// close();
		if (!isConnected) {
			System.out.println("init tcputil connect");
			init();
		}

		String tmp = XMLUtil.MakeXML(userName, passwd);
		System.out.println(tmp);
		System.out.println("login长度：" + tmp.length());
		byte[] req = new Packet(Constant.REQ_LOGIN, tmp.length(), 1, tmp)
				.getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
	}

	// ************************************
	/**
	 * 获得播放组织结构
	 * 
	 * @param handler
	 */
	public static void getPlayingList(Handler handler) {
		setLoginReq("getPlayingList",1);

		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 播放列表
		String tmp = XMLUtil.MakeXML4List(MyApplication.rsp_login.getUserId());
		byte[] req = new Packet(Constant.REQ_GET_ORG_STRUCTURE, tmp.length(),
				1, tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
	}

	// ************************************
	/**
	 * 添加收藏
	 * 
	 * @param handler
	 */
	public static void SaveChannel(Handler handler, String channelName,
			String channelNo, String channelId) {
		setLoginReq("SaveChannel", 1);

		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 播放列表
		String tmp = XMLUtil.MakeXML4SaveAdd(
				MyApplication.rsp_login.getUserId(), userName, channelName,
				channelNo, channelId);
		System.out.println(tmp);
		System.out.println("req字符串的长度为：" + tmp.length());
		byte[] req = new Packet(Constant.REQ_ADD_FAVORITES, tmp.length(), 1,
				tmp).getBuf();
		connect.write(req);

		StringBuffer sb = new StringBuffer();
		for (byte b : req) {
			sb.append(b);
		}

		Log.e("req 2进制", req.toString());
		String str = bytesToHexString(req);
		Log.e("req", str);
		System.out.println("byte[]转化为16进制后长度：" + str.length());
	}

	// ************************************
	/**
	 * 获得收藏组织结构
	 * 
	 * @param handler
	 */
	public static void getSaveList(Handler handler) {

		setLoginReq("getSaveList", 1);
		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 播放列表
		String tmp = XMLUtil.MakeXML4SaveList(
				MyApplication.rsp_login.getUserId(), TcpUtil.userName);

		System.out.println(tmp);
		System.out.println("req字符串的长度为：" + tmp.length());

		byte[] req = new Packet(Constant.REQ_LIST_FAVORITES, tmp.length(), 1,
				tmp).getBuf();
		connect.write(req);
		Log.e("req 2进制", req.toString());
		String str = bytesToHexString(req);
		Log.e("req", str);
		System.out.println("byte[]转化为16进制后长度：" + str.length());

	}

	// ***************
	// 获取某个摄像头的具体播放地址信息
	public static void getVideoAddress(String nodeId, Handler handler) {
		setLoginReq("getVideoAddress", 2);
		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.MakeXML4PlayAddress(nodeId);
		byte[] req = new Packet(Constant.REQ_GET_VIDEO_ADDR, tmp.length(), 1,
				tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
	}

	// ***************
	// 历史录像回放
	public static void getVideoHistory(String deviceId, String channelId,
			String beginTime, String endTime, Handler handler) {
		setLoginReq("getVideoHistory", 1);
		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.makeXML4PlayVideoHistory(deviceId, channelId,
				beginTime, endTime);
		System.out.println(tmp);
		byte[] req = new Packet(Constant.REQ_GET_VIDEO_HISTORY_REC,
				tmp.length(), 1, tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
	}

	// ***************
	// 赞次通道
	public static void praiseChannel(String channelId, Handler handler) {
		setLoginReq("praiseChannel", 2);
		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.makeXML4Zan(channelId);
		byte[] req = new Packet(Constant.REQ_PRAISE_CHANNEL, tmp.length(), 1,
				tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));

	}

	// 赞次通道
	public static void getDemoList(final Handler handler) {
		setLoginReq("getDemoList", 3);
		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}
		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.makeXML4Demo();
		byte[] req = new Packet(Constant.REQ_LIST_DEMO_ADDR, tmp.length(), 1,
				tmp).getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));

	}

	/**
	 * 观看某个视频+1
	 * 
	 * @param userName
	 */
	public static void WatchChannel(String channelId, String type) {
		TcpUtil.handler = null;
		if (isConnected) {
			String tmp = XMLUtil.makeXML4WatchChannel(channelId, type);
			byte[] req = new Packet(Constant.REQ_ENTER_LEAVE_CHANNEL,
					tmp.length(), 1, tmp).getBuf();
			connect.write(req);
			Log.e("req", bytesToHexString(req));
			login_req = 1;
		}
	}

	// 获取在线用户列表
	public static void getUserList(final Handler handler) {
		setLoginReq("getUserList", 1);
		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.makeXML4OnlineUser();
		byte[] req = new Packet(Constant.REQ_ONLINE_USER, tmp.length(), 1, tmp)
				.getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
	}

	// 获取在线用户列表
	public static void reqSpeak(String from, String to, byte[] content,
			final Handler handler) {
		setLoginReq("reqSpeak", 10);
		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.makeXML4Speak(from, to);
		byte[] req = new Packet(Constant.REQ_VOICE, tmp.length()
				+ content.length, 1, tmp).getBuf();

		Log.i("req_xml:", +req.length + ">>" + getStringFromBuff(req));
		// 处理数据拼接
		byte[] datas = new byte[req.length + content.length];

		System.arraycopy(req, 0, datas, 0, req.length);
		System.arraycopy(content, 0, datas, req.length, content.length);

		System.out.println("16進制語音數據："+bytesToHexString(datas));
		Log.e("req", datas.length + ">>" + getStringFromBuff(datas));
		connect.write(datas);

		Log.i("req", getStringFromBuff(datas));

	}

	// 获取充值记录
	public static void getRechargeRecord(String userId, final Handler handler) {

		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.makeXML4RechargeRecord(userId);
		byte[] req = new Packet(Constant.REQ_BUY, tmp.length(), 1, tmp)
				.getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
		setLoginReq("getRechargeRecord", 1);

	}

	// 获取消费记录
	public static void getConsumeRecord(String userId, final Handler handler) {

		TcpUtil.handler = handler;
		if (!isConnected) {
			init();
		}

		// 发送请求：获取 第一个直播地址
		String tmp = XMLUtil.makeXML4RechargeRecord(userId);
		byte[] req = new Packet(Constant.REQ_CONSUME, tmp.length(), 1, tmp)
				.getBuf();
		connect.write(req);
		Log.e("req", bytesToHexString(req));
		setLoginReq("getConsumeRecord", 1);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	// ---------------------------------------------------------------------------------------------------------------
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
		// Log.e("req", str);
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
			byte[] req = new Packet(Constant.REQ_HEART, tmp.length(), 1, tmp)
					.getBuf();
			connect.write(req);
			Log.e("req", bytesToHexString(req));
			// login_req = 2;
		}
	}

	public static boolean isConnected() {
		return isConnected;
	}

	/*
	 * 暂时中断连接、等待下次继续
	 */
	private static void close() {
		System.out.println("退出登录 ：TcpUtil --close()");
		if (connect != null) {
			isConnected = false;
			stopPolling();
			connect = null;
		}
	}

	public static void disconnect() {
		if (connect != null) {
			connect.disconnect();
		}
	}
}
