package com.poe.lewen.util;

import java.net.Socket;

import com.poe.lewen.bean.Constant;

/**
 * typedef struct tagLoginReq
{
	char userName[32];
	char userPsw[64];//需要Md5加密
	int  userType;
	char loginIp[32];
	char pcName[32];	
}LoginReq;
 *
 */
public class Packet {
	private byte[] buf = null;

	/**
	* 将int转为低字节在前，高字节在后的byte数组
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
	* 将float转为低字节在前，高字节在后的byte数组
	*/
	private static byte[] toLH(float f) {
	return toLH(Float.floatToRawIntBits(f));
	}

	/**
	* 构造并转换
	*/
	public Packet(int packetID, int packetLen, String packetBody) {
	byte[] temp = null;

	buf = new byte[packetBody.getBytes().length + 8];
	temp = toLH(packetID);
	System.arraycopy(temp, 0, buf, 0, temp.length);

	temp = toLH(packetLen);
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
	* 发送测试
	*/
	public static void send(String userName,String passwd) {
		
	try {
		
	String tmp = MakeXML(userName,passwd);
	
	Socket sock = new Socket(Constant.str_login_ip, Constant.login_port);
	sock.getOutputStream().write(
	new Packet(123, tmp.length(), tmp).getBuf());
	
	
	   byte[] recvHead =new byte[4];
       int cForm;

       sock.getInputStream().read(recvHead,0, 4);

       cForm= Common.bytes2Integer(recvHead);
       System.out.println(cForm);
//	sock.close();
	} catch (Exception e) {
	e.printStackTrace();
	}
	}

	private static String MakeXML(String userName, String passwd) {
		// TODO Auto-generated method stub
//		<JoyMon>
//		<type>req</type>
//		<cmd>0xA001</cmd>
//		<userName>102</userName>
//		<userPsw>e10adc3949ba59abbe56e057f20f883e</userPsw>
//		<loginType>0</loginType>
//		<userType>1</userType>
//		<loginIp>127.0.0.1</loginIp>
//		<pcName>nyPcName</pcName>
//		</JoyMon>
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0xA001</cmd>");
		sb.append("<userName>"+userName+"</userName>");
		sb.append("<userPsw>" +Md5Util.getMD5Str(passwd)+	"</userPsw>");
		sb.append("<loginType>0</loginType>");
		sb.append("<loginIp>" +Constant.str_login_ip +"</loginIp>");
		sb.append("<pcName>nyPcName</pcName>");
		sb.append("</JoyMon>");
		
		return sb.toString();
	}
}
