package com.aca.nwsupdater.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.aca.nwsupdater.model.webapp.ErrorMessage;
import com.aca.nwsupdater.service.sns.SnsClient;
import com.amazonaws.services.sns.model.UnsubscribeRequest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

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
	
	public static void sendError(int code, String msg) {
		ErrorMessage errmsg = new ErrorMessage(code, msg);
		Response response = Response.status(400)
				.entity(errmsg)
				.build();
		throw new WebApplicationException(response);
	}
}
