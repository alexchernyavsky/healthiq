
package com.healthiq.util;

/**
 * Holds utility and helper methods for the application
 *
 * @author Alex Chernyavsky
 */


public class ActivityHelper {

	//TODO globals for now will move to a property file later
	public static Integer NORMAL_SUGAR_COUNT = 80;
	public static Integer STABILIZATION_RATE = 1;
	public static Integer FOOD_RATE_DURATION_MIN = 120;
	public static Integer EXERCISE_RATE_DURATION_MIN = 60;
	public static Integer MINUTES_IN_TIME_LINE = 840;
	public static Integer DAY_BEGINNING_HOUR = 8;

	/**
	 * Calculates increase or decrease in blood sugar level
	 *
	 * @param aType        - Activity type
	 * @param currentLevel - current blood sugar level
	 * @param index        - glycemic or exercise index
	 * @return - Change rate per minute (positive or negative
	 */
	public static Double calculateBloodSugarRateChangePerMinute(ActivityType aType, Integer currentLevel, Integer index) {
		double ratePerMinute = 0;

		switch (aType) {
			case NO_ACTIVITY:
				if (currentLevel < NORMAL_SUGAR_COUNT) {
					ratePerMinute = STABILIZATION_RATE;
				} else {
					ratePerMinute = -STABILIZATION_RATE;
				}
				break;
			case FOOD:
				ratePerMinute = index / FOOD_RATE_DURATION_MIN;
				break;
			case EXERCISE:
				ratePerMinute = (index / EXERCISE_RATE_DURATION_MIN) * -1;
				break;
			default:
				break;
		}


		return ratePerMinute;

	}
}
