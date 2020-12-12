package com.aca.nwsupdater.service.sns;

import java.util.HashMap;
import java.util.Map;

import com.aca.nwsupdater.model.AlertFeatures;
import com.amazonaws.services.sns.model.MessageAttributeValue;

public class SnsPublishMessage {
	
	public static void setSnsPublishMessage(AlertFeatures features, String cityName) {
		StringBuffer message = new StringBuffer();
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		
		message.append("Location : " + cityName);
		message.append("Event: " + features.getProperties().getEvent());
		
		smsAttributes.put("City", new MessageAttributeValue()
					.withStringValue(cityName)
					.withDataType("String"));
		
		SnsPublish.publishUpdate("Alerts", message.toString(), smsAttributes);
	}
}
