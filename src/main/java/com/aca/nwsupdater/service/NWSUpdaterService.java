package com.aca.nwsupdater.service;

import com.aca.nwsupdater.dao.NWSUpdaterDAO;
import com.aca.nwsupdater.dao.NWSUpdaterDAOImpl;
import com.aca.nwsupdater.model.AlertProperties;
import com.aca.nwsupdater.model.NWSAlert;
import com.aca.nwsupdater.model.nws.Point;
import com.aca.nwsupdater.model.sns.DistinctLocations;
import com.aca.nwsupdater.model.sns.Simulation;
import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.HomePageModel;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.NewLocation;
import com.aca.nwsupdater.model.webapp.User;
import com.aca.nwsupdater.service.sns.SimulationAlertService;
import com.aca.nwsupdater.service.sns.SnsSubscriberService;

import java.util.ArrayList;
import java.util.List;

public class NWSUpdaterService {
	public static final NWSUpdaterService instance = new NWSUpdaterService();

	private SessionManager sessionManager;
	private NWSUpdaterDAO dao;
	private Validator validator;
	private WeatherAlertService alertService;

	private AlertProperties simulationAlert = null;

	public NWSUpdaterService() {
		dao = new NWSUpdaterDAOImpl();
		sessionManager = new SessionManager();
		validator = new Validator(this);
		sessionManager.start();

		alertService = new WeatherAlertService();
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

	public AlertProperties getSimulationAlert() {
		return simulationAlert;
	}

	public void setSimulationAlert(AlertProperties simulationAlert) {
		this.simulationAlert = simulationAlert;
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


	public Location newLocation(String auth, Location location) {
		int userId = validator.validateToken(validator.validateAuth(auth));

		if (location == null) {
			ServiceUtils.sendError(10, "Location could not be read from JSON data.");
		}

		location.setOwnerID(userId);
		return dao.addLocation(location);
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

	public List<DistinctLocations> getDistinctLocations(){		
		return dao.getAllDistinctLocations();
	}
	
	public List<Location> getLocationByCoords(Double lat, Double lon) {
		return dao.getLocationsByCoords(lat, lon);
	}
	
	public Location getDistinctLocationByCoords(Double lat, Double lon) {
		return dao.getDistinctLocationByCoords(lat, lon);
	}
	
	public Simulation simulation(Simulation simulation) {
		return SimulationAlertService.simulate(simulation);
	}

	public AlertProperties getAlert(String alertId) {
		if (alertId.equals("SIMULATION")) {
			return simulationAlert;
		}

		NWSAlert alert = alertService.getAlertById(alertId);
		if (alert != null) {
			return alert.getProperties();
		}

		return null;
	}

	public Point getPoint(Integer locationId) {
		Location locCoords = dao.locationCoordsById(locationId);
		String point = locCoords.getLat() + "," + locCoords.getLon();
		return alertService.getPoint(point);
	}
}
