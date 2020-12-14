package com.aca.nwsupdater.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NWSAlert {
	@JsonProperty("properties")
	private AlertProperties properties;

	public NWSAlert() {
	}

	public NWSAlert(AlertProperties properties) {
		this.properties = properties;
	}

	public AlertProperties getProperties() {
		return properties;
	}

	public void setProperties(AlertProperties properties) {
		this.properties = properties;
	}
}
