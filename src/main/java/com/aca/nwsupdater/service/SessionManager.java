package com.aca.nwsupdater.service;

import com.aca.nwsupdater.model.webapp.User;

import java.util.*;

public class SessionManager extends TimerTask {
	// 30 minute time out in milliseconds
	private static long SESSION_TIMEOUT = 1_800_000;

	private List<Session> sessions = new ArrayList<>();

	public String newSession(int userId) {
		UUID id = UUID.randomUUID();
		Session session = new Session(id, userId, System.currentTimeMillis());
		sessions.add(session);
		return id.toString();
	}

	/**
	 * Gets the session from the UUID, and if there is one, updates the session's last
	 * request to the current time
	 * @param uuid
	 * @return the user associated with the session, or null if there is no session
	 */
	public int getSession(String uuid) {
		UUID id = UUID.fromString(uuid);

		for (Session session: sessions) {
			if (session.getUuid().equals(id)) {
				session.setLastRequest(System.currentTimeMillis());
				return session.getUserId();
			}
		}

		return -1;
	}

	public void removeSession(String uuid) {
		Session session = new Session(UUID.fromString(uuid), 0, 0);
		sessions.remove(session);
	}

	public void start() {
		new Timer().schedule(this, 0, SESSION_TIMEOUT);
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		List<Session> remove = new ArrayList<>();

		for (Session session: sessions) {
			long totalTime = time - session.getLastRequest();
			if (totalTime > SESSION_TIMEOUT) {
				remove.add(session);
			}
		}

		sessions.removeAll(remove);
	}
}
