/* @(#)SimRunner
 * 
 * Copyright (c) 2015 Cepheid. All Rights Reserved.
 *
 */

package com.healthiq.client;

import com.google.gson.Gson;
import com.healthiq.business.BloodSugarRateCalculator;
import com.healthiq.info.ActivityInfo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * To be used with a command prompt
 *
 * @author Alex Chernyavsky
 */

public class SimRunner {

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Wrong number of arguments provided.  USAGE: first argument is a full config path and the second argument is the path to the input file");
			return;
		}

		String configPath = args[0];
		String inputFilePath = args[1];

		try {
			BloodSugarRateCalculator calculator = new BloodSugarRateCalculator(configPath);

			String inputFile = new String(Files.readAllBytes(Paths.get(inputFilePath)));
			List<ActivityInfo> aInfoList = (ArrayList) ActivityInfo.fromJSONArray(inputFile);

			calculator.buildTimeLines(aInfoList);

			Gson gson = new Gson();

			System.out.println("\nBLOOD SUGAR RATE: \n" + gson.toJson(calculator.getBloodSugarTimeLine()));
			System.out.println("\n\nGLYCATION RATE: \n" + gson.toJson(calculator.getGlycationTimeLine()));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}