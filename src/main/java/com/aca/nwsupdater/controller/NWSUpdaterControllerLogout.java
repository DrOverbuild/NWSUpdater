package com.aca.nwsupdater.controller;

import com.aca.nwsupdater.service.NWSUpdaterService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;

@Path("/logout")
public class NWSUpdaterControllerLogout {
	@GET
	public String logout(@HeaderParam("Authorization") String auth) {
		NWSUpdaterService.instance.logout(auth);
		return "Success";
	}
}
