package com.healthiq.util;

/**
 * Holds Activity types
 */
public enum ActivityType {

	FOOD
			("FOOD"),
	EXERCISE
			("EXERCISE"),
	NO_ACTIVITY
			("NO_ACTIVITY");
	private String propertyName;


	private ActivityType(String _propertyName) {
		this.propertyName = _propertyName;
	}

	@Override
	public String toString() {
		return this.propertyName;
	}
}
