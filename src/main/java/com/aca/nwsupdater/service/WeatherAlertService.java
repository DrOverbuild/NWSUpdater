package com.aca.nwsupdater.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.aca.nwsupdater.model.AlertProperties;
import com.aca.nwsupdater.model.NWSAlert;
import com.aca.nwsupdater.model.WeatherAlertData;
import com.aca.nwsupdater.model.nws.Point;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherAlertService {
	private final String protocol = "https://";
	private final String domain = "api.weather.gov";
	private final String activeAlerts = "/alerts/active";
	private final String singleAlert = "/alerts/";
	private final String pointEndpoint = "/points/";

	public NWSAlert getAlertById(String id) {
		NWSAlert alert = null;
		String jsonString = start(getSingleAlertURL(id));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			alert = objectMapper.readValue(jsonString, NWSAlert.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return alert;
	}
	
	public WeatherAlertData getWeatherAlertData(String point) {
		WeatherAlertData weatherAlertData = null;
		String jsonString = start(getAlertsURL(point));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			weatherAlertData = objectMapper.readValue(jsonString, WeatherAlertData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return weatherAlertData;
	}

	public Point getPoint(String point) {
		Point pointData = null;
		String jsonString = start(getPointURL(point));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			pointData = objectMapper.readValue(jsonString, Point.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return pointData;
	}

	public String start(String request) {
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
	
	private String getAlertsURL(String point) {
		StringBuffer myRequest = new StringBuffer();
		
		myRequest.append(protocol);
		myRequest.append(domain);
		myRequest.append(activeAlerts);
		myRequest.append("?");
		myRequest.append("point=" + point);
		
		System.out.println(myRequest);
		
		return myRequest.toString();
	}

	private String getSingleAlertURL(String id) {
		StringBuilder builder = new StringBuilder(protocol);
		builder.append(domain).append(singleAlert).append(id);
		System.out.println(builder.toString());
		return builder.toString();
	}

	private String getPointURL(String point) {
		StringBuilder builder = new StringBuilder(protocol);
		builder.append(domain).append(pointEndpoint).append(point);
		return builder.toString();
	}
}

