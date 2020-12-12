package com.aca.nwsupdater.service.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

public class SnsSubscription {
	
	public static String subscribePhoneNumber(String phoneNumber) {
		SubscribeRequest request = new SubscribeRequest();
		request.setEndpoint("+1" + phoneNumber);
		request.setProtocol("sms");
		
		return subscribe(request);
	}

	public static String subscribeEmail(String email) {
		SubscribeRequest request = new SubscribeRequest();
		request.setEndpoint(email);
		request.setProtocol("email");
		
		return subscribe(request);
	}
	
	private static String subscribe(SubscribeRequest request) {
		request.setTopicArn(SnsClient.WEATHER_TOPIC_ARN);
		
		AmazonSNS client = SnsClient.getAwsClient();
		SubscribeResult result = client.subscribe(request);
		
		return result.getSubscriptionArn();
	}

}
