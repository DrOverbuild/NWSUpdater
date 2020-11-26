package com.aca.nwsupdater.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.aca.nwsupdater.model.WeatherAlertData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherAlertService {
	private final String protocol = "https://";
	private final String domain = "api.weather.gov";
	private final String resource = "/alerts/active";
	
	public WeatherAlertData getWeatherAlertData(String point) {
		WeatherAlertData weatherAlertData = null;
		String jsonString = start(point);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			weatherAlertData = objectMapper.readValue(jsonString, WeatherAlertData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return weatherAlertData;
	}
	
	public String start(String point) {
		String request = getRequestURL(point);
		int responseCode = 0;
		String responseBody = "";
		
		try {
			URL url = new URL(request);
			HttpURLConnection con = ServiceUtils.getConnection(url);
			responseCode = con.getResponseCode();
			responseBody = ServiceUtils.getResponseBody(con);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return responseBody;
	}
	
	private String getRequestURL(String point) {
		StringBuffer myRequest = new StringBuffer();
		
		myRequest.append(protocol);
		myRequest.append(domain);
		myRequest.append(resource);
		myRequest.append("?");
		myRequest.append("point=" + point);
		
		System.out.println(myRequest);
		
		return myRequest.toString();
	}
}

