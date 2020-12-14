package com.aca.nwsupdater.service.sns;

import com.aca.nwsupdater.dao.sns.AwsSnsDAO;
import com.aca.nwsupdater.dao.sns.AwsSnsDAOImpl;
import com.aca.nwsupdater.model.sns.TopicSubscriber;

public class AwsSnsService {
	public static final AwsSnsService instance = new AwsSnsService();
	
	private AwsSnsDAO dao;
	
	public AwsSnsService() {
		dao = new AwsSnsDAOImpl();
	}
	
	public AwsSnsDAO getDao() {
		return dao;
	}

	public void updateTopicArn(String topicArn, int locationId) {
		dao.updateTopicArn(topicArn, locationId);
	}
	
	public void addTopicSubscriber(TopicSubscriber topic) {
		dao.addTopicSubscriber(topic);
	}
	
	public TopicSubscriber getTopicSubscriber(int userId, int locationId) {
		return dao.getTopicSubscriber(userId, locationId);
	}
	
	public void updateEmailArn(String emailArn, int locationId, int userId) {
		dao.updateEmailArn(emailArn, locationId, userId);
	}
}
