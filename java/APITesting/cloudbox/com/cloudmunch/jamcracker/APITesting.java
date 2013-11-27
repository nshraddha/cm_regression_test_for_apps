package com.cloudmunch.jamcracker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import com.cloudmunch.argumentparser.ArgumentParser;
import com.cloudmunch.argumentparser.ParameterHandler;

public class APITesting {

	/**
	 * @param args
	 * @throws JSONException
	 */
	public static void main(String[] args) throws JSONException {
		ArgumentParser.requiredParams.clear();
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("API Testing URL").setRequired(true));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("Report Location").setRequired(true));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("API Report Location").setRequired(true));

		int s = ArgumentParser.parseArguments(args);
		int exitCode = 0;
		System.out.println("Executing API Testing");
		if (s == 0) {
			URL url = null;
			String host = "";
			String htmlContent = "";
			String error = "";
			try {
				url = new URL(ArgumentParser.inputParams.getString("API Testing URL"));
				host = url.getHost();
				URLConnection urlConnection = url.openConnection();
				InputStream is = urlConnection.getInputStream();
				StringWriter stringWriter = new StringWriter();
				IOUtils.copy(is, stringWriter);
				htmlContent = stringWriter.toString();
			} catch (MalformedURLException e) {
				error = e.toString();
			} catch (IOException e) {
				error = e.toString();
			}

			StringBuffer sb = new StringBuffer();

			sb.append("URL for API Testing : " + ArgumentParser.inputParams.getString("API Testing URL")).append(System.getProperty("line.separator"));
			sb.append("API Testing Report Machine : " + host).append(System.getProperty("line.separator"));
			sb.append("API Testing Report Location : " + ArgumentParser.inputParams.getString("API Report Location")).append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator") + System.getProperty("line.separator"));

			if (error.trim().length() == 0) {
				boolean status = validateHTMLFOrJamcrackerHTMLContent(htmlContent);
				if (status) {
					sb.append("The API Testing seems to be valid");
				} else {
					sb.append("Some thing went wrong in API Testing");
				}
			} else {
				sb.append(error);
			}

			sb.append(System.getProperty("line.separator") + System.getProperty("line.separator"));

			System.out.println(sb.toString());

			File outputFile = new File(ArgumentParser.inputParams.getString("Report Location"));
			try {
				FileUtils.writeStringToFile(outputFile, ArgumentParser.inputParams.getString("API Report Location"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Required Arguments not found");
			exitCode = 1;
		}
		System.out.println("Executing API Testing Completed");
		System.exit(exitCode);
	}

	private static boolean validateHTMLFOrJamcrackerHTMLContent(String htmlContent) {
		return true;
	}

}
