
package com.healthiq.util;

import com.healthiq.info.ActivityInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Holds utility and helper methods for the application
 *
 * @author Alex Chernyavsky
 */


public class ActivityHelper {

	/**
	 * Calculates rate change per minute for a given activity
	 *
	 * @param properties - System properties map
	 * @param aInfo      - Activity info object
	 * @return - Rate increase/decrease value
	 */
	public static Double calculateActivityRateChangePerMinute(Map<String, String> properties, ActivityInfo aInfo) {
		double ratePerMinute = 0.0;


		if (aInfo.getType().equals(ActivityType.FOOD.toString())) {
			ratePerMinute = roundRate(aInfo.getIndex() / Double.parseDouble(properties.get("FOOD_RATE_DURATION_MIN")));
		} else if (aInfo.getType().equals(ActivityType.EXERCISE.toString())) {
			ratePerMinute = roundRate(aInfo.getIndex() / Double.parseDouble(properties.get("EXERCISE_RATE_DURATION_MIN"))) * -1.0;
		}

		return ratePerMinute;
	}

	/**
	 * Calculate activity's last index
	 *
	 * @param properties - System properties map
	 * @param startIndex - Activity start index
	 * @param timeLength - Duration in minutes
	 * @return - Last activity index
	 */
	public static Integer calculateEndIndex(Map<String, String> properties, Integer startIndex, Integer timeLength) {

		int endIndex = startIndex + timeLength;
		int minutesIntTimeLine = Integer.parseInt(properties.get("MINUTES_IN_TIME_LINE"));
		//do not continue past the end of the timeline
		if (endIndex > minutesIntTimeLine) {
			endIndex = minutesIntTimeLine;
		}
		return endIndex;
	}

	/**
	 * Return activity's duration
	 *
	 * @param properties   - System properties map
	 * @param activityType - Type of activity
	 * @return - Duration value
	 */
	public static Integer getTimeLengthForActivity(Map<String, String> properties, String activityType) {

		if (activityType.equals(ActivityType.EXERCISE.toString())) {
			return Integer.parseInt(properties.get("EXERCISE_RATE_DURATION_MIN"));
		} else if (activityType.equals(ActivityType.FOOD.toString())) {
			return Integer.parseInt(properties.get("FOOD_RATE_DURATION_MIN"));
		} else {
			return 0;
		}
	}

	/**
	 * Find out starting index for an activity
	 *
	 * @param properties - System properties map
	 * @param aInfo      - Activity info object
	 * @return - Activity's begin index
	 */
	public static Integer calculateActivityStartIndex(Map<String, String> properties, ActivityInfo aInfo) {
		return ((aInfo.getHour() - Integer.parseInt(properties.get("DAY_BEGINNING_HOUR"))) *
				Integer.parseInt(properties.get("MINUTES_IN_HOUR"))) + aInfo.getMinute();
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

	/**
	 * Calculates sum total of all increase/decrease rates for a given minute
	 *
	 * @param allActivitiesList - List of all activitites
	 * @param index             - Represents a minute in timeline
	 * @return - Sum total
	 */
	public static Double calculateSugarInOneMinuteOfTimeLine(List<List<Double>> allActivitiesList, Integer index) {

		double totalRateValue = 0.0;
		for (List aList : allActivitiesList) {
			totalRateValue += (double) aList.get(index);
		}
		return totalRateValue;
	}

	/**
	 * Builds an activity timeline for a given ActivityInfo object
	 *
	 * @param properties - System properties map
	 * @param aInfo      - Activity info object
	 * @return - List representing Activity timeline
	 */
	public static List<Double> buildActivityTimeLine(Map<String, String> properties, ActivityInfo aInfo) {
		List<Double> aTimeLine = new ArrayList<>(Collections.nCopies(Integer.parseInt(properties.get("MINUTES_IN_TIME_LINE")), 0.0));

		int startIndex = ActivityHelper.calculateActivityStartIndex(properties, aInfo);
		int timeLength = ActivityHelper.getTimeLengthForActivity(properties, aInfo.getType());
		int endIndex = ActivityHelper.calculateEndIndex(properties, startIndex, timeLength);
		double rateValuePerMinute = ActivityHelper.calculateActivityRateChangePerMinute(properties, aInfo);

		for (int i = startIndex; i < endIndex; i++) {
			aTimeLine.set(i, rateValuePerMinute);
		}

		return aTimeLine;
	}
}
