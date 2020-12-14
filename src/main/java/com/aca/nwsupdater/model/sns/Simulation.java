package com.aca.nwsupdater.model.sns;

public class Simulation {
	private String cityName;
	private Double lat;
	private Double lon;
	private String areaDesc;
	private String severity;
	private String certainty;
	private String urgency;
	private String event;
	private String headline;
	private String description;
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}

	public String getAreaDesc() {
		return areaDesc;
	}
	
	public String getSeverity() {
		return severity;
	}
	
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	public String getCertainty() {
		return certainty;
	}
	
	public void setCertainty(String certainty) {
		this.certainty = certainty;
	}
	
	public String getUrgency() {
		return urgency;
	}
	
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	
	public String getEvent() {
		return event;
	}
	
	public void setEvent(String event) {
		this.event = event;
	}
	
	public String getHeadline() {
		return headline;
	}
	
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		String value = "\nAreaDesc: " + areaDesc + "\nSeverity: " + severity +
				"\nCertainty: " + certainty + "\nUrgency: " + urgency +
				"\nEvent: " + event + "\nHeadline: " + headline +
				"\nDescription: " + description;
		
		return value;
	}
}
