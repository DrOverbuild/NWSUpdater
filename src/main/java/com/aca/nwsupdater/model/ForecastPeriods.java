package com.aca.nwsupdater.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForecastPeriods {
	@JsonProperty("isDaytime")
	private Boolean isDayTime;
	
	@JsonProperty("detailedForecast")
	private String detailedForecast;

	public Boolean getIsDayTime() {
		return isDayTime;
	}

	public void setIsDayTime(Boolean isDayTime) {
		this.isDayTime = isDayTime;
	}

	public String getDetailedForecast() {
		return detailedForecast;
	}

	public void setDetailedForecast(String detailedForecast) {
		this.detailedForecast = detailedForecast;
	}

	@Override
	public String toString() {
		String value = "\nisDayTime: " + isDayTime +
				"\ndetailedForecast: " + detailedForecast;
		
		return value;
	}
}
