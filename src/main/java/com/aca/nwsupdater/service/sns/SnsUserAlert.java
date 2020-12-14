package com.aca.nwsupdater.service.sns;

import java.util.*;

import javax.servlet.ServletContextEvent;

import com.aca.nwsupdater.model.AlertFeatures;
import com.aca.nwsupdater.model.WeatherAlertData;
import com.aca.nwsupdater.service.NWSUpdaterService;
import com.aca.nwsupdater.service.URLShortener;
import com.aca.nwsupdater.service.WeatherAlertService;
import com.aca.nwsupdater.model.sns.DistinctLocations;

public class SnsUserAlert extends TimerTask implements javax.servlet.ServletContextListener{
	private static long DELAY = 60_000;
	private static List<DistinctLocations> distinctLocations = new ArrayList<>();
	private static List<String> coords = new ArrayList<>();
	private static List<String> cityName = new ArrayList<>();
	private List<String> haveAlerts = new ArrayList<>();
	private WeatherAlertService service = new WeatherAlertService();
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		start();
	}

	public static void start() {
		System.out.println("Starting SNS User Alert Service");
		new SnsUserAlert().startTimer();
		distinctLocations = NWSUpdaterService.instance.getDistinctLocations();
		setCoords();
	}
	
	public void startTimer() {
		new Timer().schedule(this, 0, DELAY);
	}

	@Override
	public void run() {
		System.out.println("Looking through all locations...");

		for(int i = 0; i < distinctLocations.size(); i++) {
			System.out.println("Searching " + distinctLocations.get(i).getName() + "...");
			WeatherAlertData weatherAlertData = service.getWeatherAlertData(coords.get(i));
			List<AlertFeatures> features = weatherAlertData.getFeatures();
			String topic = "";
			
			if(!features.isEmpty()) {
				System.out.println("  - Found " + features.size() + " alerts");
				if(haveAlerts.contains(coords.get(i))) {
					System.out.println("    Already have alerts");
				} else {
					haveAlerts.add(coords.get(i));
					System.out.println("    Sending alerts...");

					for(AlertFeatures f : features) {
						SnsPublishMessage.setSnsPublishMessage(f, cityName.get(i), topic, "");
					}
				}
			} else if(haveAlerts.contains(coords.get(i))) {
				haveAlerts.remove(coords.get(i));
			}

			System.out.println();
		}
	}
	
	private static void setCoords() {
		for(DistinctLocations disLoc : distinctLocations) {
			coords.add(disLoc.getLat().toString() + "," + disLoc.getLon().toString());
			cityName.add(disLoc.getName());
		}
	}

}
