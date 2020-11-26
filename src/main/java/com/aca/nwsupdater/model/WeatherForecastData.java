package com.aca.nwsupdater.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherForecastData {
	@JsonProperty("properties")
	private ForecastProperties properties;
	
	public ForecastProperties getForecastProperties() {
		return properties;
	}

	public void setForecastProperties(ForecastProperties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		String value = "Properties: " + properties;
		
		return value;
	}
}
