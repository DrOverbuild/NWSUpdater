package com.aca.nwsupdater.dao;

import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;

import java.util.List;

public class NWSUpdaterDAOImpl implements NWSUpdaterDAO{
	@Override
	public User authenticate(User loginInformation) {
		return null;
	}

	@Override
	public User createAccount(User information) {
		return null;
	}

	@Override
	public User getUser(int userID) {
		return null;
	}

	@Override
	public User updateUser(User userInformation) {
		return null;
	}

	@Override
	public List<Location> getLocations(int userID) {
		return null;
	}

	@Override
	public Location addLocation(Location location) {
		return null;
	}

	@Override
	public Location updateLocation(Location location) {
		return null;
	}

	@Override
	public List<Location> deleteLocation(Location location) {
		return null;
	}

	@Override
	public List<Alert> getAlerts() {
		return null;
	}
}
