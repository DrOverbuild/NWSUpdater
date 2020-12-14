package com.aca.nwsupdater.service.sns;

import java.util.HashMap;
import java.util.Map;

import com.aca.nwsupdater.model.AlertFeatures;
import com.amazonaws.services.sns.model.MessageAttributeValue;

public class SnsPublishMessage {
	
	public static void setSnsPublishMessage(AlertFeatures features, String cityName, String topic) {
		StringBuffer message = new StringBuffer();
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		
		appendMessage(message, cityName, features);
		
		cityName = cityName.replace(" ", "_");
		features.getProperties().setEvent(features.getProperties().getEvent().replace(" ", "_"));
		
		smsAttributes.put("Alerts", new MessageAttributeValue()
					.withStringValue(features.getProperties().getEvent())
					.withDataType("String"));
		
		SnsPublish.publishUpdate("Alerts", message.toString(), smsAttributes, topic);
	}
	
	private static void appendMessage(StringBuffer message, String cityName, AlertFeatures features) {
		message.append("Location : " + cityName);
		message.append(System.lineSeparator());
		message.append("Event: " + features.getProperties().getEvent());
	}
}
