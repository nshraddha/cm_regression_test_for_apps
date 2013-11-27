package com.cloudmunch.mapperpojo;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapperConfig {

	private static JSONObject mapperConfig = null;

	static {
		InputStream is = MapperConfig.class.getResourceAsStream("mapperconfig.json");
		try {
			mapperConfig = new JSONObject(IOUtils.toString(is));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JSONObject getMapperConfig() {
		return mapperConfig;
	}

	public static JSONArray getProjectExceptionList() throws JSONException {
		return getCustomFieldInJsonArray("projectexception");
	}

	public static JSONArray getBuildCategorys() throws JSONException {
		return getCustomFieldInJsonArray("validcategorylist");
	}

	public static JSONArray getCustomFieldInJsonArray(String key) throws JSONException {
		JSONArray returnJsonArray = new JSONArray();
		if (mapperConfig.has(key)) {
			returnJsonArray = mapperConfig.getJSONArray(key);
		}
		return returnJsonArray;
		
	}
}
