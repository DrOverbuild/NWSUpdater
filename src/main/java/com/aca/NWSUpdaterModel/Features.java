package com.aca.NWSUpdaterModel;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Features {
	
	@JsonProperty("properties")
	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		String value = "\nProperties: " + properties.toString();
		
		return value;
	}
}
