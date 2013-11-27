package com.cloudmunch.mapperpojo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Project {

	private String name = "";
	private JSONObject project = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getProject() {
		return project;
	}

	public void setProject(JSONObject project) {
		this.project = project;
	}

	public JSONObject getProjectAttributes() throws JSONException {
		if (project.has("ProjectAttributes")) {
			return project.getJSONObject("ProjectAttributes");
		} else {
			return new JSONObject();
		}
	}

	public List<String> getAvailableBuildCategory() throws JSONException {
		List<String> retValue = new ArrayList<String>();
		JSONArray validCategories = MapperConfig.getBuildCategorys();
		for (int i = 0; i < validCategories.length(); i++) {
			if (project.has(validCategories.getString(i))) {
				retValue.add(validCategories.getString(i));
			}
		}
		return retValue;
	}

	public Category getCategory(String categoryName) throws JSONException {
		Category c = new Category();

		if (project.has(categoryName)) {
			c.setName(categoryName);
			c.setCategory(project.getJSONObject(categoryName));
		}

		return c;
	}

}
