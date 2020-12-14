package com.aca.nwsupdater.model.nws;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Point {
	@JsonProperty("properties")
	private PointProperties properties;

	@JsonProperty("geometry")
	private Geometry geometry;

	public Point() {
	}

	public Point(PointProperties properties, Geometry geometry) {
		this.properties = properties;
		this.geometry = geometry;
	}

	public PointProperties getProperties() {
		return properties;
	}

	public void setProperties(PointProperties properties) {
		this.properties = properties;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}
