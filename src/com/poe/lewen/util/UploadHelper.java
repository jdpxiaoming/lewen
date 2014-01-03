package com.poe.lewen.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.poe.lewen.MyApplication;


public class UploadHelper {

	private static final long TICKS_AT_EPOCH = 621355968000000000L;
	private static final long TICKS_PER_MILLISECOND = 10000;
	private static TimeZone timeZone = TimeZone.getDefault();
	private static final String TAG = "UploadHelper";
	private static String MULTIPART_FROM_DATA = "multipart/form-data";
	private static InputStream inputStream;

	// support single upload and batch upload mode, just organize your contents
	// into xml string
	public static String uploadXML(String host, String XMLContent) {
		//Log.d("responseText", XMLContent);
		// test url
		// http://10.0.44.56:8001/SyncService/post.aspx?account_no=grace&acc_pwd=123456&data_type=UploadQuestion
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		HttpPost httpPost = new HttpPost(host);
		
//		StringEntity entity = null;
		String the_string_response = null;
//		try {
//			entity = new StringEntity(XMLContent, "UTF-8");
//			entity.setContentType("text/xml; charset=UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
		// FileEntity entity = new FileEntity(new File("/sdcard/problem.xml"),
		// "text/xml; charset=\"UTF-8\"");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("identifier", MyApplication.getPreferenceData("token")));
		params.add(new BasicNameValuePair("xml", XMLContent));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response = client.execute(httpPost);
			the_string_response = convertResponseToString(response);
			//Log.i("imagine", the_string_response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return the_string_response;
	}

