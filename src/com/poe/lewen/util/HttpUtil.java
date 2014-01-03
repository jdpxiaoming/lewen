package com.poe.lewen.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import struct.JavaStruct;
import struct.StructException;

import com.poe.lewen.MyApplication;
import com.poe.lewen.bean.Constant;
import com.poe.lewen.bean.tagLoginReq;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpUtil {

	private static String TAG = "HttpUtil";
	public static boolean isWap = false;
	private static int ONE_SECOND = 1000;


	public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		MyApplication.getInstance().NoNetWorkTips();

		return false;
	}

	/**
	 * 
	 * @param params
	 * @param strUrl
	 *            服务器访问地址
	 * @return
	 */
	public static String sendGetRequest(HashMap<String, Object> params,String strUrl) {
		
		String result = null;
		BufferedReader br = null;
		InputStream instream = null;
		
		try {
			
			StringBuilder paraUrl = new StringBuilder(strUrl);
			//2013-3-14添加
//			paraUrl.append("db="+CRMApplication.getDB());
			
			int index = 0;
			
			if (params != null && !params.isEmpty()) {
				
				for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
					
					Map.Entry e = (Map.Entry) it.next();
					paraUrl.append("&");
					paraUrl.append(e.getKey().toString());
					paraUrl.append("=");
					
					if (e.getValue() != null)
						paraUrl.append(URLEncoder.encode(e.getValue().toString(), "utf-8"));
					
					index++;
					
				}
			}
			
			instream = getInputsteamByAppache(paraUrl.toString());

			if (instream == null) {

				instream = getInputsteamByAppache(paraUrl.toString());
			}

			br = new BufferedReader(new InputStreamReader(instream));

			StringBuilder sBuilder = new StringBuilder();
			String line = "";

			while ((line = br.readLine()) != null) {

				sBuilder.append(line);

			}
			String temp = sBuilder.toString();
			Log.d(TAG, "response=" + temp);

			result = temp;

		} catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			if (null != e) {
				if (e.getMessage() != null) {
					if (e != null && null != e.getMessage())
						Log.d("HTTP", e.getMessage());
				}
			}
		} finally {
			try {
				if (null != instream)
					instream.close();
				if (null != br)
					br.close();
				// if(null != connection)
				// connection.disconnect();
			} catch (IOException ioe) {

				if (ioe != null && null != ioe.getMessage())
					Log.d("HTTP", ioe.getMessage());
			}
		}
		return result;
	}

	/**
	 * post获取 服务的方法
	 * 
	 * @param params
	 * @param strUrl 服务器地址+方法名
	 * @return xml 需要在对xml进行解析
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String PostRequest(HashMap<String, Object> params,
			String strUrl) throws ClientProtocolException, IOException {
		String result = null;

		HttpPost httpRequest = new HttpPost(strUrl);

		
		List<NameValuePair> params2 = new ArrayList<NameValuePair>();

		//2013-3-14添加
//		params2.add(new BasicNameValuePair("db",CRMApplication.getDB()));
		
		int index = 0;
		if (params != null && !params.isEmpty()) {
			for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				params2.add(new BasicNameValuePair(e.getKey().toString(), e
						.getValue().toString()));
				index++;
			}
		}

		httpRequest.setEntity(new UrlEncodedFormEntity(params2, HTTP.UTF_8));

		//设置连接超时 和 数据超时
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				3 * ONE_SECOND);
		HttpConnectionParams.setSoTimeout(client.getParams(), 5 * ONE_SECOND);
		
		HttpResponse response = client.execute(httpRequest);//new DefaultHttpClient().execute(httpRequest);

		if (response.getStatusLine().getStatusCode() == 200) {
			// 取出应答字符串
			String strResult = EntityUtils.toString(response.getEntity());
			result = strResult;
		}

		// 取出 结果集
		if (null != result) {
			if (result.indexOf("return") != -1) {
				/**
				 * <ns:updateClientResponse xmlns:ns="http://service">
				 * <ns:return
				 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				 * xsi:nil="true" /> </ns:updateClientResponse>
				 */
				if (result.indexOf("</ns:return>") != -1) {
					result = result.substring(
							result.indexOf("<ns:return>") + 11,
							result.indexOf("</ns:return>"));
				} else {// 没有明确的返回结果
					result = "fail:操作失败！";
				}
			}
		}

		return result;
	}

	/**
	 * 3次连接机会 第三次连不上就算了
	 * 
	 * @param conn
	 */
	public static void connectServer(HttpURLConnection conn) {
		boolean success = false;
		int count = 0;
		while (!success && count < 3) {
			success = true;
			try {
				conn.connect();
			} catch (java.net.SocketTimeoutException e) {
				// CRMApplication.throwTips("连接服务器失败，请联系管理员。");
			} catch (IOException e) {
				count++;
				success = false;
			}
		}
	}

	/**
	 * 以后所有的 输入流都采用这个 方法来获取 ，也可以 获取 字符串。
	 * 
	 * @param address
	 * @return
	 */
	public static InputStream getInputsteamByAppache(String address) {
		
		InputStream inStream = null;

		// 使用get请求
		HttpGet getRequest = new HttpGet(address);

		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				3 * ONE_SECOND);
		HttpConnectionParams.setSoTimeout(client.getParams(), 5 * ONE_SECOND);
		try {
			HttpResponse response = client.execute(getRequest);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				inStream = response.getEntity().getContent();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // 221.224.52.112:80
		}
		return inStream;
	}
	
	
	public static String login(String username , String passwd){
		
		String result  = null;
		tagLoginReq tr =new tagLoginReq();
		tr.loginIp = Constant.str_login_ip.toCharArray();
		tr.pcName = "nyPcName".toCharArray();
		tr.userType=0;
		tr.userName = username.toCharArray();
		tr.userPsw	=	Md5Util.getMD5Str(passwd).toCharArray();
		
		try {
			
			byte[] b =JavaStruct.pack(tr);
			
			Socket socket = new Socket(Constant.str_login_ip, Constant.login_port);
			
		    socket.getOutputStream().write(b);
		    
		    byte[] recvHead =new byte[4];
	        int cForm;
	        int cmd, length, para1,para2, para3;
	        // readcmd转换字节序

	        socket.getInputStream().read(recvHead,0, 4);

	        cForm= Common.bytes2Integer(recvHead);

	        cmd= Common.bytes2Integer(Common.toLH(cForm));

	 

	        // read length转换字节序

	        socket.getInputStream().read(recvHead,0, 4);

	        cForm= Common.bytes2Integer(recvHead);

	        length= Common.bytes2Integer(Common.toLH(cForm));

	 

	        // read para1转换字节序

	        socket.getInputStream().read(recvHead,0, 4);

	        cForm= Common.bytes2Integer(recvHead);

	        para1= Common.bytes2Integer(Common.toLH(cForm));

	 

	        // read para2转换字节序

	        socket.getInputStream().read(recvHead,0, 4);

	        cForm= Common.bytes2Integer(recvHead);

	        para2= Common.bytes2Integer(Common.toLH(cForm));

	 

	        // read para3转换字节序

	        socket.getInputStream().read(recvHead,0, 4);

	        cForm= Common.bytes2Integer(recvHead);

	        para3= Common.bytes2Integer(Common.toLH(cForm));

	 

	        byte[] recvData =new byte[length];

	 

	        // read data

	        socket.getInputStream().read(recvData,0, length);

	        String DataStr = new String(recvData);

	        System.out.println(DataStr);
		} catch (StructException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
            e.printStackTrace();
        }
		
		return result;
	}
}