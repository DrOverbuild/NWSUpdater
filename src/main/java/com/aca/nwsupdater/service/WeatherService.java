package com.aca.nwsupdater.service;

import java.util.ArrayList;
import java.util.List;

import com.aca.nwsupdater.model.ForecastPeriods;
import com.aca.nwsupdater.model.WeatherForecastData;
import com.aca.nwsupdater.model.WeatherPointData;
import com.aca.nwsupdater.model.webapp.Location;

public class WeatherService {
	public static List<ForecastPeriods> requestForecastData(Double lat, Double lon) {
		WeatherPointService pointService = new WeatherPointService();
		WeatherForecastData weatherForecastData = null;
		WeatherPointData weatherPointData = pointService.getWeatherPointData(lat + "," + lon);
		
		if(weatherPointData != null) {
			WeatherForecastService forecastService = new WeatherForecastService();
			weatherForecastData = forecastService.getWeatherForecastData(weatherPointData.getProperties().getForecast());
		}
		
		List<ForecastPeriods> forecastPeriods = new ArrayList<>();
		
		for(ForecastPeriods f : weatherForecastData.getForecastProperties().getPeriod()) {
			if(!f.getName().toLowerCase().contains("night") || f.getName().toLowerCase().contains("tonight")){
				forecastPeriods.add(f);
				if(forecastPeriods.size() == 3) {
					break;
				}
			}
		}
		
		return forecastPeriods;
	}
}
