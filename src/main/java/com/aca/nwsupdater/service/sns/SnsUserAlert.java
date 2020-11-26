package com.aca.nwsupdater.service.sns;

import java.util.*;

import com.aca.nwsupdater.model.AlertFeatures;
import com.aca.nwsupdater.model.AlertProperties;
import com.aca.nwsupdater.model.WeatherAlertData;
import com.aca.nwsupdater.service.WeatherAlertService;
import com.amazonaws.services.sns.AmazonSNS;

public class SnsUserAlert extends TimerTask{
	private static long DELAY = 60_000;
	private static AmazonSNS snsClient = SnsClient.getAwsClient();
	
	public SnsUserAlert() {

	}
	
	public void start() {
		new Timer().schedule(this, 0, DELAY);
	}

	@Override
	public void run() {	
		WeatherAlertService service = new WeatherAlertService();
		WeatherAlertData weatherAlertData = service.getWeatherAlertData("25.441393,-80.471638");
		List<AlertFeatures> features = weatherAlertData.getFeatures();
			
		for(AlertFeatures f : features) {
			AlertProperties properties = f.getProperties();
				
			if(properties.getSeverity().equals("Major")) {					
				AwsSnsPublish.publishUpdate("Hello world", "Welcome to the world");
			}
		}		
	}

}
