package com.aca.nwsupdater.service.sns;

import java.util.ArrayList;
import java.util.List;

import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;
import com.aca.nwsupdater.service.NWSUpdaterService;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.Subscription;

public class SnsUtils{
	
	public static void checkEmailForConfirmation(User user, Location loc) {
		ListSubscriptionsByTopicResult subscription = SnsClient.getAwsClient().listSubscriptionsByTopic(loc.getTopicArn());
		
		//check if the user have comfirm their email
		for(Subscription s : subscription.getSubscriptions()) {
			if(s.getEndpoint().equals(user.getEmail())) {
				if(s.getSubscriptionArn().replaceAll("[^\\p{IsAlphabetic}]", "").toLowerCase().equals("pendingconfirmation")) {
					System.out.println("Please comfirm your subscription!!");
				} else {
					AwsSnsService.instance.updateEmailArn(s.getSubscriptionArn(), loc.getId(), loc.getOwnerID());
					subscriptionFilter(s.getSubscriptionArn(), loc);
				}
				break;
			}
		}
	}
	
	public static void checkAllEmailForConfirmation(List<Location> locs) {
		for(Location loc : locs) {
			User user = NWSUpdaterService.instance.getDao().getUser(loc.getOwnerID());
			checkEmailForConfirmation(user, loc);
		}
	}
	
	public static void subscriptionFilter(String subscriptionArn, Location loc) {		
		SnsSubscriptionFilter fp = new SnsSubscriptionFilter();	
		
		List<Alert> alerts = loc.getAlerts();
		ArrayList<String> alert = new ArrayList<>();
			
		for(Alert a : alerts) {
			a.setName(a.getName().replace(" ", "_"));
			alert.add(a.getName());
		}	
			
		fp.addAttributes("Alerts", alert);
		fp.apply(subscriptionArn);
	}
	
}
