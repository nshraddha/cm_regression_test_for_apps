package com.cloudmunch.mapperpojo;

import org.json.JSONException;
import org.json.JSONObject;

public class AppStep {

	private String name = "";
	private JSONObject appStep = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getAppStep() {
		return appStep;
	}

	public void setAppStep(JSONObject appStep) {
		this.appStep = appStep;
	}

	public String getInternalAppName() throws JSONException {
		return getStringValue("cb_appname");
	}

	public void setInternalAppName(String intAppName) throws JSONException {
		setStringValue("cb_appname", intAppName);
	}

	public String getCategory() throws JSONException {
		return getStringValue("cb_category");
	}

	public void setCategory(String category) throws JSONException {
		setStringValue("cb_category", category);
	}

	public String getDisplayName() throws JSONException {
		return getStringValue("cb_displayName");
	}

	public void setDisplayName(String displayName) throws JSONException {
		setStringValue("cb_displayName", displayName);
	}

	public String getDescription() throws JSONException {
		return getStringValue("description");
	}

	public void setDescription(String description) throws JSONException {
		setStringValue("description", description);
	}

	public String getThumbnail() throws JSONException {
		return getStringValue("cb_thumbnail");
	}

	public void setThumbnail(String description) throws JSONException {
		setStringValue("cb_thumbnail", description);
	}

	public JSONObject getParameters() throws JSONException {
		return getJsonObjectValue("Parameters");
	}

	public void setParameters(JSONObject params) throws JSONException {
		setJSONObjectValue("Parameters", params);
	}

	public Tolerance getToleranceDetails() throws JSONException {
		Tolerance tolerance = new Tolerance();
		tolerance.setToleranceDetails(getJsonObjectValue("tolerance"));

		return tolerance;
		// return getJsonObjectValue("tolerance");
	}

	public void setToleranceValues(JSONObject toleranceObj) throws JSONException {
		setJSONObjectValue("tolerance", toleranceObj);
	}

	public boolean isAbortOnFailure() throws JSONException {
		String val = getStringValue("cb_abortonfailure");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setAbortOnFailure(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("cb_abortonfailure", active);
	}

	public boolean isApplyTolerance() throws JSONException {
		String val = getStringValue("cb_applytolerance");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setApplyTolerance(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("cb_applytolerance", active);
	}

	public boolean isDisplay() throws JSONException {
		String val = getStringValue("cb_display");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setDisplay(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("cb_display", active);
	}

	public boolean isExecute() throws JSONException {
		String val = getStringValue("cb_execute");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setExecute(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("cb_execute", active);
	}

	public boolean isRaiseDefectOnStepFailure() throws JSONException {
		String val = getStringValue("cb_raisedefectonstepfailure");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setRaiseDefectOnStepFailure(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("cb_raisedefectonstepfailure", active);
	}

	private String getStringValue(String key) throws JSONException {
		if (appStep.has(key)) {
			Object o = appStep.get(key);
			if (o instanceof String) {
				return (String) o;
			}
		}
		return "";
	}

	private void setStringValue(String key, String value) throws JSONException {
		if (appStep.has(key)) {
			Object o = appStep.get(key);
			if (o instanceof String) {
				appStep.put(key, value);
			}
		}
	}

	private JSONObject getJsonObjectValue(String key) throws JSONException {
		if (appStep.has(key)) {
			Object o = appStep.get(key);
			if (o instanceof JSONObject) {
				return appStep.getJSONObject(key);
			}
		}
		return new JSONObject();
	}

	private void setJSONObjectValue(String key, JSONObject value) throws JSONException {
		if (appStep.has(key)) {
			Object o = appStep.get(key);
			if (o instanceof JSONObject) {
				appStep.put(key, value);
			}
		}
	}
}
