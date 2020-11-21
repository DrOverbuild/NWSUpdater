package com.aca.nwsupdater.service;

import com.aca.nwsupdater.model.webapp.User;

import java.util.UUID;

public class Session {
	UUID uuid;
	int userId;
	long lastRequest;

	public Session() {
	}

	public Session(UUID uuid, int userId, long lastRequest) {
		this.uuid = uuid;
		this.userId = userId;
		this.lastRequest = lastRequest;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public long getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(long lastRequest) {
		this.lastRequest = lastRequest;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
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

		return session.getUuid().equals(this.getUuid());
	}
}
