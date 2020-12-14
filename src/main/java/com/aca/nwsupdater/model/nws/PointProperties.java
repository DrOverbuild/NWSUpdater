package com.aca.nwsupdater.model.nws;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PointProperties {
	@JsonProperty("cwa")
	private String cwa;

	public PointProperties() {
	}

	public PointProperties(String cwa) {
		this.cwa = cwa;
	}

	public String getCwa() {
		return cwa;
	}

	public void setCwa(String cwa) {
		this.cwa = cwa;
	}
}
