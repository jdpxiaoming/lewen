package com.poe.lewen.util;


/**
 * typedef struct tagLoginReq { char userName[32]; char userPsw[64];//需要Md5加密
 * int userType; char loginIp[32]; char pcName[32]; }LoginReq;
 */
public class Packet {


	private byte[] buf = null;

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
		// iLen = buf.length;

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
}
