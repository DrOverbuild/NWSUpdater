package com.aca.nwsupdater.dao.sns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.aca.nwsupdater.dao.NWSUpdaterDB;
import com.aca.nwsupdater.model.sns.TopicSubscriber;
import com.aca.nwsupdater.model.webapp.User;

public class AwsSnsDAOImpl implements AwsSnsDAO{
	private static final String insertTopicSubscriberQuery = 
			"INSERT INTO subscriptionArn (location_id, user_id, phoneArn, emailArn) " +
			"VALUES(?, ?, ?, ?)";
	
	private static final String insertTopicSubscriberPhoneQuery = 
			"INSERT INTO subscriptionArn (location_id, user_id, phoneArn) " +
			"VALUES(?, ?, ?)";
	
	private static final String insertTopicSubscriberEmailQuery = 
			"INSERT INTO subscriptionArn (location_id, user_id, emailArn) " +
			"VALUES(?, ?, ?)";
	
	private static final String updateTopicArnQuery = 
			"UPDATE location " +
			"Set topicArn = ? " +
			"Where id = ? ";
	
	private static final String selectTopicSubscriberQuery = 
			"SELECT location_id, user_id, phoneArn, topicArn, emailArn " + 
			"FROM subscriptionArn sub " + 
			"INNER JOIN location loc ON loc.id = sub.location_id " + 
			"INNER JOIN user ON user.id = sub.user_id " + 
			"WHERE loc.id = ? AND user.id = ?";
	
	private static final String updateEmailArn = 
			"UPDATE subscriptionArn " + 
			"SET emailArn = ? " + 
			"WHERE location_id = ? AND user_id = ? ";
	
	private static final String updatePhoneArn = 
			"UPDATE subscriptionArn " + 
			"SET phoneArn = ? " + 
			"WHERE location_id = ? AND user_id = ? ";
	
	@Override
	public void updateTopicArn(String topicArn, int locationId) {

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(updateTopicArnQuery);
			stmt.setString(1, topicArn);
			stmt.setInt(2, locationId);

			stmt.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}
	}
	
	@Override
	public TopicSubscriber getTopicSubscriber(int userId, int locationId) {
		Connection conn = NWSUpdaterDB.getConnection();
		TopicSubscriber topicSubscriber = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement(selectTopicSubscriberQuery);
			stmt.setInt(1, locationId);
			stmt.setInt(2, userId);

			rs = stmt.executeQuery();
			
			while(rs.next()) {
				topicSubscriber = makeTopicSubscriber(rs);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}
		
		return topicSubscriber;
	}

	@Override
	public void addTopicSubscriber(TopicSubscriber topic) {

		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(insertTopicSubscriberQuery);
			stmt.setInt(1, topic.getLocationID());
			stmt.setInt(2, topic.getUserID());
			
			if(topic.getPhoneArn() == null) {
				stmt.setNull(3, Types.VARCHAR);
			} else {
				stmt.setString(3, topic.getPhoneArn());
			}

			if(topic.getEmailArn() == null) {
				stmt.setNull(4, Types.VARCHAR);
			} else {
				stmt.setString(4, topic.getEmailArn());
			}
			
			stmt.executeUpdate();

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}
		
	}
	
	@Override
	public void updateSubscriberArn(String arn, String type, int locationId, int userId) {
		Connection conn = NWSUpdaterDB.getConnection();
		PreparedStatement stmt = null;
		String q = "";
		
		if(type.equals("email")) {
			q = updateEmailArn;
		} else if(type.equals("phone")){
			q = updatePhoneArn;
		}
		
		try {
			stmt = conn.prepareStatement(q);
			stmt.setString(1, arn);
			stmt.setInt(2, locationId);
			stmt.setInt(3, userId);

			stmt.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			closeResourses(null, stmt, conn);
		}
	}
	
	private TopicSubscriber makeTopicSubscriber(ResultSet rs) throws SQLException {
		TopicSubscriber topicSubscriber = new TopicSubscriber();
		topicSubscriber.setUserID(rs.getInt("user_id"));
		topicSubscriber.setLocationID(rs.getInt("location_id"));
		topicSubscriber.setPhoneArn(rs.getString("phoneArn"));
		topicSubscriber.setEmailArn(rs.getString("emailArn"));
		topicSubscriber.setTopicArn(rs.getString("topicArn"));
		
		return topicSubscriber;
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
