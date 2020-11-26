package com.aca.nwsupdater;

import com.aca.nwsupdater.model.WeatherAlertData;
import com.aca.nwsupdater.model.WeatherForcastData;
import com.aca.nwsupdater.service.WeatherAlertService;

public class WeatherTest {
	
	public static void main(String[] args) {
		WeatherAlertService service = new WeatherAlertService("https://", "api.weather.gov", "/gridpoints", "/LZK/", "/forecast?units=us");
		WeatherForcastData weatherData = service.getWeatherForcastData("75,87");
		
		System.out.println(weatherData.toString());
	}
}
