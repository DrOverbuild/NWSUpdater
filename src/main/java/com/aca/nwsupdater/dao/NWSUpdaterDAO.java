package com.aca.nwsupdater.dao;

import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;

import java.util.List;

public interface NWSUpdaterDAO {
	public User authenticate(User loginInformation);
	public User createAccount(User information);
	public User getUser(int userID);
	public User updateUser(User userInformation);
	public List<Location> getLocations(int userID);
	public Location locationById(int locationID);
	public Location addLocation(Location location);
	public Location updateLocation(Location location);
	public List<Location> deleteLocation(Location location);
	public List<Alert> getAlerts();
}
