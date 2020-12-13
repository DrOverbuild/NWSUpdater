package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.webapp.ErrorMessage;
import com.aca.nwsupdater.service.NWSUpdaterService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/logout")
public class NWSUpdaterControllerLogout {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ErrorMessage logout(@HeaderParam("Authorization") String auth) {
		NWSUpdaterService.instance.logout(auth);
		return new ErrorMessage(10, "Logged out");
	}
}
