package com.aca.nwsupdater.service.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class AwsSnsPublish {
	
	public static String publishUpdate(String subject, String message) {
		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setTopicArn(SnsClient.WEATHER_TOPIC_ARN);
		publishRequest.setMessage(message);
		publishRequest.setSubject(subject);
		
		AmazonSNS client = SnsClient.getAwsClient();
		PublishResult result = client.publish(publishRequest);
		
		return result.getMessageId();
	}

}
