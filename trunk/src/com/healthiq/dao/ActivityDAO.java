
package com.healthiq.dao;

import com.healthiq.info.ActivityInfo;
import com.healthiq.util.DataAccessHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Activity info class
 *
 * @author Alex Chernyavsky
 */

public class ActivityDAO {
	private Map<String, String> _props;

	/**
	 * Constructor
	 *
	 * @param properties - System properties map
	 * @throws Exception
	 */
	public ActivityDAO(Map<String, String> properties) throws Exception {
		DataAccessHelper.checkForRequiredKeys(properties);

		_props = properties;
	}

	/**
	 * Return the list of exercise activities
	 *
	 * @return - List of exercise activities
	 */
	public List<ActivityInfo> retrieveExerciseActivities() {

		List<ActivityInfo> aList = new ArrayList();

		try {
			Connection connection = DataAccessHelper.getConnection(_props);

			PreparedStatement ps = connection.prepareStatement("select * from exercise_index");
			ResultSet rs = ps.executeQuery();


			while (rs.next()) {
				ActivityInfo aInfo = new ActivityInfo();
				aInfo.setName(rs.getString("exercise_name"));
				aInfo.setIndex(rs.getInt("exercise_index"));
				aList.add(aInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return aList;
	}

	/**
	 * Return the list of food activities
	 *
	 * @return - List of food activities
	 */
	public List<ActivityInfo> retrieveFoodActivities() {

		List<ActivityInfo> aList = new ArrayList();

		try {
			Connection connection = DataAccessHelper.getConnection(_props);

			PreparedStatement ps = connection.prepareStatement("select * from food_index");
			ResultSet rs = ps.executeQuery();


			while (rs.next()) {
				ActivityInfo aInfo = new ActivityInfo();
				aInfo.setName(rs.getString("food_name"));
				aInfo.setIndex(rs.getInt("glycemic_index"));
				aList.add(aInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return aList;
	}
}
