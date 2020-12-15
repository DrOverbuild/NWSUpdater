package com.aca.nwsupdater.service;

import java.util.List;

import com.aca.nwsupdater.model.ForecastPeriods;
import com.aca.nwsupdater.model.WeatherForecastData;
import com.aca.nwsupdater.model.WeatherPointData;
import com.aca.nwsupdater.model.webapp.Location;

public class WeatherService {
	public static List<ForecastPeriods> requestForecastData(Location location) {
		WeatherPointService pointService = new WeatherPointService();
		WeatherForecastData weatherForecastData = null;
		WeatherPointData weatherPointData = pointService.getWeatherPointData(location.getLat() + "," + location.getLon());
		
		if(weatherPointData != null) {
			WeatherForecastService forecastService = new WeatherForecastService();
			weatherForecastData = forecastService.getWeatherForecastData(weatherPointData.getProperties().getForecast());
		}
		
		
		return weatherForecastData.getForecastProperties().getPeriod();
	}
}
