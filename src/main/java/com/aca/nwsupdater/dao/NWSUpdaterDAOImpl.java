package com.aca.nwsupdater.dao;

import com.aca.nwsupdater.model.sns.DistinctLocations;
import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;
import com.aca.nwsupdater.service.sns.SnsSubscriberService;
import com.aca.nwsupdater.service.sns.SnsSubscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NWSUpdaterDAOImpl implements NWSUpdaterDAO{
	private static final String authenticateQuery =
			"SELECT id, email, phone, subscriptionArn FROM user " +
					"WHERE email = ? AND password = AES_ENCRYPT(?, ?)";

	private static final String createNewUserQuery =
			"INSERT INTO user (email, phone, password) " +
					"VALUES (?, ?, AES_ENCRYPT(?, ?))";

	private static final String selectUserByIdQuery =
			"SELECT id, email, phone, subscriptionArn FROM user " +
					"WHERE id = ?";

	private static final String updateUserQuery =
			"UPDATE user " +
					"SET email = ?, phone = ? " +
					"WHERE id = ?";

	private static final String updateUserPasswordQuery =
			"UPDATE user " +
					"SET email = ?, phone = ?, password = AES_ENCRYPT(?, ?) " +
					"WHERE id = ?";

	private static final String getAllLocationsByOwnerIDQuery =
			"SELECT id, name, lat, lon, gridpoint_office, gridpoint_x, " +
					"gridpoint_y, sms_enabled, email_enabled, owner_id " +
					"FROM location " +
					"WHERE owner_id = ?";

	private static final String selectLocationsByIDQuery =
			"SELECT id, name, lat, lon, gridpoint_office, gridpoint_x, " +
					"        gridpoint_y, sms_enabled, email_enabled, owner_id " +
					"FROM location WHERE id = ? AND owner_id = ?";

	private static final String insertLocationQuery =
			"INSERT INTO location (name, lat, lon, gridpoint_office, gridpoint_x, " +
					"                      gridpoint_y, sms_enabled, email_enabled, owner_id) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String updateLocationQuery =
			"UPDATE location SET name = ?, lat = ?, lon = ?, gridpoint_office = ?," +
					" gridpoint_x = ?, gridpoint_y = ?, sms_enabled = ?," +
					" email_enabled = ? WHERE id = ?";
	
	private static final String getAllDistinctLocationsQuery =
			"SELECT DISTINCT name "+
			"FROM location";

	private static final String updateUserSubscriptionArn = 
			"UPDATE user " +
			"SET subscriptionArn = ?" +
			"WHERE id = ?";		
	
	private static final String deleteLocationQuery =
			"DELETE FROM location WHERE id = ?";

	private static final String deleteLocationAlerts =
			"DELETE FROM location_alerts WHERE location_id = ?";

	private static final String selectAllAlertsQuery =
			"SELECT id, name FROM alert ORDER BY name";

	private static final String insertLocationAlertQuery =
			"INSERT INTO location_alerts (location_id, alert_id) VALUES (?, ?)";

	private static final String selectLocationAlertsQuery =
			"SELECT alert_id, name FROM location_alerts " +
					"INNER JOIN alert a on location_alerts.alert_id = a.id " +
					"WHERE location_id = ?";

	private static final String deleteSingleLocationAlertQuery =
			"DELETE FROM location_alerts WHERE location_id = ? AND alert_id = ?";

	private static String selectIdQuery =
			"SELECT LAST_INSERT_ID() AS 'id'";

	/**
	 * Authenticates the user with given log in information.
	 * @param loginInformation
	 * @return Null if user cannot be authenticated or if a connection error occurs
	 * with the database.
	 */
	@Override
	public User authenticate(User loginInformation) {
		User loggedInUser = null;

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(authenticateQuery);
			stmt.setString(1, loginInformation.getEmail());
			stmt.setString(2, loginInformation.getPassword());
			stmt.setString(3, loginInformation.getPassword().substring(0,1));
			rs = stmt.executeQuery();

			while (rs.next()) {
				loggedInUser = makeUser(rs);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(rs, stmt, conn);
		}

		return loggedInUser;
	}

	@Override
	public User createAccount(User information) {
		User newUser = null;

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(createNewUserQuery);
			stmt.setString(1, information.getEmail());
			stmt.setString(2, information.getPhone());
			stmt.setString(3, information.getPassword());
			stmt.setString(4, information.getPassword().substring(0,1));
			int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated == 1) {
				int id = getLastInsertedID(conn);
				newUser = getUser(id);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}

		return newUser;
	}

	@Override
	public User getUser(int userID) {
		User user = null;

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(selectUserByIdQuery);
			stmt.setInt(1, userID);

			rs = stmt.executeQuery();

			while (rs.next()) {
				user = makeUser(rs);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(rs, stmt, conn);
		}

		return user;
	}

	@Override
	public User updateUser(User userInformation) {
		User updatedUser = null;

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;

		String q = updateUserQuery;
		int idIndex = 3;

		// check if given data contains password...
		// if so we're gonna need a different query
		if (userInformation.getPassword() != null) {
			q = updateUserPasswordQuery;
			idIndex = 5;
		}

		try {
			stmt = conn.prepareStatement(q);
			stmt.setString(1, userInformation.getEmail());
			stmt.setString(2, userInformation.getPhone());

			// if password is provided, params will be different
			if (userInformation.getPassword() != null){
				stmt.setString(3, userInformation.getPassword());
				stmt.setString(4, userInformation.getPassword().substring(0, 1));
			}

			stmt.setInt(idIndex, userInformation.getId());

			int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated == 1) {
				int id = userInformation.getId();
				updatedUser = getUser(id);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}

		return updatedUser;
	}

	@Override
	public User updateUserSubscriptionArn(int userID, String subscriptionArn) {
		User updatedUser = null;

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		
		String q = updateUserSubscriptionArn;
		
		try {
			stmt = conn.prepareStatement(q);
			stmt.setString(1, subscriptionArn);
			stmt.setInt(2, userID);

			int rowsUpdated = stmt.executeUpdate();
			
			if (rowsUpdated == 1) {
				int id = userID;
				updatedUser = getUser(id);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}
		
		return updatedUser;
	}
	
	@Override
	public List<Location> getLocations(int userID) {
		List<Location> locations = new ArrayList<>();

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(getAllLocationsByOwnerIDQuery);
			stmt.setInt(1, userID);
			rs = stmt.executeQuery();

			while (rs.next()) {
				locations.add(makeLocation(rs));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(rs, stmt, conn);
		}

		return locations;
	}

	@Override
	public Location locationById(int locationID, int userId) {
		Location location = null;

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(selectLocationsByIDQuery);
			stmt.setInt(1, locationID);
			stmt.setInt(2, userId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				location = makeLocation(rs);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(rs, stmt, conn);
		}

		if (location != null) {
			location.setAlerts(getAlertsForLocation(location));
		}

		return location;
	}

	@Override
	public Location addLocation(Location location) {
		Location newLoc = null;

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(insertLocationQuery);
			stmt.setString(1, location.getName());
			stmt.setDouble(2, location.getLat());
			stmt.setDouble(3, location.getLon());

			// TODO change from null once gridpoints are implemented
			stmt.setNull(4, Types.VARCHAR);
			stmt.setNull(5, Types.INTEGER);
			stmt.setNull(6, Types.INTEGER);

			stmt.setBoolean(7, location.getSmsEnabled());
			stmt.setBoolean(8, location.getEmailEnabled());
			stmt.setInt(9, location.getOwnerID());
			int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated == 1) {
				int id = getLastInsertedID(conn);
				newLoc = locationById(id, location.getOwnerID());
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}

		if (newLoc != null && location.getAlerts() != null) {
			for (Alert a: location.getAlerts()) {
				addLocationAlert(newLoc, a);
				newLoc.setAlerts(getAlertsForLocation(newLoc));
			}
			
		}
		
		SnsSubscriberService.createSubcription(getUser(location.getOwnerID()), newLoc);

		return newLoc;
	}
	
	@Override
	public List<DistinctLocations> getAllDistinctLocations(){
		List<DistinctLocations> distinctLocations = new ArrayList<>();

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(getAllDistinctLocationsQuery);
			rs = stmt.executeQuery();

			while (rs.next()) {
				distinctLocations.add(makeDistinctLocations(rs));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(rs, stmt, conn);
		}

		return distinctLocations;
	}
	
	@Override
	public Location updateLocation(Location location) {
		Location updated = null;

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(updateLocationQuery);
			stmt.setString(1, location.getName());
			stmt.setDouble(2, location.getLat());
			stmt.setDouble(3, location.getLon());

			// TODO change from null once gridpoints are implemented
			stmt.setNull(4, Types.VARCHAR);
			stmt.setNull(5, Types.INTEGER);
			stmt.setNull(6, Types.INTEGER);

			stmt.setBoolean(7, location.getSmsEnabled());
			stmt.setBoolean(8, location.getEmailEnabled());
			stmt.setInt(9, location.getId());
			int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated == 1) {
				updated = locationById(location.getId(), location.getOwnerID());
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}

		// Check for alert differences
		List<Alert> oldAlerts = getAlertsForLocation(location);
		for (Alert a : oldAlerts) {
			if (!location.getAlerts().contains(a)) {
				deleteSingleLocationAlert(location, a);
			}
		}

		if (location.getAlerts() != null) {
			for (Alert a : location.getAlerts()) {
				if (!oldAlerts.contains(a)) {
					addLocationAlert(location, a);
				}
			}
		}

		if (updated != null) {
			updated.setAlerts(getAlertsForLocation(location));
		}

		SnsSubscriberService.updateSubcription(getUser(updated.getOwnerID()), updated);
		
		return updated;
	}

	@Override
	public List<Location> deleteLocation(Location location) {

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(deleteLocationAlerts);
			stmt.setInt(1, location.getId());

			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement(deleteLocationQuery);
			stmt.setInt(1, location.getId());

			stmt.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}

		SnsSubscriberService.deleteFilter(getUser(location.getOwnerID()), getLocations(location.getOwnerID()));
		
		return getLocations(location.getOwnerID());
	}

	@Override
	public List<Alert> getAlerts() {
		List<Alert> alerts = new ArrayList<>();

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(selectAllAlertsQuery);

			rs = stmt.executeQuery();

			while (rs.next()) {
				Alert alert = new Alert();
				alert.setId(rs.getInt("id"));
				alert.setName(rs.getString("name"));
				alerts.add(alert);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(rs, stmt, conn);
		}

		return alerts;
	}

	private void addLocationAlert(Location location, Alert alert) {
		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(insertLocationAlertQuery);
			stmt.setInt(1, location.getId());
			stmt.setInt(2, alert.getId());

			stmt.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}
	}

	private List<Alert> getAlertsForLocation(Location location) {
		List<Alert> alerts = new ArrayList<>();

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(selectLocationAlertsQuery);
			stmt.setInt(1, location.getId());

			rs = stmt.executeQuery();

			while (rs.next()) {
				Alert alert = new Alert();
				alert.setId(rs.getInt("alert_id"));
				alert.setName(rs.getString("name"));
				alerts.add(alert);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(rs, stmt, conn);
		}

		return alerts;
	}

	private void deleteSingleLocationAlert(Location location, Alert alert) {
		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(deleteSingleLocationAlertQuery);
			stmt.setInt(1, location.getId());
			stmt.setInt(2, alert.getId());

			stmt.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}

	}

	private int getLastInsertedID(Connection conn) {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		int id = -1;

		try {
			stmt = conn.prepareStatement(selectIdQuery);
			rs = stmt.executeQuery();

			while (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(rs, stmt, null);
		}

		return id;
	}

	private User makeUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setEmail(rs.getString("email"));
		user.setPhone(rs.getString("phone"));
		user.setSubscriptionArn(rs.getString("subscriptionArn"));

		return user;
	}

	private Location makeLocation(ResultSet rs) throws SQLException {
		Location loc = new Location();
		loc.setId(rs.getInt("id"));
		loc.setLat(rs.getDouble("lat"));
		loc.setLon(rs.getDouble("lon"));
		loc.setName(rs.getString("name"));
		loc.setSmsEnabled(rs.getBoolean("sms_enabled"));
		loc.setEmailEnabled(rs.getBoolean("email_enabled"));
		loc.setOwnerID(rs.getInt("owner_id"));

		// TODO get alerts
		// TODO setup gridpoint data

		return loc;
	}

	private DistinctLocations makeDistinctLocations(ResultSet rs) throws SQLException{
		DistinctLocations disLoc = new DistinctLocations();
		disLoc.setName(rs.getString("name"));
		disLoc.setLat(rs.getDouble("lat"));
		disLoc.setLon(rs.getDouble("lon"));
		
		return disLoc;
	}
	
	private void closeResourses(ResultSet rs, PreparedStatement stmt, Connection conn) {
		try {
			if (stmt != null && !stmt.isClosed()) {
				stmt.close();
			}

			if (rs != null && !rs.isClosed()) {
				rs.close();
			}

			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
}
