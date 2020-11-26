package com.aca.nwsupdater.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForecastProperties {
	@JsonProperty("periods")
	private List<Periods> periods;
	
	public List<Periods> getPeriod(){
		return periods;
	}
	
	@Override
	public String toString() {
		String value = null;
		for(int i = 0; i < periods.size(); i++) {
			value += periods.get(i) + "\n\n\n\n\n";
		}
		
		return value;
	}
}
