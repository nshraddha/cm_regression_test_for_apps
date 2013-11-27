package com.cloudmunch.mapperpojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Category {

	private String name = "";
	private JSONObject category = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getCategory() {
		return category;
	}

	public void setCategory(JSONObject category) {
		this.category = category;
	}

	public List<String> getJobName() {
		List<String> list = new ArrayList<String>(Arrays.asList(JSONObject.getNames(category)));
		return list;
	}

	public Job getJob(String jobName) throws JSONException {
		Job j = new Job();

		if (category.has(jobName)) {
			j.setJob(category.getJSONObject(jobName));
			j.setName(jobName);
		}

		return j;
	}
}
