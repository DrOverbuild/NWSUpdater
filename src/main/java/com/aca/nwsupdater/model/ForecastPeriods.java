package com.aca.nwsupdater.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForecastPeriods {
	@JsonProperty("name")
	private String name;
	@JsonProperty("temperature")
	private int temperature;
	@JsonProperty("temperatureUnit")
	private String temperatureUnit;
	@JsonProperty("icon")
	private String icon;
	@JsonProperty("shortForecast")
	private String shortForecast;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public String getTemperatureUnit() {
		return temperatureUnit;
	}

	public void setTemperatureUnit(String temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getShortForecast() {
		return shortForecast;
	}

	public void setShortForecast(String shortForecast) {
		this.shortForecast = shortForecast;
	}
	
	@Override
	public String toString() {
		String value = "\nName: " + name +
				"\nTemperature: " + temperature +
				"\nTemperatureUnit: " + temperatureUnit +
				"\nIcon: " + icon +
				"\nShortForecast: " + shortForecast;
		
		return value;
	}
}
