package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.webapp.HomePageModel;
import com.aca.nwsupdater.service.NWSUpdaterService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.awt.*;

@Path("/app")
public class NWSUpdaterController {
	NWSUpdaterService service = new NWSUpdaterService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public HomePageModel homePage(@HeaderParam("Authorization") String auth) {
		return service.homePage(auth);
	}
}
