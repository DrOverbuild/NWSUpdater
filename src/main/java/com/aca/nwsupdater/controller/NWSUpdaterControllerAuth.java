package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.webapp.User;
import com.aca.nwsupdater.service.NWSUpdaterService;
import com.aca.nwsupdater.service.Session;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
public class NWSUpdaterControllerAuth {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Session authenticate(User loginInfo) {
		return NWSUpdaterService.instance.authenticate(loginInfo);
	}
}