	public static String getPost(String host, String XMLContent){
		String result =null;
		try {
            final String SERVER_URL = host;//"http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather"; // 定义需要获取的内容来源地址
            HttpPost request = new HttpPost(SERVER_URL); // 根据内容来源地址创建一个Http请求
            List params = new ArrayList();
            params.add(new BasicNameValuePair("theCityCode", "长沙")); // 添加必须的参数
            params.add(new BasicNameValuePair("theUserID", "")); // 添加必须的参数
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); // 设置参数的编码
            HttpResponse httpResponse = new DefaultHttpClient().execute(request); // 发送请求并获取反馈
            // 解析返回的内容
            if (httpResponse.getStatusLine().getStatusCode() != 404)
            {
                    result = EntityUtils.toString(httpResponse.getEntity());
                    System.out.println(result);
            }
    } catch (Exception e) {
    }
    return result;
	}
	public static InputStream uploadXMLInputStreamResponse(String host,
			String XMLContent) {
		// test url
		// http://10.0.44.56:8001/SyncService/post.aspx?account_no=grace&acc_pwd=123456&data_type=UploadQuestion
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(host);
		StringEntity entity = null;
		try {
			entity = new StringEntity(XMLContent);
			entity.setContentType("text/xml");
			entity.setContentEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// FileEntity entity = new FileEntity(new File("/sdcard/problem.xml"),
		// "text/xml; charset=\"UTF-8\"");
		httpPost.setEntity(entity);
		try {
			HttpResponse response = client.execute(httpPost);
			return response.getEntity().getContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传图片
	 * 
	 * @param host
	 *            图片上传地址
	 * @param file
	 *            要上传的图片文件
	 * @return
	 */
//	public static String uploadImage(String host, File file) {
//		// test url
//		// http://10.0.44.56:8001/SyncService/PostFile.aspx?PicType=QuestionPic
//		String responseStr = null;
//
//		// 要上传的文件
//		String saveName = file.getName();
//
//		// 时间戳
//		String strBoundary = "----------"
//				+ Long.toHexString(getCShapeTicks(new Date()));
//
//		byte[] boundaryBytes = ("\r\n--" + strBoundary + "\r\n").getBytes();
//		long dataLength = file.length() + boundaryBytes.length;
//
//		// 请求头部信息
//		StringBuilder sb = new StringBuilder();
//		sb.append("--");
//		sb.append(strBoundary);
//		sb.append("\r\n");
//		sb.append("Content-Disposition: form-data; name=\"");
//		sb.append("file");
//		sb.append("\"; filename=\"");
//		sb.append(saveName);
//		sb.append("\"");
//		sb.append("\r\n");
//		sb.append("Content-Length: ");
//		sb.append(dataLength);
//		sb.append("\r\n");
//		sb.append("Content-Type: ");
//		sb.append("application/octet-stream");
//		sb.append("\r\n");
//		sb.append("\r\n");
//
//		byte[] postHeaderBytes = sb.toString().getBytes();
//
//		URL uri;
//		HttpURLConnection conn;
//		try {
//			uri = new URL(host);
//			conn = (HttpURLConnection) uri.openConnection();
//			conn.setReadTimeout(30 * 1000);
//			conn.setDoInput(true);
//			conn.setDoOutput(true);
//			conn.setUseCaches(false);
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("connection", "keep-alive");
//			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
//					+ ";boundary=" + strBoundary);
//			DataOutputStream outStream = new DataOutputStream(
//					conn.getOutputStream());
//			outStream.write(postHeaderBytes);
//
//			// Send the file data, maybe some attachment
//			InputStream is = new FileInputStream(file);
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while ((len = is.read(buffer)) != -1) {
//				outStream.write(buffer, 0, len);
//			}
//
//			is.close();
//			outStream.write(boundaryBytes);
//			outStream.flush();
//
//			byte[] data = new byte[512];
//			int resLen = 0;
//			StringBuffer responseBuffer = new StringBuffer();
//			while (-1 != (resLen = conn.getInputStream().read(data))) {
//				responseBuffer.append(new String(data, 0, resLen));
//			}
//			Log.d(TAG, responseBuffer.toString());
//			responseStr = responseBuffer.toString();
//			if (responseStr.indexOf(Constants.SUCCESS) != -1) {
//				Log.i(TAG, "图片上传成功");
//				return Constants.SUCCESS;
//			}
//			outStream.close();
//			conn.disconnect();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return responseStr;
//
//	}

	/**
	 * 下载图片
	 * 
	 * @param host
	 * @param type
	 *            图片类型 问题图片: QuestionPic ；整改图片: FixPic ；验证图片: ValidPic ；巡查图片:
	 *            InspectionPic
	 * @param name
	 *            图片名字
	 */
//	public static void downloadPic(String host, String type, String name) {
//
//		URL url;
//		StringBuffer hostAddr = new StringBuffer();
//		try {
//			// sample url
//			// url = new
//			// URL("http://10.0.44.56:8001/SyncService/RequestFile.aspx?file_type=QuestionPic&file_name=Jellyfish.jpg");
//
//			if (!Utils.isEmpty(host) && !Utils.isEmpty(type)
//					&& !Utils.isEmpty(name))
//				hostAddr = hostAddr.append(host).append("?")
//						.append("file_type=").append(type)
//						.append("&file_name=").append(name);
//			else
//				return;
//			url = new URL(hostAddr.toString());
//			//Log.d("url", hostAddr.toString());
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setConnectTimeout(5 * 1000);
//			InputStream inStream = conn.getInputStream();
//			if (conn.getHeaderField("Content-Disposition") == null)
//				return;
//			File f = null;
//			if (type.equalsIgnoreCase("QuestionPic"))
//				f = new File(ImageUtils.PROBLEM_PHOTO_DIR + "/" + name);
//			if (type.equalsIgnoreCase("FixPic"))
//				f = new File(ImageUtils.FIX_PHOTO_DIR + "/" + name);
//			if (type.equalsIgnoreCase("VerifyPic"))
//				f = new File(ImageUtils.VERIFY_PHOTO_DIR + "/" + name);
//			if (type.equalsIgnoreCase("InspectionPic"))
//				f = new File(ImageUtils.INSPECTION_PHOTO_DIR + "/" + name);
//			FileOutputStream outStream = new FileOutputStream(f);
//
//			byte[] buffer = new byte[1024];
//			int len = 0;
//			while ((len = inStream.read(buffer)) != -1) {
//				outStream.write(buffer, 0, len);
//			}
//			outStream.flush();
//			outStream.close();
//			inStream.close();
//			conn.disconnect();
//			//Log.d("url", f.getAbsolutePath());
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

	/**
	 * 将Java日期对象转换成C#的ticks值
	 * 
	 * @param jDate
	 * @return
	 */
	public static long getCShapeTicks(Date jDate) {
		long result = -1;
		try {
			Calendar calendar = Calendar.getInstance(timeZone);
			calendar.setTime(jDate);
			return (calendar.getTimeInMillis() + calendar.getTimeZone()
					.getRawOffset()) * TICKS_PER_MILLISECOND + TICKS_AT_EPOCH;
		} catch (Exception e) {

		}
		return result;
	}

	/*
	 * convert HttpResponse to String
	 */
	public static String convertResponseToString(HttpResponse response)
			throws IllegalStateException, IOException {

		String res = "";
		StringBuffer buffer = new StringBuffer();
		inputStream = response.getEntity().getContent();
		int contentLength = (int) response.getEntity().getContentLength();
		if (contentLength < 0) {
		} else {
			byte[] data = new byte[512];
			int len = 0;
			try {
				while (-1 != (len = inputStream.read(data))) {
					buffer.append(new String(data, 0, len));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				inputStream.close(); // closing the stream…..
			} catch (IOException e) {
				e.printStackTrace();
			}
			res = buffer.toString(); // converting stringbuffer to string…..
		}
		return res;
	}

	public static String convertResponseToString(InputStream inStream)
			throws IllegalStateException, IOException {

		String res = "";
		StringBuffer buffer = new StringBuffer();

		byte[] data = new byte[512];
		int len = 0;
		try {
			while (-1 != (len = inputStream.read(data))) {
				buffer.append(new String(data, 0, len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inputStream.close(); // closing the stream…..
		} catch (IOException e) {
			e.printStackTrace();
		}
		res = buffer.toString(); // converting stringbuffer to string…..

		return res;
	}
}
