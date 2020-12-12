package com.aca.nwsupdater.service.sns;

import com.aca.nwsupdater.model.AlertFeatures;
import com.aca.nwsupdater.model.AlertProperties;
import com.aca.nwsupdater.model.sns.Simulation;

public class SimulationAlertService {
	
	public static Simulation simulate(Simulation sim) {
		AlertProperties properties = new AlertProperties();
		properties.setAreaDes(sim.getAreaDesc());
		properties.setCertainty(sim.getCertainty());
		properties.setDescription(sim.getDescription());
		properties.setEvent(sim.getEvent());
		properties.setHeadline(sim.getHeadline());
		properties.setSeverity(sim.getSeverity());
		properties.setUrgency(sim.getUrgency());
		
		AlertFeatures features = new AlertFeatures();
		features.setProperties(properties);
		
		SnsPublishMessage.setSnsPublishMessage(features, sim.getCityName());
		
		return sim;
	}
}
