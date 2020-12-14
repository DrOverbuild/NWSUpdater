package com.aca.nwsupdater.dao.sns;

import com.aca.nwsupdater.model.sns.TopicSubscriber;

public interface AwsSnsDAO {
	public void addTopicSubscriber(TopicSubscriber topic);
	public void updateTopicArn(String topicArn, int locationId);
	public TopicSubscriber getTopicSubscriber(int userId, int locationId);
	public void updateEmailArn(String emailArn, int locationId, int userId);
}
