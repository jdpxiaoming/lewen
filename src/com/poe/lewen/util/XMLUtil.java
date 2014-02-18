package com.poe.lewen.util;


import com.poe.lewen.bean.Constant;

public class XMLUtil {

	/**
	 * 登录
	 * 
	 * @param userName
	 * @param passwd
	 * @return
	 */
	public static String MakeXML(String userName, String passwd) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0xA001</cmd>");
		sb.append("<userName>" + userName + "</userName>");
		sb.append("<userPsw>" + Md5Util.getMD5Str(passwd) + "</userPsw>");
		sb.append("<loginType>0</loginType>");
		sb.append("<loginIp>" + Constant.str_login_ip + "</loginIp>");
		sb.append("<pcName>nyPcName</pcName>");
		sb.append("</JoyMon>");
		sb.append("\0");

		return sb.toString();
	}

	/**
	 * 获取播放列表
	 * 
	 * @param userName
	 * @param passwd
	 * @return
	 */
	public static String MakeXML4List(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0xE007</cmd>");
		sb.append("<userId>" + userId + "</userId>");
		sb.append("</JoyMon>");
		sb.append("\0");

		return sb.toString();
	}

	/**
	 * 收藏列表的xml 请求
	 * 
	 * @param userId
	 *            用户id
	 * @param userName
	 *            登陆用户名
	 * @return 组织好的mxl ex: <JoyMon> <type>req</type> <cmd>0XC002</cmd>
	 *         <userId>10034</userId> <userName>cxm</userName> </JoyMon>
	 */
	public static String MakeXML4SaveList(String userId, String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0XC002</cmd>");
		sb.append("<userId>" + userId + "</userId>");
		sb.append("<userName>" + userName + "</userName>");
		sb.append("</JoyMon>");
		sb.append("\0");

		return sb.toString();
	}

	/**
	 * 心跳xml
	 * 
	 * @return
	 */
	public static String MakeXML4Heart(String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0XA000</cmd>");
		sb.append("<userName>" + userName + "</userName>");
		sb.append("</JoyMon>");
		sb.append("\0");

		return sb.toString();
	}

	/**
	 * 获取指定编号处的直播地址 <nodeId>3</nodeId>;//
	 * 
	 * @return
	 */
	public static String MakeXML4PlayAddress(String nodeId) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0XA000</cmd>");
		sb.append("<nodeId>" + nodeId + "</nodeId>");
		sb.append("</JoyMon>");
		sb.append("\0");
		return sb.toString();
	}

	/**
	 * 
	 * @param nodeId
	 * @return
	 * 
	 *         <JoyMon> <type>req</type> <cmd>0XC001</cmd>
	 *         <userId>10034</userId> <userName>cxm</userName>
	 *         <channelName>收藏的通道名字</channelName> <channelNo>收藏的通道编号</channelNo>
	 *         </JoyMon>
	 */
	public static String MakeXML4SaveAdd(String userId, String userName, String channelName, String channelNo, String channelId) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0XC001</cmd>");
		sb.append("<userId>" + userId + "</userId>");
		sb.append("<userName>" + userName + "</userName>");

		// try {
		// channelName = new String(channelName.getBytes("utf-8"),"gb2312");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		sb.append("<channelName>" + channelName + "</channelName>");
		// sb.append("<channelName>channelName</channelName>");
		sb.append("<channelId>" + channelId + "</channelId>");
		sb.append("</JoyMon>");
		sb.append("\0");
		return sb.toString();
	}

	/**
	 * 请求获取历史录像
	 * 
	 * @return
	 */
	public static String makeXML4PlayVideoHistory(String deviceId, String channelId, String beginTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0xE009</cmd>");
		sb.append("<deviceId>" + deviceId + "</deviceId>");
		sb.append("<channelId>" + channelId + "</channelId>");
		sb.append("<beginTime>" + beginTime + "</beginTime>");
		sb.append("<endTime>" + endTime + "</endTime>");
		sb.append("</JoyMon>");
		sb.append("\0");
		return sb.toString();
	}

	/**
	 * 请求赞此通道报文
	 * 
	 * @return
	 */
	public static String makeXML4Zan(String channelId) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0XC004</cmd>");
		sb.append("<channelId>" + channelId + "</channelId>");
		sb.append("</JoyMon>");
		sb.append("\0");
		return sb.toString();
	}

	/**
	 * 、请求获取演示地址
	 * 
	 * @return
	 */
	public static String makeXML4Demo() {
		return "<JoyMon><type>req</type><cmd>0XC003</cmd></JoyMon>\0";
	}

	/**
	 * 请求获取演示地址
	 * 
	 * @param channelId
	 * @param type
	 *            1：进入观看通道 0：退出观看通道
	 * @return
	 */
	public static String makeXML4WatchChannel(String channelId, String type) {
		return "<JoyMon><type>req</type><cmd>0xA040</cmd><type>" + type + "</type><channelId>" + channelId + "</channelId></JoyMon>\0";
	}

	
	/**
	 * 请求获取在线用户列表
	 * 
	 * @return
	 */
	public static String makeXML4OnlineUser() {
		return "<JoyMon><type>req</type><cmd>0XC005</cmd></JoyMon>\0";
	}
	/**
	 * 请求通话
	 * 
	 * @return
	 */
	public static String makeXML4Speak(String from,String to) {
		return "<JoyMon><type>req</type><cmd>0XC00C</cmd>" +
				"<from>" +from +	"</<from>" +
				"<to>" +	to +	"</to>" +
				"</JoyMon>";
	}
	
	
	/**
	 * 请求获取虫豸记录
	 * 
	 * @return
	 */
	public static String makeXML4RechargeRecord(String userId) {
		return "<JoyMon><type>req</type><cmd>0XC00E</cmd><userId>" +		userId +	"</userId></JoyMon>\0";
	}
}
