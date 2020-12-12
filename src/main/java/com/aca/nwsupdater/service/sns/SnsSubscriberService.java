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
			subscriptionArn = SnsSubscription.subscribePhoneNumber(user.getPhone());
		} 
		
		if(newLoc.getEmailEnabled()) {
			subscriptionArn = SnsSubscription.subscribeEmail(user.getEmail());
		}
		
		if(subscriptionArn != "") {
			subscriptionFilter(newLoc, subscriptionArn);
			user.setSubscriptionArn(subscriptionArn);
			NWSUpdaterService.instance.updateUserSubscriptionArn(user.getId(), subscriptionArn);
		}
	}

	public static void deleteFilter(User user, List<Location> locations) {
		String subscriptionArn = user.getSubscriptionArn();
		
		resetFilters(subscriptionArn);
		
		for(Location location : locations) {
			subscriptionFilter(location, subscriptionArn);
		}
	}

	private static void resetFilters(String subscriptionArn) {
		SetSubscriptionAttributesRequest request =
		        new SetSubscriptionAttributesRequest(subscriptionArn, "FilterPolicy", "{}");
		SnsClient.getAwsClient().setSubscriptionAttributes(request);
		
	}

	public static void updateSubcription(User user, Location location) {
		String subscriptionArn = user.getSubscriptionArn();
		subscriptionFilter(location, subscriptionArn);
	}
	
	private static void subscriptionFilter(Location location, String subscriptionArn) {
		SnsSubscriptionFilter fp = new SnsSubscriptionFilter();
		
		ArrayList<String> alerts = new ArrayList<>();
		
		for(Alert a : location.getAlerts()) {
			alerts.add(a.getName());
		}
		
		fp.addAttributes(location.getName(), alerts);
		fp.apply(subscriptionArn);
	}
	
}
