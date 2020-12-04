package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.service.NWSUpdaterService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;

public class NWSUpdaterControllerNewLocation {
	@Path("/newloc")
	public class NWSUpdaterControllerLogout {
		@GET
		@Path("/coords")
		public String getNewLocationCoords(@HeaderParam("Authorization") String auth) {
			NWSUpdaterService.instance.getNewLocationCoords(auth);
			return "Success";
		}
	}
}
