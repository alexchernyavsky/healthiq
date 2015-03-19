
package com.healthiq.util;

import com.healthiq.info.ActivityInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Holds utility and helper methods for the application
 *
 * @author Alex Chernyavsky
 */


public class ActivityHelper {

	//TODO globals for now will move to a property file later
	public static Integer NORMAL_SUGAR_COUNT = 80;
	public static Integer NORMALIZATION_RATE = 1;
	public static Integer FOOD_RATE_DURATION_MIN = 120;
	public static Integer EXERCISE_RATE_DURATION_MIN = 60;
	public static Integer MINUTES_IN_TIME_LINE = 480;
	public static Integer DAY_BEGINNING_HOUR = 8;
	public static Integer GLYCATION_LEVEL = 150;

	/**
	 * Calculates increase or decrease in blood sugar level
	 *
	 * @param aType        - Activity type
	 * @param currentLevel - current blood sugar level
	 * @param index        - glycemic or exercise index
	 * @return - Change rate per minute (positive or negative
	 */
	public static Double calculateBloodSugarRateChangePerMinute(String aType, Double currentLevel, Integer index) {
		double ratePerMinute = 0.0;

		if (aType.equals(ActivityType.NO_ACTIVITY.toString()))
			if (currentLevel < NORMAL_SUGAR_COUNT) {
				ratePerMinute = NORMALIZATION_RATE;
			} else {
				ratePerMinute = -NORMALIZATION_RATE;
			}

		else if (aType.equals(ActivityType.FOOD.toString())) {
			ratePerMinute = roundRate(index / (double) FOOD_RATE_DURATION_MIN);
		} else if (aType.equals(ActivityType.EXERCISE.toString())) {
			ratePerMinute = roundRate(index / (double) EXERCISE_RATE_DURATION_MIN) * -1.0;
		}

		return ratePerMinute;
	}

	/**
	 * Calculate activity's last index
	 *
	 * @param startIndex - Activity start index
	 * @param timeLength - Duration in minutes
	 * @return - Last activity index
	 */
	public static Integer calculateEndIndex(Integer startIndex, Integer timeLength) {

		int endIndex = startIndex + timeLength;

		//do not continue past the end of the timeline
		if (endIndex > ActivityHelper.MINUTES_IN_TIME_LINE) {
			endIndex = ActivityHelper.MINUTES_IN_TIME_LINE;
		}
		return endIndex;
	}

	/**
	 * Return activity's duration
	 *
	 * @param activityType - Type of activity
	 * @return - Duration value
	 */
	public static Integer getTimeLengthForActivity(String activityType) {

		if (activityType.equals(ActivityType.EXERCISE.toString())) {
			return EXERCISE_RATE_DURATION_MIN;
		} else if (activityType.equals(ActivityType.FOOD.toString())) {
			return FOOD_RATE_DURATION_MIN;
		} else {
			return 0;
		}
	}

	/**
	 * Find out starting index for an activity
	 *
	 * @param aInfo - Activity info object
	 * @return - Activity's begin index
	 */
	public static Integer calculateActivityStartIndex(ActivityInfo aInfo) {
		return ((aInfo.getHour() - DAY_BEGINNING_HOUR) * 60) + aInfo.getMinute();
	}

	/**
	 * Rounds given value to two decimal points
	 *
	 * @param val - Value to round
	 * @return - Rounded value
	 */
	public static Double roundRate(Double val) {
		return new BigDecimal(val.toString()).setScale(2, RoundingMode.CEILING.HALF_UP).doubleValue();
	}
}
