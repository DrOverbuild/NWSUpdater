package com.aca.nwsupdater;

import com.aca.nwsupdater.model.WeatherAlertData;
import com.aca.nwsupdater.model.WeatherForecastData;
import com.aca.nwsupdater.service.WeatherAlertService;
import com.aca.nwsupdater.service.WeatherForecastService;

public class WeatherTest {
	
	public static void main(String[] args) {
		WeatherForecastService weatherForecastService = new WeatherForecastService();
		WeatherForecastData weatherForecastData = weatherForecastService.getWeatherForecastData("SHV", "30,96");
		
		System.out.println(weatherForecastData.toString());
	}
}
