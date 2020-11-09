package com.aca.nwsupdater;

import com.aca.nwsupdater.model.WeatherData;
import com.aca.nwsupdater.service.WeatherService;

public class WeatherTest {
	
	public static void main(String[] args) {
		WeatherService service = new WeatherService();
		WeatherData weatherData = service.getWeatherData("25.441393,-80.471638");
		
		System.out.println(weatherData.toString());
	}
}
