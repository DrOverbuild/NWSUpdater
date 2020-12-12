package com.aca.nwsupdater.service.sns;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.aca.nwsupdater.model.AlertFeatures;
import com.aca.nwsupdater.model.AlertProperties;
import com.aca.nwsupdater.model.ForecastPeriods;
import com.aca.nwsupdater.model.ForecastProperties;
import com.aca.nwsupdater.model.WeatherAlertData;
import com.aca.nwsupdater.model.WeatherForecastData;
import com.aca.nwsupdater.service.WeatherAlertService;
import com.aca.nwsupdater.service.WeatherForecastService;
import com.amazonaws.services.sns.AmazonSNS;

public class SnsUserForecast extends TimerTask{
	private static long DELAY = 60_000;
	private static AmazonSNS snsClient = SnsClient.getAwsClient();
	
	public void start() {
		new Timer().schedule(this, 0, DELAY);
	}

	@Override
	public void run() {	
		WeatherForecastService service = new WeatherForecastService();
		WeatherForecastData weatherForecastData = service.getWeatherForecastData("SHV", "30,96");
		ForecastProperties properties = weatherForecastData.getForecastProperties();
		List<ForecastPeriods> periods = properties.getPeriod();
		
		if(periods.get(0).getIsDayTime()) {
			//AwsSnsPublish.publishUpdate("Hello world", "It is daytime");
		}	
	}
}
