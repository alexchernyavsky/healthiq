package com.healthiq.business;

import com.healthiq.info.ActivityInfo;
import com.healthiq.util.ActivityHelper;
import com.healthiq.util.ActivityType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds main business logic
 *
 * @author Alex Chernyavsky
 */

public class BloodSugarRateCalculator {

	//initialize time line array
	private static List<Double> bloodSugarTimeLine = new ArrayList<>(Collections.nCopies(ActivityHelper.MINUTES_IN_TIME_LINE, 0.0));
	private static List<Integer> glycationTimeLine = new ArrayList<>(Collections.nCopies(ActivityHelper.MINUTES_IN_TIME_LINE, 0));

	/**
	 * Main method for calculating sugar level values
	 *
	 * @param listOfActivities - List of Activity objects
	 * @return - List with sugar levels
	 */
	public static List<Double> calculateBloodSugar(List<ActivityInfo> listOfActivities) {


		//1. For each activity in the list
		//a. find out rate value
		//b. begin to add/subtract rate to every minute of activity's duration
		//c. identify gaps of no activity
		//i. pick into the next activity's hour
		//  if it's not immediately after the current activity ended
		// begin to stabilize sugar level by increasing or decreasing the level
		// depending on the current blood sugar count

		// Keep stabilizing until next activity begins or
		// 80 is reached in which case, fill in the rest of the
		// timeline with 80 until beginning of the next activity

		//Glycation index can be updated during same pass, just check the current value after it's been updated

		int currActivityIndex = -1;
		int activityStartIndex = 0;
		double currentRate = ActivityHelper.NORMAL_SUGAR_COUNT;

		for (ActivityInfo aInfo : listOfActivities) {

			activityStartIndex = ActivityHelper.calculateActivityStartIndex(aInfo);
			int timeLength = ActivityHelper.getTimeLengthForActivity(aInfo.getType());

			//identify a gap between activities and fill it
			if (activityStartIndex > (currActivityIndex + 1)) {
				//when calculating length, subtract one, because this is the index for the next activity
				int adjustmentLength = (activityStartIndex - currActivityIndex - 1);
				currentRate = adjustNoActivityTimeLineValues(currentRate, currActivityIndex, adjustmentLength);
				currActivityIndex += adjustmentLength;
			}
			//readjust already set values, e.g. an activity begins before previous one ends
			else if (activityStartIndex < currActivityIndex) {
				//TODO work in progress
				//get the last sugar rate before this activity begins
				double tempRate = bloodSugarTimeLine.get(activityStartIndex - 1);
				//find out rate of change
				double tempAddend = ActivityHelper.calculateBloodSugarRateChangePerMinute(aInfo.getType(), tempRate, aInfo.getIndex());
				//set time values and get the  new rate
				currentRate = setTimeLineValues(currentRate, activityStartIndex, timeLength, tempAddend);

			}

			//find out rate of change
			double addend = ActivityHelper.calculateBloodSugarRateChangePerMinute(aInfo.getType(), currentRate, aInfo.getIndex());

			//set time values and get the  new rate
			double newRate = setTimeLineValues(currentRate, activityStartIndex, timeLength, addend);

			//update current values
			currActivityIndex += timeLength;
			currentRate = newRate;

		}

		return bloodSugarTimeLine;
	}

	/**
	 * Sets sugar level values for a given activities timeline
	 *
	 * @param currentRate - Current sugar level
	 * @param startIndex  - Represents minute index in the timeline
	 * @param timeLength  - Activities span
	 * @param addend      - Activities index (positive or negative)
	 * @return - Last set rate
	 */
	private static Double setTimeLineValues(Double currentRate, Integer startIndex, Integer timeLength, Double addend) {

		int endIndex = ActivityHelper.calculateEndIndex(startIndex, timeLength);
		double sugarLevel = currentRate;

		for (int i = startIndex; i < endIndex; i++) {
			sugarLevel = ActivityHelper.roundRate(sugarLevel + addend + bloodSugarTimeLine.get(i));
			bloodSugarTimeLine.set(i, sugarLevel);
		}

		return sugarLevel;
	}

	/**
	 * Sugar level shall be normalized when there is no activity
	 *
	 * @param currentRate - Last sugar level value
	 * @param startIndex  - Normalization start minute
	 * @param timeLength  - Time period until next activity begins
	 * @return - Last calculated sugar level
	 */
	private static Double adjustNoActivityTimeLineValues(Double currentRate, Integer startIndex, Integer timeLength) {

		int endIndex = ActivityHelper.calculateEndIndex(startIndex, timeLength);
		double sugarLevel = currentRate;
		double addend = ActivityHelper.calculateBloodSugarRateChangePerMinute(ActivityType.NO_ACTIVITY.toString(), currentRate, startIndex);

		//make sure when the level stabilizes during given stabilization period
		// it stays so until next activity
		for (int i = startIndex; i < endIndex; i++) {
			sugarLevel = ActivityHelper.roundRate(sugarLevel + addend + bloodSugarTimeLine.get(i));

			//make sure we're not going over or under 80 when normalizing
			if ((addend > 0 && sugarLevel > ActivityHelper.NORMAL_SUGAR_COUNT) ||
					(addend < 0 && sugarLevel < ActivityHelper.NORMAL_SUGAR_COUNT)) {
				sugarLevel = ActivityHelper.NORMAL_SUGAR_COUNT;
			}

			bloodSugarTimeLine.set(i, sugarLevel);
		}
		return sugarLevel;
	}


}
