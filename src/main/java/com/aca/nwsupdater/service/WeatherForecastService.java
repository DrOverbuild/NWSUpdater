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
	
	public WeatherForecastData getWeatherForecastData(String forecastUrl) {
		WeatherForecastData weatherAlertData = null;
		String jsonString = start(forecastUrl);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			weatherAlertData = objectMapper.readValue(jsonString, WeatherForecastData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return weatherAlertData;
	}
	
	public String start(String forecastUrl) {
		int responseCode = 0;
		String responseBody = "";
		
		try {
			URL url = new URL(forecastUrl);
			HttpURLConnection con = ServiceUtils.getConnection(url);
			responseCode = con.getResponseCode();
			responseBody = ServiceUtils.getResponseBody(con);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return responseBody;
	}
	
}
