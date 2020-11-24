package com.aca.nwsupdater.service;

import com.aca.nwsupdater.model.webapp.ErrorMessage;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ServiceUtil {
	public static void sendError(int code, String msg) {
		ErrorMessage errmsg = new ErrorMessage(code, msg);
		Response response = Response.status(400)
				.entity(errmsg)
				.build();
		throw new WebApplicationException(response);
	}
}
