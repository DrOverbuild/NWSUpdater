package com.aca.nwsupdater.service.sns;

import java.util.Map;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.MessageAttributeValue;

public class SnsPublish {
	
	public static String publishUpdate(String subject, String message, Map<String, MessageAttributeValue> smsAttributes, String topic) {
		PublishRequest publishRequest = new PublishRequest();
		publishRequest.setTopicArn(topic);
		publishRequest.setMessage(message);
		publishRequest.setSubject(subject);
		publishRequest.setMessageAttributes(smsAttributes);
		
		AmazonSNS client = SnsClient.getAwsClient();
		PublishResult result = client.publish(publishRequest);
		
		return result.getMessageId();
	}

}
