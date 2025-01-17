package com.aca.nwsupdater.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlertProperties {
	@JsonProperty("id")
	private String id;
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
	@JsonProperty("effective")
	private String effective;
	@JsonProperty("expires")
	private String expires;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAreaDesc() {
		return areaDesc;
	}
	
	public void setAreaDesc(String areaDesc) {
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
