package com.cloudmunch.mapperpojo;

import org.json.JSONException;
import org.json.JSONObject;

public class Tolerance {

	private JSONObject toleranceObject = null;

	public JSONObject getToleranceObject() {
		return toleranceObject;
	}

	public void setToleranceDetails(JSONObject tolerance) {
		this.toleranceObject = tolerance;
	}

	private String getStringValue(String key) throws JSONException {
		if (toleranceObject.has(key)) {
			Object o = toleranceObject.get(key);
			if (o instanceof String) {
				return (String) o;
			}
		}
		return "";
	}

	private void setStringValue(String key, String value) throws JSONException {
		if (toleranceObject.has(key)) {
			Object o = toleranceObject.get(key);
			if (o instanceof String) {
				toleranceObject.put(key, value);
			}
		}
	}

	public String getPassCriteria() throws JSONException {
		return getStringValue("PassCriteria");
	}

	public void setPassCriteria(String val) throws JSONException {
		setStringValue("PassCriteria", val);
	}

	public String getTolerancePercentage() throws JSONException {
		return getStringValue("TolerancePercentage");
	}

	public void setTolerancePercentage(String val) throws JSONException {
		setStringValue("TolerancePercentage", val);
	}

}
