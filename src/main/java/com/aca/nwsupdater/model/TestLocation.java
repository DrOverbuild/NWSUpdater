package com.aca.nwsupdater.model;

import java.util.List;

import com.aca.nwsupdater.model.webapp.Alert;

public class TestLocation {
	private String name;
	private Double lat;
	private Double lon;
	private Boolean enabledSMS;
	private Boolean enabledEmail;
	private Boolean tornadoWarning;
	private Boolean tornadoWatch;
	private Boolean severeThunderstormWarning;
	private Boolean severeThunderstormWatch;
	private Boolean fleshFloodWarning;
	private Boolean fleshFloodWatch;

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

	public Boolean getEnabledSMS() {
		return enabledSMS;
	}

	public void setEnabledSMS(Boolean enabledSMS) {
		this.enabledSMS = enabledSMS;
	}

	public Boolean getEnabledEmail() {
		return enabledEmail;
	}

	public void setEnabledEmail(Boolean enabledEmail) {
		this.enabledEmail = enabledEmail;
	}

	public Boolean getTornadoWarning() {
		return tornadoWarning;
	}

	public void setTornadoWarning(Boolean tornadoWarning) {
		this.tornadoWarning = tornadoWarning;
	}

	public Boolean getTornadoWatch() {
		return tornadoWatch;
	}

	public void setTornadoWatch(Boolean tornadoWatch) {
		this.tornadoWatch = tornadoWatch;
	}

	public Boolean getSevereThunderstormWarning() {
		return severeThunderstormWarning;
	}

	public void setSevereThunderstormWarning(Boolean severeThunderstormWarning) {
		this.severeThunderstormWarning = severeThunderstormWarning;
	}

	public Boolean getSevereThunderstormWatch() {
		return severeThunderstormWatch;
	}

	public void setSevereThunderstormWatch(Boolean severeThunderstormWatch) {
		this.severeThunderstormWatch = severeThunderstormWatch;
	}

	public Boolean getFleshFloodWarning() {
		return fleshFloodWarning;
	}

	public void setFleshFloodWarning(Boolean fleshFloodWarning) {
		this.fleshFloodWarning = fleshFloodWarning;
	}

	public Boolean getFleshFloodWatch() {
		return fleshFloodWatch;
	}

	public void setFleshFloodWatch(Boolean fleshFloodWatch) {
		this.fleshFloodWatch = fleshFloodWatch;
	}
	
	@Override
	public String toString() {
		String toReturn;
		
		toReturn = "\nName: " + name + 
					"\nLongitude: " + lon +
					"\nLatitude: " + lat +
					"\nsmsEnabled: " + enabledSMS +
					"\nemailEnabled: " + enabledEmail +
					"\ntornadoWarning: " + tornadoWarning +
					"\ntornadoWatch: " + tornadoWatch +
					"\nsevereThunderstormWarning: " + severeThunderstormWarning +
					"\nsevereThunderstormWatch: " + severeThunderstormWatch +
					"\nfleshFloodWarning: " + fleshFloodWarning +
					"\nfleshFloodWatch: " + fleshFloodWatch;

		return toReturn;
	}
}
