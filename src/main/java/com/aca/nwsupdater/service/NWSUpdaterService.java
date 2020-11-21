package com.aca.nwsupdater.service;

import com.aca.nwsupdater.dao.NWSUpdaterDAO;
import com.aca.nwsupdater.dao.NWSUpdaterDAOImpl;
import com.aca.nwsupdater.model.webapp.HomePageModel;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;

import java.util.List;

public class NWSUpdaterService {
	private SessionManager sessionManager;
	private NWSUpdaterDAO dao;

	public NWSUpdaterService() {
		dao = new NWSUpdaterDAOImpl();
		sessionManager = new SessionManager();
		sessionManager.start();
	}

	private String validateAuth(String auth) {
		if (auth == null) {
			ServiceUtil.sendError(1, "Not Logged In");
		}

		String[] components = auth.split(" ");

		if (components.length != 2) {
			ServiceUtil.sendError(1, "Not Logged In");
		}

		return components[1];
	}

	public HomePageModel homePage(String auth) {
		String token = validateAuth(auth);
		int userId = sessionManager.getSession(token);

		if (userId == -1) {
			ServiceUtil.sendError(1, "Not Logged In");
		}

		User user = dao.getUser(userId);
		List<Location> locations = dao.getLocations(userId);

		return new HomePageModel(locations, user, token);
	}
}
