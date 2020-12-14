package com.aca.nwsupdater.model.sns;

public class TopicSubscriber {
	private int locationID;
	private int userID;
	private String phoneArn;
	private String emailArn;
	private String topicArn;
	
	public int getLocationID() {
		return locationID;
	}
	
	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getPhoneArn() {
		return phoneArn;
	}

	public void setPhoneArn(String phoneArn) {
		this.phoneArn = phoneArn;
	}

	public String getEmailArn() {
		return emailArn;
	}

	public void setEmailArn(String emailArn) {
		this.emailArn = emailArn;
	}

	public String getTopicArn() {
		return topicArn;
	}

	public void setTopicArn(String topicArn) {
		this.topicArn = topicArn;
	}
	
}
