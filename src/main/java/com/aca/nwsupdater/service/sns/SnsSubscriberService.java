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
		TopicSubscriber topicSubscriber = new TopicSubscriber();
		String topicArn = "";
		
		//check if topic for coords already exist
		Location distinctLoc = NWSUpdaterService.instance.getDistinctLocationByCoords(loc.getLat(), loc.getLon());
		if(distinctLoc.getTopicArn() == null) {
			topicArn = SnsCreateTopic.createLocationTopic(loc.getName() +
					loc.getLat().toString() + 
					loc.getLon().toString());
		} else {
			topicArn = distinctLoc.getTopicArn();
		}
		
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
		SnsUtils.unsubscribe(topicSub.getPhoneArn());

		if(topicSub.getEmailArn().replaceAll("[^\\p{IsAlphabetic}]", "").toLowerCase().equals("pendingconfirmation")) {
			System.out.println("Please Unsubscriber from your email");
		} else {
			SnsUtils.unsubscribe(topicSub.getEmailArn());
		}
	}
	
	private void update() {
		TopicSubscriber topicSubscriber = AwsSnsService.instance.getTopicSubscriber(loc.getOwnerID(), loc.getId());
		User user = NWSUpdaterService.instance.getDao().getUser(loc.getOwnerID());
		
		if(loc.getSmsEnabled()) {
			SnsUtils.subscriptionFilter(topicSubscriber.getPhoneArn(), loc);
		} else {
			SnsUtils.unsubscribe(topicSubscriber.getPhoneArn());
		}

		if(loc.getEmailEnabled()) {
			if(topicSubscriber.getEmailArn().replaceAll("[^\\p{IsAlphabetic}]", "").toLowerCase().equals("pendingconfirmation")) {
				SnsUtils.checkEmailForConfirmation(user, loc);
			} else {
				SnsUtils.subscriptionFilter(topicSubscriber.getEmailArn(), loc);
			}
		} else {
			SnsUtils.unsubscribe(topicSubscriber.getEmailArn());
		}
	}

}
