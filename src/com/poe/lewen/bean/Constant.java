package com.poe.lewen.bean;

public class Constant {

	public static String str_login_ip = "219.146.251.189";
//	public static String str_login_ip = "60.18.152.38";//"202.136.60.234";
	public static int login_port = 8888;//37779;//8808;//
	
	public static int REQ_LOGIN = 0XA001;	//用登陆请求XML格式
	
	public static int REQ_GET_ORG_STRUCTURE         = 0XE007;//获取直播组织架构
	
	public static int RSP_GET_ORG_STRUCTURE         = 0XB403; //响应获取直播组织架构
	
	public static int REQ_HEART				= 0XA000 ;//心跳
	
	public static int REQ_GET_VIDEO_ADDR            = 0XE008;      //获取直播地址
	
	
}
