package com.cloudmunch.checkstyle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * Check Style App which is a development tool to help programmers write Java code that adheres to a coding standard.
 * 
 * @author Leela
 */
public class CheckStyleRunner {

	private static JSONObject inputParams = new JSONObject();
	private static String strInputParams = "";
	private static StringBuffer message = new StringBuffer();

	public static JSONObject getInputParams() {
		return inputParams;
	}

	public static void setInputParams(JSONObject inputParams) {
		CheckStyleRunner.inputParams = inputParams;
	}

	public static String getStrInputParams() {
		return strInputParams;
	}

	public static void setStrInputParams(String strInputParams) {
		CheckStyleRunner.strInputParams = strInputParams;
	}

	public static StringBuffer getMessage() {
		return message;
	}

	public static void setMessage(StringBuffer message) {
		CheckStyleRunner.message = message;
	}

	public enum PARAMETERS {
		Configuration, SourcePath, Format, OutputReport;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int parseArgumentCount = ArgumentParser.parseArguments(args);
		System.out.println("No of arguments missed are - " + parseArgumentCount);
		if (parseArgumentCount == 0) {
			List<String> cmdList = new ArrayList<String>();
			constructCommandLine(cmdList);
			// System.out.println("Commands are - " + cmdList);
			CMCheckStyleMain.main(cmdList.toArray(new String[cmdList.size()]));
			System.out.println("CheckStyle run completed with " + CMCheckStyleMain.getNumberOfErrors() + " errors ");
			System.out.println("About to parse the report to create JSON Structure");

			try {
				JSONObject finalReport = parseReportToJSON();
				// System.out.println("Final JSON Report Content is - " + JSONObject.valueToString(finalReport));
				String jsonOutFile = inputParams.getString("outfile");
				FileUtils.writeStringToFile(new File(jsonOutFile), JSONObject.valueToString(finalReport), false);
				System.out.println("Parsing Completed, and created JSON report.");
			} catch (JSONException e) {
				System.out.println("Exception while parsing report to JSON Format" + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Exception while parsing report to JSON Format" + e.getMessage());
				e.printStackTrace();
			}

			System.exit(0);
		} else {
			System.out.println("Some of mandatory arguments are missing.Hence exiting..");
			System.exit(1);
		}

	}

	private static JSONObject parseReportToJSON() throws JSONException, IOException {
		String xmlFile = inputParams.getString("xmloutfile");
		String content = FileUtils.readFileToString(new File(xmlFile));
		JSONObject s = XML.toJSONObject(content);
		// System.out.println(s);
		JSONObject summaryObj = new JSONObject();
		if (s.has("checkstyle")) {
			JSONObject finalReport = s.getJSONObject("checkstyle");

			if (finalReport.get("file") instanceof JSONObject) {
				summaryObj.put("no_of_files", 1);
			} else if (finalReport.get("file") instanceof JSONArray) {
				JSONArray jsonArray = finalReport.getJSONArray("file");
				summaryObj.put("no_of_files", jsonArray.length());
			}
		}
		String errors = String.valueOf(CMCheckStyleMain.getNumberOfErrors());
		summaryObj.put("actual", errors);
		summaryObj.put("remarks", errors + " errors");
		summaryObj.put("total", "");
		s.put("summary", summaryObj);
		return s;
	}

	private static void constructCommandLine(List<String> cmdList) {
		try {
			cmdList.add("-c");
			if (inputParams.has("configuration")) {
				cmdList.add(formatInput("Configuration", inputParams.getString("configuration")));
			} else {
				cmdList.add(CheckStyleRunner.class.getResource("sun_checks.xml").toExternalForm());
			}
			cmdList.add("-o");
			cmdList.add(formatInput("OutputReport", inputParams.getString("xmloutfile")));
			String sourceList = inputParams.getString("source");
			cmdList.add("-f");
			cmdList.add("xml");
			String[] sources = StringUtils.split(sourceList, ',');
			for (String source : sources) {
				source = source.trim();
				if (new File(source).exists() && new File(source).isFile()) {
					cmdList.add(source);
				} else if (new File(source).exists() && new File(source).isDirectory()) {
					cmdList.add("-r");
					cmdList.add(source);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private static String formatInput(String category, String value) {
		PARAMETERS p = PARAMETERS.valueOf(category);
		String retValue = "";
		switch (p) {
		case SourcePath:
			retValue = "\"" + value + "\"";
			break;
		case Configuration:
			retValue = "\"" + value + "\"";
			break;
		case OutputReport:
			retValue = "\"" + value + "\"";
			break;
		case Format:
			retValue = value;
			break;
		default:
			retValue = value;
		}
		return retValue;
	}

}
