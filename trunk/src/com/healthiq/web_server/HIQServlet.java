/* @(#)HIQServlet
 * 
 * Copyright (c) 2015 Cepheid. All Rights Reserved.
 *
 */

package com.healthiq.web_server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * System's servlet
 *
 * @author Alex Chernyavsky
 */

public class HIQServlet implements ServletContextListener {

	private static String CONFIG_PATH = "";

	public static String getConfigPath() {
		return CONFIG_PATH;
	}

	/**
	 * Load the Command processor once when the servlet starts.
	 * Necessary to set config path
	 *
	 * @param arg0 Servlet Context
	 */

	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext sc = arg0.getServletContext();

		String SYSTEM_PROPERTIES =
				File.separator + "WEB-INF" + File.separator + "config" + File.separator + "system.properties";

		try {
			//getting the properties as a stream to make it compatible with Windows and linux
			InputStream inputStream = sc.getResourceAsStream(SYSTEM_PROPERTIES);
			inputStream.close();

			//Path to WebApp
			String pathToWebAppStr = sc.getRealPath("");
			String configPath = File.separator + "WEB-INF" + File.separator + "config";

			//absolutePath to config
			CONFIG_PATH = pathToWebAppStr + configPath;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Context Destroyer
	 *
	 * @param arg0 Servlet Context event
	 */
	public void contextDestroyed(ServletContextEvent arg0) {

	}
}
