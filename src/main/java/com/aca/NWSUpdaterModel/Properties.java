package com.aca.NWSUpdaterModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Properties {
	@JsonProperty("areaDesc")
	private String areaDesc;
	@JsonProperty("severity")
	private String severity;
	@JsonProperty("certainty")
	private String certainty;
	@JsonProperty("urgency")
	private String urgency;
	@JsonProperty("event")
	private String event;
	@JsonProperty("headline")
	private String headline;
	@JsonProperty("description")
	private String description;
	
	public String getAreaDesc() {
		return areaDesc;
	}
	
	public void setAreaDes(String areaDesc) {
		this.areaDesc = areaDesc;
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
