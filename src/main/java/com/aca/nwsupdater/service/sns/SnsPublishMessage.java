package com.aca.nwsupdater.service.sns;

import java.util.HashMap;
import java.util.Map;

import com.aca.nwsupdater.model.AlertFeatures;
import com.amazonaws.services.sns.model.MessageAttributeValue;

public class SnsPublishMessage {
	
	public static void setSnsPublishMessage(AlertFeatures features, String cityName, String topic, String url) {
		StringBuffer message = new StringBuffer();
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		
		appendMessage(message, cityName, features, url);
		
		cityName = cityName.replace(" ", "_");
		String eventSanitized = features.getProperties().getEvent().replace(" ", "_");
		
		smsAttributes.put("Alerts", new MessageAttributeValue()
					.withStringValue(eventSanitized)
					.withDataType("String"));
		
		SnsPublish.publishUpdate("Alerts", message.toString(), smsAttributes, topic);
	}
	
	private static void appendMessage(StringBuffer message, String cityName, AlertFeatures features, String url) {
		message.append("Alert: ").append(features.getProperties().getEvent()).append(" for ")
				.append(cityName).append(System.lineSeparator()).append(url);
	}
}
