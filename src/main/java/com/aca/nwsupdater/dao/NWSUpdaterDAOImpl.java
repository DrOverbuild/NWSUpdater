package com.aca.nwsupdater.dao;

import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NWSUpdaterDAOImpl implements NWSUpdaterDAO{
	private static final String authenticateQuery =
			"SELECT id, email, phone FROM user " +
					"WHERE email = ? AND password = AES_ENCRYPT(?, ?)";

	private static final String createNewUserQuery =
			"INSERT INTO user (email, phone, password) " +
					"VALUES (?, ?, AES_ENCRYPT(?, ?))";

	private static final String selectUserByIdQuery =
			"SELECT id, email, phone FROM user " +
					"WHERE id = ?";

	private static final String updateUserQuery =
			"UPDATE user " +
					"SET email = ?, phone = ? " +
					"WHERE id = ?";

	private static final String updateUserPasswordQuery =
			"UPDATE user " +
					"SET email = ?, phone = ?, password = AES_ENCRYPT(?, ?) " +
					"WHERE id = ?";

	private static final String getAllLocationsByOwnerID =
			"SELECT id, name, lat, lon, gridpoint_office, gridpoint_x, " +
					"gridpoint_y, sms_enabled, email_enabled, owner_id " +
					"FROM location " +
					"WHERE owner_id = ?";

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
	public List<Location> getLocations(int userID) {
		List<Location> locations = new ArrayList<>();

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement(getAllLocationsByOwnerID);

			rs = stmt.executeQuery();

			while (rs.next()) {
				locations.add(makeLocation(rs));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(rs, stmt, conn);
		}

		return user;
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
	}

	private void closeResourses(ResultSet rs, PreparedStatement stmt, Connection conn) {
		try {
			if (stmt != null && !stmt.isClosed()) {
				rs.close();
			}

			if (rs != null && !rs.isClosed()) {
				stmt.close();
			}

			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
}
