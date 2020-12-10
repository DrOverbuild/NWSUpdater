package com.aca.nwsupdater.service;

import com.aca.nwsupdater.dao.NWSUpdaterDAO;
import com.aca.nwsupdater.dao.NWSUpdaterDAOImpl;
import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.HomePageModel;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.NewLocation;
import com.aca.nwsupdater.model.webapp.User;

import java.util.ArrayList;
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
			ServiceUtils.sendError(8, "Email already exists.");
		}

		return newUser;
	}

	public Session authenticate(User loginInfo) {
		if (loginInfo == null) {
			ServiceUtils.sendError(7, "Login information not provided.");
		}

		validator.validateUserEmail(loginInfo.getEmail());
		validator.validateUserPassword(loginInfo.getPassword());

		User user = dao.authenticate(loginInfo);

		if (user != null) {
			return sessionManager.newSession(user.getId());
		} else {
			ServiceUtils.sendError(5, "Username or password incorrect.");
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
			ServiceUtils.sendError(10, "Location could not be read from JSON data.");
		}

		location.setOwnerID(userId);

		return dao.updateLocation(location);
	}


	public Location newLocation(String auth, Location newLocation) {
		Location location = new Location();
		
		int userId = validator.validateToken(validator.validateAuth(auth));

		if (newLocation == null) {
			ServiceUtils.sendError(10, "Location could not be read from JSON data.");
		}

//		setLocation(newLocation, location);
		location.setOwnerID(userId);
		return dao.addLocation(newLocation);
	}

	private void setLocation(NewLocation newLocation, Location location) {
		List<Alert> alerts = new ArrayList<>();
		
		if(newLocation.getTornadoWarning()) {
			Alert tornadoWarning = new Alert();
			tornadoWarning.setId(1);
			tornadoWarning.setName("Tornado Warning");
			alerts.add(tornadoWarning);
		}
		if(newLocation.getTornadoWatch()) {
			Alert tornadoWatch = new Alert();
			tornadoWatch.setId(2);
			tornadoWatch.setName("Tornado Watch");
			alerts.add(tornadoWatch);
		}
		if(newLocation.getSevereThunderstormWarning()) {
			Alert severeThunderstormWarning = new Alert();
			severeThunderstormWarning.setId(3);
			severeThunderstormWarning.setName("Severe Thunderstorm Warning");
			alerts.add(severeThunderstormWarning);
		}		
		if(newLocation.getSevereThunderstormWatch()) {
			Alert severeThunderstormWatch = new Alert();
			severeThunderstormWatch.setId(4);
			severeThunderstormWatch.setName("Severe Thunderstorm Watch");
			alerts.add(severeThunderstormWatch);
		}
		if(newLocation.getFleshFloodWarning()) {
			Alert fleshFloodWarning = new Alert();
			fleshFloodWarning.setId(5);
			fleshFloodWarning.setName("Flesh Flood Warning");
			alerts.add(fleshFloodWarning);
		}
		if(newLocation.getFleshFloodWatch()) {
			Alert fleshFloodWatch = new Alert();
			fleshFloodWatch.setId(6);
			fleshFloodWatch.setName("Flesh Flood Watch");
			alerts.add(fleshFloodWatch);
		}
		
		location.setName(newLocation.getName());
		location.setLat(newLocation.getLat());
		location.setLon(newLocation.getLon());
		location.setEmailEnabled(newLocation.getEnabledEmail());
		location.setSmsEnabled(newLocation.getEnabledSMS());
		location.setAlerts(alerts);
	}

	public List<Location> deleteLocation(String auth, Integer locationId) {
		int userId = validator.validateToken(validator.validateAuth(auth));

		Location loc = dao.locationById(locationId, userId);

		if (loc == null) {
			ServiceUtils.sendError(6, "Location does not exist.");
		}

		return dao.deleteLocation(loc);
	}

	public List<Alert> getAlerts() {
		return dao.getAlerts();
	}
}
