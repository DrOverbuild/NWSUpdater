package com.aca.nwsupdater.service.sns;

import java.util.ArrayList;
import java.util.List;

import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;
import com.aca.nwsupdater.service.NWSUpdaterService;
import com.amazonaws.services.sns.model.SetSubscriptionAttributesRequest;

public class SnsSubscriberService {
	
	public static void createSubcription(User user, Location newLoc) {
		String subscriptionArn = "";
		
		if(newLoc.getSmsEnabled()) {
			String subscriptionType = "subscriptionArnPhone";
			subscriptionArn = SnsSubscription.subscribePhoneNumber(user.getPhone());
			subcribeUsers(subscriptionArn, subscriptionType, newLoc, user);
		} 
		
		if(newLoc.getEmailEnabled()) {
			String subscriptionType = "subscriptionArnEmail";
			subscriptionArn = SnsSubscription.subscribeEmail(user.getEmail());
			subcribeUsers(subscriptionArn, subscriptionType, newLoc, user);
		}
	}

	public static void deleteFilter(User user, List<Location> locations) {
		
		String subscriptionArn = user.getSubscriptionArnPhone();
		
		resetFilters(subscriptionArn);
		
		for(Location location : locations) {
			subscriptionFilter(location, subscriptionArn);
		}
	}

	public static void updateSubcription(User user, Location location) {
		String subscriptionArn = user.getSubscriptionArnPhone();
		subscriptionFilter(location, subscriptionArn);
	}
	
	private static void subcribeUsers(String subscriptionArn, String subscriptionType, Location newLoc, User user) {
		subscriptionFilter(newLoc, subscriptionArn);
		user.setSubscriptionArnEmail(subscriptionArn);
		NWSUpdaterService.instance.updateUserSubscriptionArn(user.getId(), subscriptionArn, subscriptionType);
	}
	
	private static void resetFilters(String subscriptionArn) {
		SetSubscriptionAttributesRequest request =
		        new SetSubscriptionAttributesRequest(subscriptionArn, "FilterPolicy", "{}");
		SnsClient.getAwsClient().setSubscriptionAttributes(request);
		
	}
	
	private static void subscriptionFilter(Location location, String subscriptionArn) {
		SnsSubscriptionFilter fp = new SnsSubscriptionFilter();
		
		ArrayList<String> alerts = new ArrayList<>();
		
		for(Alert a : location.getAlerts()) {
			a.setName(a.getName().replace(" ", "_"));
			alerts.add(a.getName());
		}
		
		fp.addAttributes(location.getLat() + "," + location.getLon(), alerts);
		fp.apply(subscriptionArn);
	}
	
}
