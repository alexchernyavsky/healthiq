

package com.healthiq.web_server;

import com.google.gson.Gson;
import com.healthiq.business.BloodSugarRateCalculator;
import com.healthiq.info.ActivityInfo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
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
	public static Response getFoodActivities() {

		BloodSugarRateCalculator bloodSugarRateCalculator = new BloodSugarRateCalculator(HIQServlet.getConfigPath());

		List<ActivityInfo> aList = bloodSugarRateCalculator.getFoodActivities();
		Gson gson = new Gson();
		gson.toJson(aList);

		String activities = gson.toJson(aList);
		return Response.ok().entity(activities).header("Content-Type", "application/json").build();
	}

	@GET
	@Path("/exerciseactivities")
	public static Response getExerciseActivities() {

		BloodSugarRateCalculator bloodSugarRateCalculator = new BloodSugarRateCalculator(HIQServlet.getConfigPath());

		List<ActivityInfo> aList = bloodSugarRateCalculator.getExerciseActivities();
		Gson gson = new Gson();
		gson.toJson(aList);

		String activities = gson.toJson(aList);
		return Response.ok().entity(activities).header("Content-Type", "application/json").build();
	}
}
