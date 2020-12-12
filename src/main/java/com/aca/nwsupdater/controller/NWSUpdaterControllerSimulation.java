package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.Simulation;
import com.aca.nwsupdater.service.NWSUpdaterService;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

public class NWSUpdaterControllerSimulation {
	@POST
	@Path("/sim")
	@Produces(MediaType.APPLICATION_JSON)
	public Simulation simulation(Simulation simulation) {
		return NWSUpdaterService.instance.simulation(simulation);
	}
}
