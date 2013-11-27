package com.cloudmunch.changeset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangeSet {

	public static String workspacePath = "";
	public static List<String> fileList = new ArrayList<String>();
	public static Map<String, List<Integer>> changeset = new HashMap<String, List<Integer>>();
	public static boolean isChangeSet = (System.getenv("cloudbox.changeset") == null ? false : ((System.getenv("cloudbox.changeset").equalsIgnoreCase("false")) ? false : true));
	public static boolean doInit = true;

	public static void init() throws IOException, JSONException {
		if (doInit) {
			doInit = false;
			System.out.println("Initializing Changeset");
			String changeSetFileName = System.getenv("cloudbox.changeset");
			String changeSetData = FileUtils.readFileToString(new File(changeSetFileName));
			JSONArray changesetJson = new JSONArray(changeSetData);
			for (int i = 0; i < changesetJson.length(); i++) {
				JSONObject item = changesetJson.getJSONObject(i);
				String key = item.getString("Filename");
				JSONArray valueJ = item.getJSONArray("Lines");
				List<Integer> value = new ArrayList<Integer>();
				for (int j = 0; j < valueJ.length(); j++) {
					value.add(valueJ.getInt(j));
				}
				changeset.put(key, value);
				fileList.add(key);
			}
			System.out.println("Changeset Initization completed");
		} else {
			// already initialized, no need to re init unless required, if required set the do init to true
		}
	}

	public static List<String> getFileNameList() {
		return fileList;
	}

	public static boolean hasFileName(String fileNameKey) {
		if (!changeset.containsKey(fileNameKey)) {
			fileNameKey = StringUtils.remove(fileNameKey, workspacePath);
		}
		return changeset.containsKey(fileNameKey);
	}

	public static List<Integer> getFileName(String FileNameKey) {
		if (!changeset.containsKey(FileNameKey)) {
			FileNameKey = StringUtils.remove(FileNameKey, workspacePath);
		}
		return changeset.get(FileNameKey);
	}

	public static void dummyInit() {
		List<Integer> l = new ArrayList<Integer>();
		l.add(1045);
		changeset.put("E:\\GITWorkSpace_new1\\CloudBox\\EclipseInContextPlugin\\src\\com\\cloudbox\\views\\EclipseInContextView.java", l);
	}

}
