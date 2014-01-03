package com.poe.lewen.util;

public class Common {
	  //字节序转换

    public static byte[] toLH(int n) {

        byte[] b =new byte[4];

        b[0]= (byte)(n & 0xff);

        b[1]= (byte)(n >> 8 & 0xff);

        b[2]= (byte)(n >> 16 & 0xff);

        b[3]= (byte)(n >> 24 & 0xff);

        return b;

    }

    //btye数组转换为int型

    public static int bytes2Integer(byte[] byteVal) {

        int result = 0;

        for (int i = 0; i < byteVal.length; i++) {

            int tmpVal = (byteVal[i]<< (8 * (3 - i)));

            switch (i) {

            case 0:

                tmpVal= tmpVal & 0xFF000000;

                break;

            case 1:

                tmpVal= tmpVal & 0x00FF0000;

                break;

            case 2:

                tmpVal= tmpVal & 0x0000FF00;

                break;

            case 3:

                tmpVal= tmpVal & 0x000000FF;

                break;

            }

            result= result | tmpVal;

        }

        return result;

    }
}
