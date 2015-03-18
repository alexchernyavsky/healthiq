package com.healthiq.business;

import com.healthiq.info.ActivityInfo;
import com.healthiq.util.ActivityHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds main business logic
 *
 * @author Alex Chernyavsky
 */

public class BloodSugarRateCalculator {

	public void calculateBloodSugar(List<ActivityInfo> listOfActivities) {

		//initialize time line array
		List<Integer> bloodSugarTimeLine = new ArrayList<>(Collections.nCopies(ActivityHelper.MINUTES_IN_TIME_LINE, 0));
		List<Integer> glycationTimeLine = new ArrayList<>(Collections.nCopies(ActivityHelper.MINUTES_IN_TIME_LINE, 0));

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


	}
}
