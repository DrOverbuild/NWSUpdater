package com.aca.NWSUpdaterService;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

public class SnsClient {
	
	private static final String ACCESS_KEY = "";
	private static final String SECRET_KEY = "";
	public static final String WEATHER_TOPIC_ARN = "";
	
	public static AmazonSNS getAwsClient() {
		BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
		AmazonSNS snsClient = AmazonSNSClient
				.builder()
				.withRegion("us-east-1")
				.withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials))
				.build();
		
		return snsClient;
	}

}
