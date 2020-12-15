package com.aca.nwsupdater.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.aca.nwsupdater.model.WeatherPointData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherPointService {
	private final String protocol = "https://";
	private final String domain = "api.weather.gov";
	private final String firstResource = "/points";
	
	public WeatherPointData getWeatherPointData(String point) {
		WeatherPointData weatherPointData = null;
		String jsonString = start(point);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			weatherPointData = objectMapper.readValue(jsonString, WeatherPointData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return weatherPointData;
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
		myRequest.append(firstResource);
		myRequest.append("/" + point);
		
		System.out.println(myRequest);
		
		return myRequest.toString();
	}
}
