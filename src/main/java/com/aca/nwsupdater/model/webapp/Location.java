package com.aca.nwsupdater.model.webapp;

import java.util.List;

public class Location {
	private Integer id;
	private String name;
	private Double lat;
	private Double lon;
	private Boolean smsEnabled;
	private Boolean emailEnabled;
	private List<Alert> alerts;
	private Integer ownerID;
	private String topicArn;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Boolean getSmsEnabled() {
		return smsEnabled;
	}

	public void setSmsEnabled(Boolean smsEnabled) {
		this.smsEnabled = smsEnabled;
	}

	public Boolean getEmailEnabled() {
		return emailEnabled;
	}

	public void setEmailEnabled(Boolean emailEnabled) {
		this.emailEnabled = emailEnabled;
	}

	public List<Alert> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}

	public Integer getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Integer ownerID) {
		this.ownerID = ownerID;
	}
	
	@Override
	public String toString() {
		String toReturn;
		
		toReturn = "\nName: " + name + 
					"\nLongitude: " + lon +
					"\nLatitude: " + lat +
					"\nsmsEnabled: " + smsEnabled +
					"\nemailEnabled: " + emailEnabled;
		
		
		return toReturn;
	}

	public String getTopicArn() {
		return topicArn;
	}

	public void setTopicArn(String topicArn) {
		this.topicArn = topicArn;
	}
}
