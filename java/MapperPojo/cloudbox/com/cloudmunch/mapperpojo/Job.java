package com.cloudmunch.mapperpojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Job {

	private String name = "";
	private JSONObject job = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getJob() {
		return job;
	}

	public void setJob(JSONObject job) {
		this.job = job;
	}

	public void getMailConfiguration() {

	}

	public void getSCM() {

	}

	public boolean isActive() throws JSONException {
		String val = getStringValue("active");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setActive(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("active", active);
	}

	public JSONArray getArchives() throws JSONException {
		if (job.has("archives")) {
			Object o = job.get("archives");
			if (o instanceof JSONArray) {
				return (JSONArray) o;
			} else {
				return null;
			}
		} else {
			job.put("archives", new JSONArray());
			return job.getJSONArray("archives");
		}
	}

	public boolean isAutoUpdateDefect() throws JSONException {
		String val = getStringValue("autoUpdateDefect");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setAutoUpdateDefect(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("autoUpdateDefect", active);
	}

	public boolean isAutoUpdateRequirement() throws JSONException {
		String val = getStringValue("autoUpdateRequirement");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setAutoUpdateRequirement(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("autoUpdateRequirement", active);
	}

	public boolean isDodeploy() throws JSONException {
		String val = getStringValue("dodeploy");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setDodeploy(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("dodeploy", active);
	}

	public boolean isRetainarchive() throws JSONException {
		String val = getStringValue("retainarchive");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setRetainarchive(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("retainarchive", active);
	}

	public boolean isTestnode() throws JSONException {
		String val = getStringValue("use_testnode");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setTestnode(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("use_testnode", active);
	}

	public boolean isWindowsnode() throws JSONException {
		String val = getStringValue("use_windowsnode");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setWindowsnode(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("use_windowsnode", active);
	}

	public boolean isAutoRaiseDefectOnFailure() throws JSONException {
		String val = getStringValue("autoRaiseDefectOnFailure");
		if (val.equalsIgnoreCase("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public void setAutoRaiseDefectOnFailure(boolean value) throws JSONException {
		String active = "";
		if (value) {
			active = "yes";
		} else {
			active = "no";
		}
		setStringValue("autoRaiseDefectOnFailure", active);
	}

	public CBSteps getCBStep() throws JSONException {
		CBSteps c = new CBSteps();

		if (job.has("cb_steps")) {
			c.setSteps(job.getJSONObject("cb_steps"));
		}

		return c;
	}

	private String getStringValue(String key) throws JSONException {
		if (job.has(key)) {
			Object o = job.get(key);
			if (o instanceof String) {
				return (String) o;
			}
		}
		return "";
	}

	private void setStringValue(String key, String value) throws JSONException {
		if (job.has(key)) {
			Object o = job.get(key);
			if (o instanceof String) {
				job.put(key, value);
			}
		}
	}
}
