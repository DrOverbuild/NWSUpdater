package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.AlertProperties;
import com.aca.nwsupdater.service.NWSUpdaterService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@Path("/alert")
public class NWSUpdaterControllerAlert {
	@GET
	@Path("/{alert_id}")
	@Produces
	public static AlertProperties getAlert(@PathParam("alert_id") String alertId) {
		return NWSUpdaterService.instance.getAlert(alertId);
	}
}
