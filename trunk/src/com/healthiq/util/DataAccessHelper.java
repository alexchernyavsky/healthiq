

package com.healthiq.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
}
