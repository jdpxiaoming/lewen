package com.poe.lewen.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Common {

	public static void saveFile(String fileName, byte[] data) {
		try {
			FileOutputStream out = new FileOutputStream(fileName,true);
			out.write(data);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *  方法：打开指定文件，读取其数据，返回字符串对象
	 * @param fileName
	 * @return
	 */
	public static String readFileData(String fileName) {
		String result = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			// 获得FileInputStream对象
			int length = fin.available();// 获取文件长度
			byte[] buffer = new byte[length];// 创建byte数组用于读入数据
			fin.read(buffer);// 将文件内容读入到byte数组中
			// result = EncodingUtils.getString(buffer);//将byte数组转换成指定格式的字符串
			for (byte b : buffer) {
				result += b;
			}
			fin.close(); // 关闭文件输入流
		} catch (Exception e) {
			e.printStackTrace();// 捕获异常并打印
		}
		return result;// 返回读到的数据字符串
	}

	/**
	 * 语音byte[]
	 * @param fileName
	 * @return
	 */
	public static byte[] readFileDataBytes(String fileName) {
		byte[] buffer = null;
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();// 获取文件长度
			buffer = new byte[length];// 创建byte数组用于读入数据
			fin.read(buffer);// 将文件内容读入到byte数组中
			fin.close(); // 关闭文件输入流
		} catch (Exception e) {
			e.printStackTrace();// 捕获异常并打印
		}
		return buffer;
	}

	// 字节序转换
	public static byte[] toLH(int n) {

		byte[] b = new byte[4];

		b[0] = (byte) (n & 0xff);

		b[1] = (byte) (n >> 8 & 0xff);

		b[2] = (byte) (n >> 16 & 0xff);

		b[3] = (byte) (n >> 24 & 0xff);

		return b;

	}

	// btye数组转换为int型

	public static int bytes2Integer(byte[] byteVal) {

		int result = 0;

		for (int i = 0; i < byteVal.length; i++) {

			int tmpVal = (byteVal[i] << (8 * (3 - i)));

			switch (i) {

			case 0:

				tmpVal = tmpVal & 0xFF000000;

				break;

			case 1:

				tmpVal = tmpVal & 0x00FF0000;

				break;

			case 2:

				tmpVal = tmpVal & 0x0000FF00;

				break;

			case 3:

				tmpVal = tmpVal & 0x000000FF;

				break;

			}

			result = result | tmpVal;

		}

		return result;
	}
}
