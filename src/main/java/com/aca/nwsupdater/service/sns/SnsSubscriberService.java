package com.aca.nwsupdater.service.sns;

import com.aca.nwsupdater.model.sns.TopicSubscriber;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;
import com.aca.nwsupdater.service.NWSUpdaterService;
import com.amazonaws.services.sns.model.UnsubscribeRequest;

public class SnsSubscriberService extends Thread{
	private static boolean create = false;
	private static boolean delete = false;
	private static boolean update = false;
	private static Location loc;
	private static TopicSubscriber topicSub;
	
	@Override
	public void run() {
		if(create) {
			create();
			create = false;
		} else if(delete) {
			delete();
			delete = false;
		} else if(update) {
			update();
			update = false;
		}
	}
	
	public static void createSubcription(Location newLocation) {
		loc = newLocation;
		create = true;
		new SnsSubscriberService().start();
	}

	public static void deleteFilter(TopicSubscriber topicSubscriber) {
		topicSub = topicSubscriber;
		delete = true;
		new SnsSubscriberService().start();
	}

	public static void updateSubcription(Location location) {
		loc = location;
		update = true;
		new SnsSubscriberService().start();
	}
	
	private void create(){
		String subscriptionArn = "";
		User user = NWSUpdaterService.instance.getDao().getUser(loc.getOwnerID());
		String topicArn = SnsCreateTopic.createLocationTopic(loc.getName() + 
							loc.getLat().toString() + 
							loc.getLon().toString());
		
		TopicSubscriber topicSubscriber = new TopicSubscriber();
		
		if(loc.getSmsEnabled()) {
			subscriptionArn = SnsSubscription.subscribePhoneNumber(user.getPhone(), topicArn);
			topicSubscriber.setPhoneArn(subscriptionArn);
			SnsUtils.subscriptionFilter(subscriptionArn, loc);
		}
		
		if(loc.getEmailEnabled()) {
			subscriptionArn = SnsSubscription.subscribeEmail(user.getEmail(), topicArn);
			topicSubscriber.setEmailArn(subscriptionArn);
		}
		
		topicSubscriber.setLocationID(loc.getId());
		topicSubscriber.setUserID(loc.getOwnerID());
		
		AwsSnsService.instance.addTopicSubscriber(topicSubscriber);
		AwsSnsService.instance.updateTopicArn(topicArn, loc.getId());
	}
	
	private void delete() {
		
		if(loc.getSmsEnabled()) {
			UnsubscribeRequest request = new UnsubscribeRequest();
			request.setSubscriptionArn(topicSub.getPhoneArn());
			SnsClient.getAwsClient().unsubscribe(request);
		}
		
		if(loc.getEmailEnabled()) {
			if(topicSub.getEmailArn().replaceAll("[^\\p{IsAlphabetic}]", "").toLowerCase().equals("pendingconfirmation")) {
				System.out.println("Please Unsubscriber from your email");
			} else {
				UnsubscribeRequest request = new UnsubscribeRequest();
				request.setSubscriptionArn(topicSub.getEmailArn());
				SnsClient.getAwsClient().unsubscribe(request);
			}
		}
	}
	
	private void update() {
		TopicSubscriber topicSubscriber = AwsSnsService.instance.getTopicSubscriber(loc.getOwnerID(), loc.getId());
		User user = NWSUpdaterService.instance.getDao().getUser(loc.getOwnerID());
		
		if(loc.getSmsEnabled()) {
			SnsUtils.subscriptionFilter(topicSubscriber.getPhoneArn(), loc);
		}

		if(loc.getEmailEnabled()) {
			
			if(topicSubscriber.getEmailArn().replaceAll("[^\\p{IsAlphabetic}]", "").toLowerCase().equals("pendingconfirmation")) {
				SnsUtils.checkEmailForConfirmation(user, loc);
			} else {
				SnsUtils.subscriptionFilter(topicSubscriber.getEmailArn(), loc);
			}
		}
	}

}
