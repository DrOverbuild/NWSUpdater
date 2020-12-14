package com.aca.nwsupdater.service.sns;

import java.util.List;

import com.aca.nwsupdater.model.AlertFeatures;
import com.aca.nwsupdater.model.AlertProperties;
import com.aca.nwsupdater.model.sns.Simulation;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.service.NWSUpdaterService;
import com.aca.nwsupdater.service.URLShortener;

public class SimulationAlertService {
	
	public static Simulation simulate(Simulation sim) {
		AlertProperties properties = new AlertProperties();
		properties.setId("SIMULATION");
		properties.setAreaDesc(sim.getAreaDesc());
		properties.setCertainty(sim.getCertainty());
		properties.setDescription(sim.getDescription());
		properties.setEvent(sim.getEvent());
		properties.setHeadline(sim.getHeadline());
		properties.setSeverity(sim.getSeverity());
		properties.setUrgency(sim.getUrgency());
		
		AlertFeatures features = new AlertFeatures();
		features.setProperties(properties);

		NWSUpdaterService.instance.setSimulationAlert(properties);
		
		List<Location> locs = NWSUpdaterService.instance.getLocationByCoords(sim.getLat(), sim.getLon());
		
		if(locs != null) {
			String shortenedURL = URLShortener.shortenedUrlForAlert(locs.get(0).getId(), properties.getId());

			String topic = locs.get(0).getTopicArn();
			SnsUtils.checkAllEmailForConfirmation(locs);
			SnsPublishMessage.setSnsPublishMessage(features, sim.getCityName(), topic, shortenedURL);
		}

		return sim;
	}
}
