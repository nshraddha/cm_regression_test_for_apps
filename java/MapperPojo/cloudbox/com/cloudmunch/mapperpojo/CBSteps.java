package com.cloudmunch.mapperpojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudmunch.mapperutil.MapperConstants;

public class CBSteps {

	private JSONObject steps = null;
	private JSONObject executableSteps = null;

	public JSONObject getSteps() {
		return steps;
	}

	public void setSteps(JSONObject steps) {
		this.steps = steps;
	}

	public List<String> getAllCBStepNames() {
		String[] s = JSONObject.getNames(steps);
		return new ArrayList<String>(Arrays.asList(s));
	}

	public JSONObject getExecutableSteps() throws JSONException {
		executableSteps = new JSONObject();
		String[] s = JSONObject.getNames(steps);
		for (String stepName : s) {
			if (steps.get(stepName) instanceof JSONObject) {
				JSONObject targetObject = steps.getJSONObject(stepName);
				boolean execute = targetObject.has(MapperConstants.EXECUTE_TARGET_KEY)
						&& ((targetObject.getString(MapperConstants.EXECUTE_TARGET_KEY).equalsIgnoreCase(MapperConstants.YES))
								|| (targetObject.getString(MapperConstants.EXECUTE_TARGET_KEY).equalsIgnoreCase(MapperConstants.TRUE)) || (targetObject.getString(MapperConstants.EXECUTE_TARGET_KEY)
								.equalsIgnoreCase(MapperConstants.OK)));
				if (execute) {
					executableSteps.put(stepName, targetObject);
				}
			}
		}
		return executableSteps;
	}

	public int getNumberOfExecutableCBSteps() throws JSONException {
		return getAllExecutableCBStepObjects().size();
	}

	public int getNumberOfCBSteps() throws JSONException {
		return getAllCBStepNames().size();
	}

	public List<JSONObject> getAllExecutableCBStepObjects() throws JSONException {
		List<JSONObject> listOfSteps = new ArrayList<JSONObject>();
		String[] s = JSONObject.getNames(steps);
		for (String stepName : s) {
			if (steps.get(stepName) instanceof JSONObject) {
				JSONObject targetObject = steps.getJSONObject(stepName);
				if (targetObject.has(MapperConstants.EXECUTE_TARGET_KEY)
						&& ((targetObject.getString(MapperConstants.EXECUTE_TARGET_KEY).equalsIgnoreCase(MapperConstants.YES))
								|| (targetObject.getString(MapperConstants.EXECUTE_TARGET_KEY).equalsIgnoreCase(MapperConstants.TRUE)) || (targetObject.getString(MapperConstants.EXECUTE_TARGET_KEY)
								.equalsIgnoreCase(MapperConstants.OK)))) {
					listOfSteps.add(targetObject);
				}
			}
		}

		return listOfSteps;
	}

	public AppStep getCBStep(String name) throws JSONException {
		AppStep as = new AppStep();
		if (steps.has(name)) {
			as.setAppStep(steps.getJSONObject(name));
			as.setName(name);
		}

		return as;
	}

}
