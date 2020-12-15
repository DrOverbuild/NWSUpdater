package com.aca.nwsupdater.service.sns;

import java.util.*;

import javax.servlet.ServletContextEvent;

import com.aca.nwsupdater.model.AlertFeatures;
import com.aca.nwsupdater.model.AlertProperties;
import com.aca.nwsupdater.model.WeatherAlertData;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.service.NWSUpdaterService;
import com.aca.nwsupdater.service.URLShortener;
import com.aca.nwsupdater.service.WeatherAlertService;
public class SnsUserAlert extends TimerTask implements javax.servlet.ServletContextListener{
	private static long DELAY = 60_000;
	private WeatherAlertService service = new WeatherAlertService();

	private List<String> seenAlerts = new ArrayList<>();

	private Timer timer;
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		timer.cancel();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		start();
	}

	public void start() {
		System.out.println("Starting SNS User Alert Service");
		timer = new Timer();
		timer.schedule(this, 0, DELAY);
	}

	@Override
	public void run() {
		System.out.println("Looking through all locations...");

		List<Location> distinctLocations = buildDistinctLocations();

		// add all alerts we get to this array, and set seen alerts to this after going through all locations and alerts
		List<String> currentAlerts = new ArrayList<>();

		for (Location loc: distinctLocations) {
			System.out.println("Searching " + loc.getName() + "...");

			String coords = loc.getLat().toString() + "," + loc.getLon().toString();
			WeatherAlertData weatherAlertData = service.getWeatherAlertData(coords);
			List<AlertFeatures> features = weatherAlertData.getFeatures();

			System.out.println("    Found " + features.size() + " alerts");

			for (AlertFeatures feature : features) {
				AlertProperties props = feature.getProperties();
				currentAlerts.add(props.getId());

				if (!seenAlerts.contains(props.getId())) {
					String shortenedURL = URLShortener.shortenedUrlForAlert(loc.getId(), props.getId());
					SnsPublishMessage.setSnsPublishMessage(feature, loc.getName(), loc.getTopicArn(), shortenedURL);
				} else {
					System.out.println("    Already sent " + props.getEvent());
				}
			}
			System.out.println();
		}

		seenAlerts = currentAlerts;
		System.out.println("Done checking alerts.");
	}

	private List<Location> buildDistinctLocations() {
		List<Location> allLocations = NWSUpdaterService.instance.getAllLocations();
		List<Location> distinctLocations = new ArrayList<>();

		for (Location location: allLocations) {
			if (!addedDistinctLocation(distinctLocations, location)) {
				distinctLocations.add(location);
			}
		}

		return distinctLocations;
	}

	private boolean addedDistinctLocation(List<Location> distinctLocations, Location newLocation) {
		for (Location distinctLocation: distinctLocations) {
			if (distinctLocation.getLat().equals(newLocation.getLat())
					&& distinctLocation.getLon().equals(newLocation.getLat())) {
				return true;
			}
		}

		return false;
	}

}
