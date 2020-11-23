package com.aca.nwsupdater.service;

import com.aca.nwsupdater.dao.NWSUpdaterDAO;
import com.aca.nwsupdater.dao.NWSUpdaterDAOImpl;
import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.HomePageModel;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;

import java.util.List;

public class NWSUpdaterService {
	public static final NWSUpdaterService instance = new NWSUpdaterService();

	private SessionManager sessionManager;
	private NWSUpdaterDAO dao;
	private Validator validator;

	public NWSUpdaterService() {
		dao = new NWSUpdaterDAOImpl();
		sessionManager = new SessionManager();
		validator = new Validator(this);
		sessionManager.start();
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public NWSUpdaterDAO getDao() {
		return dao;
	}

	public Validator getValidator() {
		return validator;
	}

	public HomePageModel homePage(String auth) {
		String token = validator.validateAuth(auth);

		User user = dao.getUser(validator.validateToken(token));

		if (user == null) {
			return null;
		}

		List<Location> locations = dao.getLocations(user.getId());

		return new HomePageModel(locations, user, token);
	}

	public User getUser(String auth) {
		User user;

		int id = validator.validateToken(validator.validateAuth(auth));
		user = dao.getUser(id);

		return user;
	}

	public User updateUser(String auth, User user) {
		int userId = validator.validateToken(validator.validateAuth(auth));
		validator.validateUserId(userId);

		user.setId(userId); // prevent request from updating user with a different user ID

		validator.validateUserEmail(user.getEmail());
		validator.validateUserPhone(user.getPhone());

		if (user.getPassword() != null) {
			validator.validateUserPassword(user.getPassword());
		}

		return dao.updateUser(user);
	}

	public User createUser(User user) {
		validator.validateUserEmail(user.getEmail());
		validator.validateUserPhone(user.getPhone());
		validator.validateUserPassword(user.getPassword());

		User newUser = dao.createAccount(user);

		if (newUser == null) {
			ServiceUtil.sendError(8, "Email already exists.");
		}

		return newUser;
	}

	public Session authenticate(User loginInfo) {
		if (loginInfo == null) {
			ServiceUtil.sendError(7, "Login information not provided.");
		}

		validator.validateUserEmail(loginInfo.getEmail());
		validator.validateUserPassword(loginInfo.getPassword());

		User user = dao.authenticate(loginInfo);

		if (user != null) {
			return sessionManager.newSession(user.getId());
		} else {
			ServiceUtil.sendError(5, "Username or password incorrect.");
		}

		return null;
	}

	public void logout(String auth) {
		String token = validator.validateAuth(auth);
		sessionManager.removeSession(token);
	}

	public Location getLocation(String auth, Integer locationId) {
		int userId = validator.validateToken(validator.validateAuth(auth));

		return dao.locationById(locationId, userId);
	}

	public Location updateLocation(String auth, Location location) {
		int userId = validator.validateToken(validator.validateAuth(auth));

		if (location == null) {
			ServiceUtil.sendError(10, "Location could not be read from JSON data.");
		}

		location.setOwnerID(userId);

		return dao.updateLocation(location);
	}


	public Location newLocation(String auth, Location location) {
		int userId = validator.validateToken(validator.validateAuth(auth));

		if (location == null) {
			ServiceUtil.sendError(10, "Location could not be read from JSON data.");
		}

		location.setOwnerID(userId);
		return dao.addLocation(location);
	}

	public List<Location> deleteLocation(String auth, Integer locationId) {
		int userId = validator.validateToken(validator.validateAuth(auth));

		Location loc = dao.locationById(locationId, userId);

		if (loc == null) {
			ServiceUtil.sendError(6, "Location does not exist.");
		}

		return dao.deleteLocation(loc);
	}

	public List<Alert> getAlerts() {
		return dao.getAlerts();
	}
}