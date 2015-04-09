

package com.healthiq.util;

import com.healthiq.info.DataItemInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility methods for data access
 *
 * @author Alex Chernyavsky
 */

public class DataAccessHelper {

	public static Connection getConnection(Map<String, String> connectionInfo) throws Exception {
		Connection connection;

		checkForRequiredKeys(connectionInfo);
		Class.forName("org.postgresql.Driver");

		String url = connectionInfo.get("JDBC_URL");
		String user = connectionInfo.get("USER_NAME");
		String password = connectionInfo.get("DB_PASSWORD");

		try {
			connection = DriverManager.getConnection(url, user, password);

		} catch (SQLException e) {
			throw new Exception("Failed to connect to DB", e);
		}

		return connection;
	}

	public static Connection getSQLiteConnection(Map<String, String> connectionInfo) throws Exception {
		Connection connection;

		Class.forName("org.sqlite.JDBC");
		String SQLITE_DB_PATH = connectionInfo.get("SQLITE_DB_PATH");

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + SQLITE_DB_PATH);

		} catch (SQLException e) {
			throw new Exception("Failed to connect to DB", e);
		}

		return connection;
	}

	public static void checkForRequiredKeys(Map<String, String> connectionInfo) throws Exception {
		if (!connectionInfo.containsKey("JDBC_URL")) {
			throw new Exception("JDBC url is missing");
		}
		if (!connectionInfo.containsKey("USER_NAME")) {
			throw new Exception("DB User id is missing");
		}
		if (!connectionInfo.containsKey("DB_PASSWORD")) {
			throw new Exception("DB password  is missing");
		}
	}

	/**
	 * Converts Input stream from request body into a String object
	 *
	 * @param in Input Stream
	 * @return String representing input stream
	 * @throws Exception
	 */
	public static String streamToString(InputStream in) throws Exception {
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		String read = br.readLine();

		while (read != null) {
			sb.append(read);
			read = br.readLine();
		}

		return sb.toString();
	}

	/**
	 * Helper method building a scaled timeline
	 *
	 * @param props    - Propperties object
	 * @param dataList - List with data values
	 * @return - List with timed data items
	 */
	public static List<DataItemInfo> buildDateItemList(Map<String, String> props, List<? extends Number> dataList) {
		List<DataItemInfo> hTimeLine = new ArrayList<>();

		//the original list has one data item per minute.  To make graphs easier ot read,
		//build time line with certain minute intervals between data points
		int minuteInterval = Integer.parseInt(props.get("TIME_SCALE_INTERVAL_MIN"));
		int hourValue = Integer.parseInt(props.get("DAY_BEGINNING_HOUR"));
		int minuteInHour = Integer.parseInt(props.get("MINUTES_IN_HOUR"));
		int minuteValue = 0;
		int timeIndex = 0;
		String timeVal;

		//set hour and minute values for the scaled timeline
		for (int i = 0; i <= dataList.size(); i += minuteInterval) {
			if (i == 0) {
				timeVal = hourValue + ".00";
			} else {
				timeIndex = i - 1;
				minuteValue += minuteInterval;
				if (minuteValue == minuteInHour) {
					hourValue++;
					minuteValue = 0;
				}
				timeVal = hourValue + "." + minuteValue;
			}

			DataItemInfo dItem = new DataItemInfo();
			dItem.setTime(timeVal);
			dItem.setValue(String.valueOf(dataList.get(timeIndex)));
			hTimeLine.add(dItem);
		}

		return hTimeLine;
	}
}
