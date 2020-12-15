package com.aca.nwsupdater.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortenedURL {
	@JsonProperty("result_url")
	private String result;

	public ShortenedURL() {
	}

	public ShortenedURL(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
