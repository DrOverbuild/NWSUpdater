package com.aca.nwsupdater.service.sns;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

public class SnsClient {
	
	private static final String ACCESS_KEY = "AKIA6J4SAW6VFALADZO2";
	private static final String SECRET_KEY = "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";
	
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
