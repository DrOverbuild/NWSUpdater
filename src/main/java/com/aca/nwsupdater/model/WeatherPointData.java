package com.aca.nwsupdater.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherPointData {
	@JsonProperty("properties")
	private PointProperties properties;
	
	public PointProperties getProperties() {
		return properties;
	}
	
	public void setProperties(PointProperties properties) {
		this.properties = properties;
	}
	
	@Override
	public String toString() {
		String value = "\nProperties: " + properties;
		
		return value;
	}
}
