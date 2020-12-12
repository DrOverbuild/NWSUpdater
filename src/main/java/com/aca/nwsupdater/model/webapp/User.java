package com.aca.nwsupdater.model.webapp;

public class User {
	private int id;
	private String email;
	private String phone;
	private String password; // only used when browser logs in, we will not store password retrieved from database
	private String subscriptionArnPhone;
	private String subscriptionArnEmail;

	public User() {
	}

	public User(int id, String email, String phone) {
		this.id = id;
		this.email = email;
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubscriptionArnPhone() {
		return subscriptionArnPhone;
	}

	public void setSubscriptionArnPhone(String subscriptionArnPhone) {
		this.subscriptionArnPhone = subscriptionArnPhone;
	}

	public String getSubscriptionArnEmail() {
		return subscriptionArnEmail;
	}

	public void setSubscriptionArnEmail(String subscriptionArnEmail) {
		this.subscriptionArnEmail = subscriptionArnEmail;
	}
}
