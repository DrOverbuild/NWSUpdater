package com.aca.NWSUpdaterService;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

public class AwsSnsSubscription {
	
	public static String subscribePhoneNumber(String phoneNumber) {
		SubscribeRequest request = new SubscribeRequest();
		request.setEndpoint("+1" + phoneNumber);
		request.setProtocol("sms");
		
		return subscribe(request);
	}

	private static String subscribe(SubscribeRequest request) {
		request.setTopicArn(SnsClient.WEATHER_TOPIC_ARN);
		
		AmazonSNS client = SnsClient.getAwsClient();
		SubscribeResult result = client.subscribe(request);
		
		return result.getSubscriptionArn();
	}

}
