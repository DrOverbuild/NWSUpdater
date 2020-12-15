package com.aca.nwsupdater.service.sns;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.aca.nwsupdater.model.sns.TopicSubscriber;
import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;
import com.aca.nwsupdater.service.NWSUpdaterService;

public class SnsSubscriberService implements Runnable{
	public static SnsSubscriberService instance;

	private Queue<SubscriberServiceTask> taskQueue = new LinkedList<>();
	private Thread taskThread;

	public SnsSubscriberService() {
		taskThread = new Thread(this);
		taskThread.start();
	}

	@Override
	public void run() {
		while(true) {
			if (taskQueue.size() > 0) {
				try {
					taskQueue.poll().execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void createSubcription(Location newLocation) {
		SubscriberServiceTask task = () -> {
			create(newLocation);
		};

		taskQueue.add(task);
	}

	public void deleteFilter(TopicSubscriber topicSubscriber) {
		SubscriberServiceTask task = () -> {
			delete(topicSubscriber);
		};

		taskQueue.add(task);
	}

	public void updateSubcription(Location location) {
		SubscriberServiceTask task = () -> {
			update(location);
		};

		taskQueue.add(task);
	}
	
	private void create(Location loc){
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
		
		if(loc.getAlerts().isEmpty()) {
			SnsUtils.setAlertToNone(loc);
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
	
	private void delete(TopicSubscriber topicSub) {
		SnsUtils.unsubscribe(topicSub.getPhoneArn());

		if(topicSub.getEmailArn().replaceAll("[^\\p{IsAlphabetic}]", "").toLowerCase().equals("pendingconfirmation")) {
			System.out.println("Please Unsubscriber from your email");
		} else {
			SnsUtils.unsubscribe(topicSub.getEmailArn());
		}
	}
	
	private void update(Location loc) {
		TopicSubscriber topicSubscriber = AwsSnsService.instance.getTopicSubscriber(loc.getOwnerID(), loc.getId());
		User user = NWSUpdaterService.instance.getDao().getUser(loc.getOwnerID());

		if(loc.getAlerts().isEmpty()) {
			SnsUtils.setAlertToNone(loc);
		}
		
		if(topicSubscriber.getPhoneArn() == null) {
			if(loc.getSmsEnabled()) {
				String subscriptionArn = SnsSubscription.subscribePhoneNumber(user.getPhone(), topicSubscriber.getTopicArn());
				SnsUtils.subscriptionFilter(subscriptionArn, loc);
				AwsSnsService.instance.updateSubscriberArn(subscriptionArn, "phone", loc.getId(), loc.getOwnerID());
			}
		} else {
			//save alerts incase of sms not enabled
			List<Alert> temp = loc.getAlerts();
			if(!loc.getSmsEnabled()) {
				SnsUtils.setAlertToNone(loc);
			}
			SnsUtils.subscriptionFilter(topicSubscriber.getPhoneArn(), loc);
			loc.setAlerts(temp);
		}
		
		if(topicSubscriber.getEmailArn() == null) {
			if(loc.getEmailEnabled()) {
				String subscriptionArn = SnsSubscription.subscribeEmail(user.getEmail(), topicSubscriber.getTopicArn());
				AwsSnsService.instance.updateSubscriberArn(subscriptionArn, "email", loc.getId(), loc.getOwnerID());
			}
		} else {
			if(!loc.getEmailEnabled()) {
				SnsUtils.setAlertToNone(loc);
			}
			
			if(topicSubscriber.getEmailArn().replaceAll("[^\\p{IsAlphabetic}]", "").toLowerCase().equals("pendingconfirmation")) {
				SnsUtils.checkEmailForConfirmation(user, loc);
			} else {
				SnsUtils.subscriptionFilter(topicSubscriber.getEmailArn(), loc);
			}
		}
	}
}

interface SubscriberServiceTask {
	void execute();
}
