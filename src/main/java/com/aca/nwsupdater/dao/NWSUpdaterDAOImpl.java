package com.aca.nwsupdater.dao;

import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class NWSUpdaterDAOImpl implements NWSUpdaterDAO{
	private static final String authenticateQuery =
			"SELECT id, email, phone FROM user " +
					"WHERE email = ? AND password = AES_ENCRYPT(?, ?)";

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
				loggedInUser = new User();
				loggedInUser.setId(rs.getInt("id"));
				loggedInUser.setEmail(rs.getString("email"));
				loggedInUser.setPhone(rs.getString("phone"));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return loggedInUser;
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
