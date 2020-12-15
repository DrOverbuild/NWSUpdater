package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.nws.Point;
import com.aca.nwsupdater.service.NWSUpdaterService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/point")
public class NWSUpdaterControllerPoint {
	@GET
	@Path("/{loc_id}")
	public Point getPoint(@PathParam("loc_id") Integer locationId) {
		return NWSUpdaterService.instance.getPoint(locationId);
	}
}
