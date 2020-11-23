package com.aca.nwsupdater.service;

import java.util.UUID;

public class Session {
	UUID sessionID;
	Integer userId;
	Long lastRequest;

	public Session() {
	}

	public Session(UUID sessionID, Integer userId, Long lastRequest) {
		this.sessionID = sessionID;
		this.userId = userId;
		this.lastRequest = lastRequest;
	}

	public UUID getSessionID() {
		return sessionID;
	}

	public void setSessionID(UUID sessionID) {
		this.sessionID = sessionID;
	}

	public Long getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(Long lastRequest) {
		this.lastRequest = lastRequest;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Session)) {
			return false;
		}

		if (this == o) {
			return true;
		}

		Session session = (Session) o;

		return session.getSessionID().equals(this.getSessionID());
	}
}
