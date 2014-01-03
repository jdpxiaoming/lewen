package com.poe.lewen.bean;

import struct.StructClass;
import struct.StructField;


/**
 *typedef struct tagLoginReq
{
	char userName[32];
	char userPsw[64];//需要Md5加密
	int  userType;
	char loginIp[32];
	char pcName[32];	
}LoginReq;
 */
@StructClass
public class tagLoginReq {

	@StructField(order = 0)
	public char[] userName = new char[32];//32
	
	@StructField(order = 1)
	public char[] userPsw=	new char[64];//64 Md5加密
	
	@StructField(order = 2)
	public int userType;// 0 /1
	
	@StructField(order = 3)
	public char[] loginIp=new char[32];//32
	
	@StructField(order = 4)
	public char[] pcName=new char[32];//32
	
}
