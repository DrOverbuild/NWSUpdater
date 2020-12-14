package com.aca.nwsupdater.service;

import com.aca.nwsupdater.model.ShortenedURL;
import com.aca.nwsupdater.model.nws.Point;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.net.*;
import java.io.IOException;

public class URLShortener {
	private static String requestURLString = "https://goolnk.com/api/v1/shorten";

	public static  String shortenURL(String urlToShorten) {
		try {
			String param = "url=" + URLEncoder.encode(urlToShorten,"UTF-8");
			URL url = new URL(requestURLString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-TYpe", "application/x-www-form-urlencoded");

			con.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(param);
			out.flush();
			out.close();

			int responseCode = con.getResponseCode();
			String responseBody = ServiceUtils.getResponseBody(con);
			con.disconnect();

			System.out.println(responseBody);
			return getUrlFromJSON(responseBody);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String getUrlFromJSON(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			ShortenedURL url = objectMapper.readValue(json, ShortenedURL.class);
			return url.getResult();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}
}