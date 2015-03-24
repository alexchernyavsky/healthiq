/* @(#)DataItemInfo
 * 
 * Copyright (c) 2015 Cepheid. All Rights Reserved.
 *
 */

package com.healthiq.info;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a data item for a period in time
 *
 * @author Alex Chernyavsky
 */

public class DataItemInfo {

	@SerializedName("time")
	@Expose
	private String time;
	@SerializedName("value")
	@Expose
	private String value;

	public static DataItemInfo fromJSON(String jsonData) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.fromJson(jsonData, DataItemInfo.class);
	}

	public static List<DataItemInfo> fromJSONArray(String jsonData) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		DataItemInfo[] objs = gson.fromJson(jsonData, DataItemInfo[].class);
		return new ArrayList<>(Arrays.asList(objs));
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toJSON() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(this);
		if (null != json) {
			return json;
		} else return null;
	}
}
