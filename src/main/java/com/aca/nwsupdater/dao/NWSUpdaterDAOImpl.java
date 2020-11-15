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

	private static final String createNewUser =
			"INSERT INTO user (email, phone, password) " +
					"VALUES (?, ?, AES_ENCRYPT(?, ?))";

	private static final String selectUserById =
			"SELECT id, email, phone FROM user " +
					"WHERE id = ?";

	private static String selectId =
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
			stmt = conn.prepareStatement(createNewUser);
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
			stmt = conn.prepareStatement(authenticateQuery);

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

	private int getLastInsertedID(Connection conn) {
		ResultSet rs = null;
		PreparedStatement stmt = null;

		int id = -1;

		try {
			stmt = conn.prepareStatement(selectId);
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

	private void closeResourses(ResultSet rs, PreparedStatement stmt, Connection conn) {
		try {
			if (stmt != null) {
				rs.close();
			}

			if (rs != null) {
				stmt.close();
			}

			if (conn != null) {
				conn.close();
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
}
