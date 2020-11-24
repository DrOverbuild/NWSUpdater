package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.webapp.Alert;
import com.aca.nwsupdater.service.NWSUpdaterService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/alerts")
public class NWSUpdaterControllerAlerts {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Alert> getAlerts() {
		return NWSUpdaterService.instance.getAlerts();
	}
}
