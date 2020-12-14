package com.aca.nwsupdater.service.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

public class SnsSubscription {
	
	public static String subscribePhoneNumber(String phoneNumber, String topic) {
		SubscribeRequest request = new SubscribeRequest();
		request.setEndpoint("+1" + phoneNumber);
		request.setProtocol("sms");
		
		return subscribe(request, topic);
	}

	public static String subscribeEmail(String email, String topic) {
		SubscribeRequest request = new SubscribeRequest();
		request.setEndpoint(email);
		request.setProtocol("email");
		
		return subscribe(request, topic);
	}
	
	private static String subscribe(SubscribeRequest request, String topic) {
		request.setTopicArn(topic);
		
		AmazonSNS client = SnsClient.getAwsClient();
		SubscribeResult result = client.subscribe(request);
		
		return result.getSubscriptionArn();
	}

}
