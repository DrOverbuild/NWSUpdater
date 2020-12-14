package com.aca.nwsupdater.service.sns;

import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;

public class SnsCreateTopic {
	public static String createLocationTopic(String name) {
		final CreateTopicRequest createTopicRequest = new CreateTopicRequest(name.replaceAll("[^\\p{IsDigit}\\p{IsAlphabetic}]", ""));
		final CreateTopicResult createTopicResponse = SnsClient.getAwsClient().createTopic(createTopicRequest);
		
		System.out.println("TopicArn:" + createTopicResponse.getTopicArn());
		System.out.println("CreateTopicRequest: " + SnsClient.getAwsClient().getCachedResponseMetadata(createTopicRequest));
		
		return createTopicResponse.getTopicArn();
	}
}
