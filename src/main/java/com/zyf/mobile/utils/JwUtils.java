package com.zyf.mobile.utils;


import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class JwUtils {
	private static CloseableHttpClient httpClient = null;
	public final static String BASE_URL = "http://202.199.155.36/";
	
	public static CloseableHttpClient getHttpClient() {
		if (httpClient == null){
			httpClient = HttpClients.createDefault();
			return httpClient;
		}
		return httpClient;
	}
}
