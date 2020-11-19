package com.aca.nwsupdater.model.webapp;

import java.util.Objects;

public class Alert {
	private Integer id;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Alert alert = (Alert) o;
		return Objects.equals(id, alert.id) &&
				Objects.equals(name, alert.name);
	}
}
