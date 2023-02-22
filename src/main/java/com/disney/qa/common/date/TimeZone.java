package com.disney.qa.common.date;

public enum TimeZone {
	ET("US/Eastern"), MST("US/Arizona"), CT("US/Central"), MT("US/Mountain"), PT("US/Pacific");
	private String id;

	private TimeZone(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
