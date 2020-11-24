package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.model.webapp.User;
import com.aca.nwsupdater.service.NWSUpdaterService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/user")
public class NWSUpdaterControllerUser {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@HeaderParam("Authorization") String auth) {
		return NWSUpdaterService.instance.getUser(auth);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User updateUser(@HeaderParam("Authorization") String auth, User user) {
		return NWSUpdaterService.instance.updateUser(auth, user);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User createUser(User user) {
		return NWSUpdaterService.instance.createUser(user);
	}
}
