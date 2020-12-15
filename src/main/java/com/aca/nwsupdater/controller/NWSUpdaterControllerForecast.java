package com.aca.nwsupdater.controller;

import java.util.List;

import com.aca.nwsupdater.model.ForecastPeriods;
import com.aca.nwsupdater.model.webapp.Location;
import com.aca.nwsupdater.service.NWSUpdaterService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/forecast")
public class NWSUpdaterControllerForecast {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ForecastPeriods> getForecastPeriods(@QueryParam("lat") Double lat, @QueryParam("lon") Double lon){
		return NWSUpdaterService.instance.getForecastPeriods(lat, lon);
	}
}
