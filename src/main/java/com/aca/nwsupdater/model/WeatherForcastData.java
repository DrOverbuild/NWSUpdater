package com.aca.nwsupdater.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherForcastData {
	@JsonProperty("properties")
	private ForecastProperties properties;
	
	public ForecastProperties getProperties() {
		return properties;
	}

	public void setProperties(ForecastProperties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		String value = "Properties: " + properties;
		
		return value;
	}
}
