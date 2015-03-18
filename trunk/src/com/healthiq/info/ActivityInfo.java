

package com.healthiq.info;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Holds activity parameters
 *
 * @author Alex Chernyavsky
 */

public class ActivityInfo {
	@SerializedName("type")
	@Expose
	private String type;
	@SerializedName("hour")
	@Expose
	private Integer hour;
	@SerializedName("minute")
	@Expose
	private Integer minute;
	@SerializedName("index")
	@Expose
	private Integer index;

	public static ActivityInfo fromJSON(String jsonData) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.fromJson(jsonData, ActivityInfo.class);
	}

	public static List<ActivityInfo> fromJSONArray(String jsonData) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		ActivityInfo[] objs = gson.fromJson(jsonData, ActivityInfo[].class);
		return new ArrayList<>(Arrays.asList(objs));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String toJSON() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(this);
		if (null != json) {
			return json;
		} else return null;
	}
}
