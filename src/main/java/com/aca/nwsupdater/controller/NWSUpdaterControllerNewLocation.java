package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.TestLocation;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.service.NWSUpdaterService;

import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/newloc")
public class NWSUpdaterControllerNewLocation {
	@POST
	@Path("/create")
	public String createNewLocationCoords(TestLocation location) {
		NWSUpdaterService.instance.createNewLocationCoords(location);
		return "Success";
	}
}
