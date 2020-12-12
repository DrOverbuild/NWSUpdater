package com.aca.nwsupdater.service.sns;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;

public class SnsClient {
	
	private static final String ACCESS_KEY = "AKIA6J4SAW6VFALADZO2";
	private static final String SECRET_KEY = "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";
	public static final String WEATHER_TOPIC_ARN = "arn:aws:sns:us-east-1:983316871082:NWSUpdate";
	
	public static AmazonSNS getAwsClient() {
		BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		AmazonSNS snsClient = AmazonSNSClient
				.builder()
				.withRegion("us-east-1")
				.withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials))
				.build();
		
		return snsClient;
	}

	
	public static void main(String[] args) {
		
//		SnsUserAlert snsUserAlert = new SnsUserAlert();
//		snsUserAlert.start();
		
//		SnsUserForecast snsUserForecast = new SnsUserForecast();
//		snsUserForecast.start();
		
//		String subscriptionArn = "arn:aws:sns:us-east-1:983316871082:NWSUpdate:efb49127-d678-465d-8f19-5bbda1dbc787";
//		
//		SnsSubscriptionFilter fp = new SnsSubscriptionFilter();
//		
//		fp.addAttribute("Person", "Longtin");
//		
//		fp.apply(subscriptionArn);
		
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		smsAttributes.put("Person", new MessageAttributeValue()
				.withStringValue("Whitney")
				.withDataType("String"));
	
		SnsPublish.publishUpdate("Alerts", "Hello Master", smsAttributes);
	}
}
