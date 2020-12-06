package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.model.webapp.NewLocation;
import com.aca.nwsupdater.service.NWSUpdaterService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("location")
public class NWSUpdaterControllerLocation {
	@GET
	@Path("/{locationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Location getLocation(@HeaderParam("Authorization") String auth, @PathParam("locationId") Integer locationId) {
		return NWSUpdaterService.instance.getLocation(auth, locationId);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Location newLocation(@HeaderParam("Authorization") String auth, NewLocation newLocation) {
		return NWSUpdaterService.instance.newLocation(auth, newLocation);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Location updateLocation(@HeaderParam("Authorization") String auth, Location location) {
		return NWSUpdaterService.instance.updateLocation(auth, location);
	}

	@DELETE
	@Path("/{locationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> deleteLocation(@HeaderParam("Authorization") String auth, @PathParam("locationId") Integer locationId) {
		return NWSUpdaterService.instance.deleteLocation(auth, locationId);
	}
}
