package com.aca.nwsupdater.service;

import java.util.regex.Pattern;

public class Validator {
	NWSUpdaterService service;

	public Validator(NWSUpdaterService service) {
		this.service = service;
	}

	public String validateAuth(String auth) {
		if (auth == null) {
			ServiceUtils.sendError(1, "Not logged in: session token required");
		}

		String[] components = auth.split(" ");

		if (components.length != 2) {
			ServiceUtils.sendError(1, "Not logged in: invalid session token");
		}

		if (!components[0].equals("Bearer")) {
			ServiceUtils.sendError(1, "Not logged in: session token must be in form of Bearer Token");
		}

		return components[1];
	}

	public int validateToken(String token) {
		int userId = service.getSessionManager().getSession(token);

		if (userId == -1) {
			ServiceUtils.sendError(1, "Not logged in: no session for given token");
		}

		return userId;
	}

	public void validateUserId(int id) {
		if (id < 0) {
			ServiceUtils.sendError(2, "Invalid User Id: " + id);
		}

		if (service.getDao().getUser(id) == null) {
			ServiceUtils.sendError(2, "User ID does not exist: " + id);
		}
	}

	public void validateUserEmail(String email) {
		boolean invalid =
				email == null ||
				email.isEmpty() ||
				!email.contains("@");

		if (invalid) {
			ServiceUtils.sendError(3, "Invalid email: " + email);
		}
	}

	public void validateUserPhone(String phone) {
		String patterns
				= "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
				+ "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
				+ "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

		Pattern pattern = Pattern.compile(patterns);

		if (phone == null || !pattern.matcher(phone).matches()) {
			ServiceUtils.sendError(4, "Invalid phone number: " + phone);
		}
	}

	public void validateUserPassword(String password) {
		if (password == null || password.length() < 6 || password.length() > 16) {
			ServiceUtils.sendError(5, "Invalid password");
		}
	}
}
