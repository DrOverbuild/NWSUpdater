package com.aca.nwsupdater.service.sns;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.aca.nwsupdater.model.sns.TopicSubscriber;
import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.User;
import com.aca.nwsupdater.service.NWSUpdaterService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SnsSubscriberService implements Runnable, ServletContextListener {
	public static SnsSubscriberService instance;

	private BlockingQueue<SubscriberServiceTask> taskQueue = new ArrayBlockingQueue<SubscriberServiceTask>(256);
	private Thread taskThread;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		NWSUpdaterService.instance.setSubscriberService(this);
		taskThread = new Thread(this);
		taskThread.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		taskThread.stop();
	}

	@Override
	public void run() {
		System.out.println("Task thread started");
		while(true) {
			try {
				taskQueue.take().execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createSubcription(Location newLocation) {
		System.out.println("Adding create to task");
		SubscriberServiceTask task = () -> create(newLocation);

		try {
			taskQueue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void deleteFilter(TopicSubscriber topicSubscriber) {
		System.out.println("Adding delete to task.");
		SubscriberServiceTask task = () -> delete(topicSubscriber);

		try {
			taskQueue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void updateSubcription(Location location) {
		System.out.println("Adding update to task");
		SubscriberServiceTask task = () -> update(location);

		try {
			taskQueue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void create(Location loc){
		System.out.println("Executing create task...");
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
		System.out.println("Executing delete task...");

		SnsUtils.unsubscribe(topicSub.getPhoneArn());

		if(topicSub.getEmailArn().replaceAll("[^\\p{IsAlphabetic}]", "").toLowerCase().equals("pendingconfirmation")) {
			System.out.println("Please Unsubscriber from your email");
		} else {
			SnsUtils.unsubscribe(topicSub.getEmailArn());
		}
	}
	
	private void update(Location loc) {
		System.out.println("Executing update task...");

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
