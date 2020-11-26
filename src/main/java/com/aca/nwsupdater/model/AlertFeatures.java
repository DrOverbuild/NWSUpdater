package com.aca.nwsupdater.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlertFeatures {
	
	@JsonProperty("properties")
	private AlertProperties properties;

	public AlertProperties getProperties() {
		return properties;
	}

	public void setProperties(AlertProperties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		String value = "\nProperties: " + properties.toString();
		
		return value;
	}
}
