package com.cloudmunch.mapperpojo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Mapper {

	private static JSONObject mapper = null;

	public static JSONObject getMapper() {
		return mapper;
	}

	public static void setMapper(JSONObject mapper) {
		Mapper.mapper = mapper;

	}

	public static List<String> getProjectList() throws JSONException {
		List<String> list = new ArrayList<String>(Arrays.asList(JSONObject.getNames(mapper)));
		JSONArray projectException = MapperConfig.getProjectExceptionList();
		for (int i = 0; i < projectException.length(); i++) {
			String s = projectException.getString(i);
			if (list.contains(s)) {
				list.remove(s);
			}
		}
		return list;
	}

	public static Project getProject(String projName) throws JSONException {
		Project p = new Project();

		if (mapper.has(projName)) {
			JSONObject project = mapper.getJSONObject(projName);
			p.setProject(project);
			p.setName(projName);
		}

		return p;
	}

	/**
	 * @param args
	 * @throws JSONException
	 * @throws IOException
	 */
	public static void main(String[] args) throws JSONException, IOException {

		JSONObject x = new JSONObject(getFromCBData());
		Mapper.setMapper(x);

		System.out.println(Mapper.getProjectList());
		Project p = Mapper.getProject("Cloudbox");
		System.out.println(p.getAvailableBuildCategory());
		Category c = p.getCategory("build");
		System.out.println(c.getJobName());
		Job j = c.getJob("quick");
		CBSteps cbSteps = j.getCBStep();
		JSONObject executableSteps = cbSteps.getExecutableSteps();
		System.out.println("No of executable steps number - " + executableSteps.length());

		System.out.println("Steps are " + cbSteps.getAllCBStepNames());
		System.out.println("No of steps are -" + cbSteps.getNumberOfCBSteps());

		System.out.println("No of Executable steps are -" + cbSteps.getNumberOfExecutableCBSteps());
		List<JSONObject> allExecutableCBStepObjects = cbSteps.getAllExecutableCBStepObjects();
		System.out.println("Executable step obj are -" + allExecutableCBStepObjects);

		AppStep as = cbSteps.getCBStep("Incremental Duplicate code detector for Java");
		System.out.println(as.getInternalAppName());
		Tolerance tolerance = as.getToleranceDetails();
		System.out.println("Tolerance Pass value for PMD App - "+ tolerance.getPassCriteria());
		System.out.println("Tolerance % value for PMD App - "+ tolerance.getTolerancePercentage());
		
		tolerance.setPassCriteria("5");
		tolerance.setTolerancePercentage("1");
		System.out.println("Tolerance Pass value for PMD App - "+ tolerance.getPassCriteria());
		System.out.println("Tolerance % value for PMD App - "+ tolerance.getTolerancePercentage());

		// JSONObject antRunner =
		// Mapper.getProject("cloudbox").getcategory("builds").getjob("quick").getcbapps().getApp("ANTRunner_1");
		// Project
		// Category
		// String[] listOfJobs = Job.getAllName();
		// CBApp
		//
		// antRunner.put("cb_execute", "no");
		// updateMapperToCBData(x);
	}

	private static String getFromCBData() throws IOException {
		return FileUtils.readFileToString(new File("C:\\Leela\\mapperfile.json"));
	}

}
