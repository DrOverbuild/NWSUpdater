package com.aca.nwsupdater.model.sns;

public class SubscribeMessage {
	private String endpoint;
	private boolean isEmail;
	
	public boolean getIsEmail() {
		return isEmail;
	}
	public void setIsEmail(boolean isEmail) {
		this.isEmail = isEmail;
	}
	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
}
