package org.haoai.medixhub.ctkb.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	//final static HttpHost proxy = new HttpHost("sg5.cumc.columbia.edu", 8080, "HTTP");
	final static String auth="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZW1vIiwiZXhwIjoxNTM5MTAyMzg1fQ.-h73MOqE3X0V2FaAOah6XAV1JLgDOYlf1X7m73b-V6Fp9APxQMsgc95OdSTUalIoUABRgb94xg9182dGsTAQyQ";
	public static String doPost(String url, String content) {
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type", "application/json");
			httppost.setHeader("Authorization", auth);
			StringEntity se = new StringEntity(content);
			httppost.setEntity(se);
			//HttpResponse httpresponse = new DefaultHttpClient().execute(proxy,httppost);
			HttpResponse httpresponse = new DefaultHttpClient().execute(httppost);
			System.out.println(httpresponse.getStatusLine().getStatusCode());
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpresponse.getEntity());
				return strResult;
			} else {
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public static String doGet(String url) {
		try {
			
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Authorization", auth);
			//HttpResponse httpresponse = new DefaultHttpClient().execute(proxy,httpget);
			HttpResponse httpresponse = new DefaultHttpClient().execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpresponse.getEntity());
				return strResult;
			} else {
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}

	}

	public static void doPut(String urlstr,String json) {
		try {
			//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("sg5.cumc.columbia.edu", 8080));
	        URL url = new URL(urlstr);
	        //HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("PUT");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Authorization", auth);
	        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
	        osw.write(json);
	        osw.flush();
	        osw.close();
	        System.err.println(connection.getResponseCode());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}
	}
	
	public static void doDelete(String urlstr,String json) {
		try {
			//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("sg5.cumc.columbia.edu", 8080));
	        URL url = new URL(urlstr);
	        System.out.println("delete!!!");
	        //HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("DELETE");
	        connection.setRequestProperty("Authorization", auth);
	        connection.setDoOutput(true);
//	        connection.setRequestProperty("Content-Type", "application/json");
//	        connection.setRequestProperty("Accept", "application/json");
	        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
	        osw.write(json);
	        osw.flush();
	        osw.close();
	        System.out.println("code="+connection.getResponseCode());
	        System.err.println(connection.getResponseCode());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}
	}
	
	
}
