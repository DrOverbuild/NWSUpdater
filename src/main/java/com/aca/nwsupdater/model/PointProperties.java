package com.aca.nwsupdater.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PointProperties {
	@JsonProperty("forecast")
	private String forecast;
	
	public String getForecast() {
		return forecast;
	}
	
	public void setForecast(String forecast) {
		this.forecast = forecast;
	}
	
	@Override
	public String toString() {
		String value = "\nforcast: " + forecast;
		return value;
	}
}
