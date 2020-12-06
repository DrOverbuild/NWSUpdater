package com.aca.nwsupdater.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.aca.nwsupdater.model.WeatherForecastData;
import com.aca.nwsupdater.service.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherForecastService {
	private final String protocol = "https://";
	private final String domain = "api.weather.gov";
	private final String firstResource = "/gridpoints";
	private final String secondResource = "/forecast";
	
	public WeatherForecastData getWeatherForecastData(String wfo, String point) {
		WeatherForecastData weatherAlertData = null;
		String jsonString = start(wfo, point);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			weatherAlertData = objectMapper.readValue(jsonString, WeatherForecastData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return weatherAlertData;
	}
	
	public String start(String wfo, String point) {
		String request = getRequestURL(wfo, point);
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
	
	private String getRequestURL(String wfo, String point) {
		StringBuffer myRequest = new StringBuffer();
		
		myRequest.append(protocol);
		myRequest.append(domain);
		myRequest.append(firstResource);
		myRequest.append("/" + wfo);
		myRequest.append("/" + point);
		myRequest.append(secondResource);
		myRequest.append("?");
		myRequest.append("units=us");
		
		System.out.println(myRequest);
		
		return myRequest.toString();
	}
}