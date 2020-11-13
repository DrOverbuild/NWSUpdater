package com.aca.nwsupdater.dao;


import com.aca.nwsupdater.model.webapp.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class NWSUpdaterDB {

	public static Connection getConnection() {
		try {
			InputStream is = NWSUpdaterDB.class.getClassLoader().getResourceAsStream("db.properties");
			Properties props = new Properties();
			props.load(is);

			Class.forName("org.mariadb.jdbc.Driver");
			Connection conn = DriverManager.getConnection(props.getProperty("url"));

			return conn;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FATAL - Could not load properties");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("FATAL - Could not load driver");
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			System.out.println("FATAL - Could not establish connection with database");
		}

		return null;
	}

	public static void main(String[] args) {
		NWSUpdaterDAOImpl impl = new NWSUpdaterDAOImpl();

		User u = new User();
		u.setEmail("jreddin1@cub.uca.edu");
		u.setPassword("reallygoodpass!");

		User loggedin = impl.authenticate(u);
		if (loggedin != null) {
			System.out.println("Success");
		} else {
			System.out.println("Uh oh");
		}
	}
}
