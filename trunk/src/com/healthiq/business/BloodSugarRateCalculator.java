package com.healthiq.business;

import com.healthiq.dao.ActivityDAO;
import com.healthiq.info.ActivityInfo;
import com.healthiq.util.ActivityHelper;
import com.sun.jmx.remote.internal.ArrayQueue;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds main business logic
 *
 * @author Alex Chernyavsky
 */

public class BloodSugarRateCalculator {
	private static final Logger LOGGER = Logger.getLogger(BloodSugarRateCalculator.class.getName());
	//initialize time line array
	private List<Double> _bloodSugarTimeLine;
	private List<Integer> _glycationTimeLine;
	//keep property values as strings in case if strings are added later
	private Map<String, String> _propertiesMap;

	/**
	 * Constructor
	 *
	 * @param configPath - Configuration path
	 */
	public BloodSugarRateCalculator(String configPath) throws Exception {

		try {
			//initialize properties
			Properties props = buildProperties(configPath + File.separator + "system.properties");

			//build properties Map for further use (more generic)
			if (props != null) {
				_propertiesMap = new HashMap();
				for (String key : props.stringPropertyNames()) {
					_propertiesMap.put(key, props.getProperty(key));
				}
			}

			if (_propertiesMap == null || _propertiesMap.isEmpty()) {
				throw new Exception("Failed to initialize properties");
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error occurred while initialising BloodSugarRateCalculator", e);
			throw e;
		}
	}

	/**
	 * Build a properties object for a given path
	 *
	 * @param configPath Path to the properties file
	 * @return Properties object
	 * @throws Exception
	 */
	private static Properties buildProperties(String configPath) throws Exception {
		Properties prop;

		File config = new File(configPath);

		FileInputStream fileInput = new FileInputStream(config);
		prop = new Properties();
		prop.load(fileInput);
		fileInput.close();

		return prop;
	}

	public Map<String, String> getPropertiesMap() {
		return _propertiesMap;
	}

	/**
	 * Main method for calculating sugar level values
	 *
	 * @param listOfActivities - List of Activity objects
	 */
	public void buildTimeLines(List<ActivityInfo> listOfActivities) {

		//initialize timelines
		int minutesInTimeLine = Integer.parseInt(_propertiesMap.get("MINUTES_IN_TIME_LINE"));
		_bloodSugarTimeLine = new ArrayList<>(Collections.nCopies(minutesInTimeLine, 0.0));
		_glycationTimeLine = new ArrayList<>(Collections.nCopies(minutesInTimeLine, 0));
		List<List<Double>> allActivitiesList = new ArrayQueue<>(listOfActivities.size());

		LOGGER.info("Begin analysys");

		//build a timeline for each activity
		for (ActivityInfo aInfo : listOfActivities) {

			allActivitiesList.add(ActivityHelper.buildActivityTimeLine(_propertiesMap, aInfo));
		}

		double currentRate = 0.0;
		int currGlycation = 0;
		int normalSugarCount = Integer.valueOf(_propertiesMap.get("NORMAL_SUGAR_COUNT"));
		int normalizationRate = Integer.valueOf(_propertiesMap.get("NORMALIZATION_RATE"));
		int glycationBeginsLevel = Integer.valueOf(_propertiesMap.get("GLYCATION_BEGINS_LEVEL"));
		int glycationIncrement = Integer.valueOf(_propertiesMap.get("GLYCATION_INCREMENT"));

		//combine all timelines into one and add values to the glycation table
		for (int i = 0; i < minutesInTimeLine; i++) {
			double sumRate = ActivityHelper.calculateSugarInOneMinuteOfTimeLine(allActivitiesList, i);

			//day starts at a certain level
			if (i == 0) {
				currentRate = normalSugarCount;
			}

			//handle normalization
			if (sumRate == 0 && (currentRate > 0 && currentRate != normalizationRate)) {
				if (currentRate > normalSugarCount) {
					currentRate -= normalizationRate;

					//round to NORMAL_SUGAR_COUNT
					currentRate = currentRate < normalSugarCount ? normalSugarCount : currentRate;
				} else if (currentRate < normalSugarCount) {
					currentRate += normalizationRate;

					//round to NORMAL_SUGAR_COUNT
					currentRate = currentRate > normalSugarCount ? normalSugarCount : currentRate;
				}
			}

			currentRate += sumRate;
			currentRate = ActivityHelper.roundRate(currentRate);
			_bloodSugarTimeLine.set(i, currentRate);

			//process glycation
			if (currentRate > glycationBeginsLevel) {
				currGlycation += glycationIncrement;

				_glycationTimeLine.set(i, currGlycation);
			}
			//TODO find out if resetting is necessary
			//reset current glycation when blood sugar normalizes
			if (currentRate == normalizationRate) {
				currGlycation = 0;
			}
		}

		LOGGER.info("End analysys");
	}

	/**
	 * Getter for blood sugar timeliene
	 *
	 * @return - List representing blood sugar timeline
	 */
	public List<Double> getBloodSugarTimeLine() {
		return _bloodSugarTimeLine;
	}

	/**
	 * Getter for glycation level timeline
	 *
	 * @return - List representing glycation level
	 */
	public List<Integer> getGlycationTimeLine() {
		return _glycationTimeLine;
	}

	/**
	 * Get the list of exercise activities
	 *
	 * @return - List of exercise activities
	 */
	public List<ActivityInfo> getExerciseActivities() throws Exception {
		try {
			ActivityDAO dao = new ActivityDAO(_propertiesMap);
			return dao.retrieveExerciseActivities();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error during retrieval of exercise activities", e);
			throw e;
		}
	}

	/**
	 * Get the list of food activities
	 *
	 * @return - List of food activities
	 */
	public List<ActivityInfo> getFoodActivities() throws Exception {
		try {
			ActivityDAO dao = new ActivityDAO(_propertiesMap);
			return dao.retrieveFoodActivities();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error during retrieval of food activities", e);
			throw e;
		}
	}
}
