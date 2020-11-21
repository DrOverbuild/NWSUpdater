package com.aca.nwsupdater.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherData {
	@JsonProperty("features")
	private List<Features> features;

	@Override
	public String toString() {
		String value = null;
		for(int i = 0; i < features.size(); i++) {
			value += features.get(i) + "\n\n\n\n\n";
		}
		
		return value;
	}
}
