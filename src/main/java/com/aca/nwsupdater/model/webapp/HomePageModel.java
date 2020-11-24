package com.aca.nwsupdater.model.webapp;

import java.util.List;

public class HomePageModel {
	List<Location> locations;
	User user;
	String sessionToken;

	public HomePageModel() {
	}

	public HomePageModel(List<Location> locations, User user, String sessionToken) {
		this.locations = locations;
		this.user = user;
		this.sessionToken = sessionToken;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
