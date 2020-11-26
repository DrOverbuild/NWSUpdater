package com.aca.nwsupdater.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceUtils {
	public static HttpURLConnection getConnection(URL requestUrl) throws IOException{
		HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
		return con;
	}
	
	public static String getResponseBody(HttpURLConnection con) throws IOException {
		
		BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
		String inputLine = null;
		StringBuffer response = new StringBuffer();
		
		while((inputLine = in.readLine()) != null){
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
}
