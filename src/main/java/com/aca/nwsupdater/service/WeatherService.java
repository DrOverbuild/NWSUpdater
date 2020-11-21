package com.aca.nwsupdater.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.aca.nwsupdater.model.WeatherData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherService {
	private final String protocol = "https://";
	private final String domain = "api.weather.gov";
	private final String resource = "/alerts/active";
	
	public WeatherData getWeatherData(String point) {
		WeatherData weatherData = null;
		String jsonString = start(point);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {
			weatherData = objectMapper.readValue(jsonString, WeatherData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return weatherData;
	}
	
	public String start(String point) {
		String request = getRequestURL(point);
		int responseCode = 0;
		String responseBody = "";
		
		try {
			URL url = new URL(request);
			HttpURLConnection con = getConnection(url);
			responseCode = con.getResponseCode();
			responseBody = getResponseBody(con);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return responseBody;
	}
	
	private HttpURLConnection getConnection(URL requestUrl) throws IOException{
		HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
		return con;
	}
	
	private String getResponseBody(HttpURLConnection con) throws IOException {
		
		BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
		String inputLine = null;
		StringBuffer response = new StringBuffer();
		
		while((inputLine = in.readLine()) != null){
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
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
