package com.cloudmunch.helper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class CMUtils {

	public static String getCurrentWorkingDirectory() throws IOException {
		File f = new File(".");
		String currentWorkingDir = f.getCanonicalPath();
		return FilenameUtils.separatorsToSystem(currentWorkingDir);
	}

	public static File getCurrentWorkingDirectoryFile() throws IOException {
		return new File(getCurrentWorkingDirectory());
	}

	public static long getTimeOutPeriod(JSONObject inputParams) throws JSONException {
		long timeout = 1;
		if (inputParams.has("timeout")) {
			String timeoutString = inputParams.getString("timeout");
			String[] items = StringUtils.split(timeoutString, "*");
			for (int i = 0; i < items.length; i++) {
				timeout *= NumberUtils.toLong(items[i]);
			}
		} else {
			timeout = 30 * 60 * 1000;
		}
		return timeout;
	}

	public static void main(String[] args) throws JSONException {
		JSONObject x = new JSONObject();
		x.put("timeout", "3*60*1000");
		long l = getTimeOutPeriod(x);
		System.out.println(l);
	}

}
