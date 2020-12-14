package com.aca.nwsupdater.model.nws;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Point {
	@JsonProperty("properties")
	private PointProperties properties;

	public Point() {
	}

	public Point(PointProperties properties) {
		this.properties = properties;
	}

	public PointProperties getProperties() {
		return properties;
	}

	public void setProperties(PointProperties properties) {
		this.properties = properties;
	}
}
