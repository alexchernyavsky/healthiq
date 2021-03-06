

package com.healthiq.web_server;

import com.google.gson.Gson;
import com.healthiq.business.BloodSugarRateCalculator;
import com.healthiq.info.ActivityInfo;
import com.healthiq.info.DataItemInfo;
import com.healthiq.util.ActivityHelper;
import com.healthiq.util.DataAccessHelper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes all client requests
 *
 * @author Alex Chernyavsky
 */
@Path("/")
public class RequestProcessor {


	@GET
	@Path("/foodactivities")
	/**
	 * Build and return the list of food activities from DB
	 */
	public static Response getFoodActivities() {

		String clientResponse;

		try {
			BloodSugarRateCalculator bloodSugarRateCalculator = new BloodSugarRateCalculator(HIQServlet.getConfigPath());

			List<ActivityInfo> aList = bloodSugarRateCalculator.getFoodActivities();
			Gson gson = new Gson();
			gson.toJson(aList);

			clientResponse = gson.toJson(aList);
		} catch (Exception e) {
			clientResponse = ActivityHelper.buildErrorJsonResponse(e.getMessage());
		}

		return Response.ok().entity(clientResponse).header("Content-Type", "application/json").build();
	}

	@GET
	@Path("/exerciseactivities")
	/**
	 * Build and return the list of exercise activities from DB
	 */
	public static Response getExerciseActivities() {

		String clientResponse;
		try {
			BloodSugarRateCalculator bloodSugarRateCalculator = new BloodSugarRateCalculator(HIQServlet.getConfigPath());

			List<ActivityInfo> aList = bloodSugarRateCalculator.getExerciseActivities();
			Gson gson = new Gson();
			gson.toJson(aList);

			clientResponse = gson.toJson(aList);
		} catch (Exception e) {
			clientResponse = ActivityHelper.buildErrorJsonResponse(e.getMessage());
		}
		return Response.ok().entity(clientResponse).header("Content-Type", "application/json").build();
	}

	@POST
	@Path("/simulate")
	/**
	 * Analyze sugar level and glycation and return the results
	 */
	public static Response runSimulation(
			@Context HttpHeaders hh,
			@Context HttpServletRequest req) {

		String reqBody;
		StringBuilder respToClient = new StringBuilder();

		try {
			reqBody = DataAccessHelper.streamToString(req.getInputStream());

			if (null == reqBody || 0 == reqBody.length()) {
				return Response.status(400).header("Content-Type", "application/json").build();
			}

			BloodSugarRateCalculator bloodSugarRateCalculator = new BloodSugarRateCalculator(HIQServlet.getConfigPath());

			//build activities list
			List<ActivityInfo> aInfoList = (ArrayList) ActivityInfo.fromJSONArray(reqBody);
			bloodSugarRateCalculator.buildTimeLines(aInfoList);

			//get analyzed data
			List<Double> bList = bloodSugarRateCalculator.getBloodSugarTimeLine();
			List<Integer> gList = bloodSugarRateCalculator.getGlycationTimeLine();

			Gson gson = new Gson();

			//build response
			respToClient.append("{ \"bloodSugarValues\":");
			List<DataItemInfo> hTimeLine = DataAccessHelper.buildDateItemList(bloodSugarRateCalculator.getPropertiesMap(), bList);
			respToClient.append(gson.toJson(hTimeLine));

			hTimeLine.clear();

			respToClient.append(", \"glycationValues\":");
			hTimeLine = DataAccessHelper.buildDateItemList(bloodSugarRateCalculator.getPropertiesMap(), gList);
			respToClient.append(gson.toJson(hTimeLine));

			//close JSON
			respToClient.append("}");


		} catch (Exception e) {
			respToClient.delete(0, respToClient.length());
			respToClient.append(ActivityHelper.buildErrorJsonResponse(e.getMessage()));
		}
		return Response.status(200).entity(respToClient.toString()).header("Content-Type", "application/json").build();
	}

}
