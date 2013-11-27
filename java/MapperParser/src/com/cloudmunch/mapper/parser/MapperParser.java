package com.cloudmunch.mapper.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudmunch.mapper.constants.StringConstants;

public class MapperParser {

	public static void main(String[] args) {
		System.out.println("This is a Mapper Parser API");
	}
	
	public static String findParameterInTarget(JSONObject targetMapper, String parameterName) throws JSONException {
		if (targetMapper.has(StringConstants.MAPPER_PARAMETERS)) {
			JSONObject params = targetMapper.getJSONObject(StringConstants.MAPPER_PARAMETERS);
			if (params.has(parameterName)) {
				return params.getString(parameterName);
			}
		}
		return null;
	}

	public static String findKeyInTarget(JSONObject targetMapper, String keyName) throws JSONException {
		// if (targetMapper.has(StringConstants.MAPPER_PARAMETERS)) {
		// JSONObject params =
		// targetMapper.getJSONObject(StringConstants.MAPPER_PARAMETERS);
		if (targetMapper.has(keyName)) {
			return targetMapper.getString(keyName);
		}
		// }
		return null;
	}

	public static JSONObject findReferenceCBApp(String mapperFile, String projectName, String jobName, String targetName) {
		try {
			JSONObject mapper = new JSONObject(mapperFile);
			if (mapper.has(projectName)) {
				JSONObject pro = mapper.getJSONObject(projectName);
				if (pro.has(jobName)) {
					JSONObject job = pro.getJSONObject(jobName);
					if (job.has(StringConstants.MAPPER_CBSTEPS)) {
						JSONObject cb_steps = job.getJSONObject(StringConstants.MAPPER_CBSTEPS);
						if (cb_steps.has(targetName)) {
							return cb_steps.getJSONObject(targetName);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String findTechStack(JSONObject mapper, String projectName, String jobName) {
		try {
			if (mapper.has(projectName)) {
				JSONObject pro = mapper.getJSONObject(projectName);
				if (pro.has(jobName)) {
					JSONObject job = pro.getJSONObject(jobName);
					if (job.has(StringConstants.MAPPER_DEPLOY)) {
						JSONObject deploy = job.getJSONObject(StringConstants.MAPPER_DEPLOY);
						if (deploy.has(StringConstants.MAPPER_TECHSTACK)) {
							JSONObject techstack = deploy.getJSONObject(StringConstants.MAPPER_TECHSTACK);
							String[] listedtechstack = JSONObject.getNames(techstack);
							for (int i = 0; i < listedtechstack.length; i++) {
								String choice = techstack.getString(listedtechstack[i]);
								if (choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("true") || choice.equalsIgnoreCase("on")) {
									return StringConstants.SERVER_PREFIX + listedtechstack[i] + "_" + System.getenv(StringConstants.CLOUDBOX_DOMAIN);
								}
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
