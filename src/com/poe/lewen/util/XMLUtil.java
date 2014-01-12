package com.poe.lewen.util;

import com.poe.lewen.bean.Constant;

public class XMLUtil {

	/**
	 * 登录
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

		return sb.toString();
	}
	
	/**
	 * 获取播放列表
	 * @param userName
	 * @param passwd
	 * @return
	 */
	public static String MakeXML4List(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0xE007</cmd>");
		sb.append("<userId>" + userId + "</userName>");
		sb.append("</JoyMon>");

		return sb.toString();
	}
	
	/**
	 * 心跳xml
	 * @return
	 */
	public static String MakeXML4Heart(String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0XA000</cmd>");
		sb.append("<userName>" + userName + "</userName>");
		sb.append("</JoyMon>");

		return sb.toString();
	}
	
	/**
	 * 获取指定编号处的直播地址
	 * <nodeId>3</nodeId>;//
	 * @return
	 */
	public static String MakeXML4PlayAddress(String nodeId) {
		StringBuffer sb = new StringBuffer();
		sb.append("<JoyMon>");
		sb.append("<type>req</type>");
		sb.append("<cmd>0XA000</cmd>");
		sb.append("<nodeId>" + nodeId + "</nodeId>");
		sb.append("</JoyMon>");

		return sb.toString();
	}
	
}
