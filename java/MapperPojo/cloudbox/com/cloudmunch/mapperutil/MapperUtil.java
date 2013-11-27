package com.cloudmunch.mapperutil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.cloudbox.url.CloudboxURLReader;
import com.cloudbox.url.URLGenerator;

public class MapperUtil {

	private static final String DOMAIN_KEY = "domain";
	private static final String CONTEXT_KEY = "context";
	private static final String USERNAME_KEY = "username";
	private static final String UTF_ENCODE = "UTF-8";
	private static final String EQUALS_SYMBOL = "=";
	private static final String URL_SEPARATOR = "/";

	
	public static void postMapperContent(String cmServerURL, String domain, String project, String context, String mode, String data) {
		final StringBuffer completeString = new StringBuffer();
		try {
			String postdata = URLEncoder.encode(USERNAME_KEY, UTF_ENCODE) + EQUALS_SYMBOL + URLEncoder.encode("leela@cloudboxonline.com", UTF_ENCODE);
			postdata += "&" + URLEncoder.encode(CONTEXT_KEY, UTF_ENCODE) + EQUALS_SYMBOL + URLEncoder.encode(context, UTF_ENCODE);
			postdata += "&" + URLEncoder.encode("password", UTF_ENCODE) + EQUALS_SYMBOL + URLEncoder.encode("cloudmunch", UTF_ENCODE);
			postdata += "&" + URLEncoder.encode("mode", UTF_ENCODE) + EQUALS_SYMBOL + URLEncoder.encode(mode, UTF_ENCODE);
			postdata += "&" + URLEncoder.encode("data", UTF_ENCODE) + EQUALS_SYMBOL + URLEncoder.encode(data, UTF_ENCODE);
			postdata += "&" + URLEncoder.encode(DOMAIN_KEY, UTF_ENCODE) + EQUALS_SYMBOL + URLEncoder.encode(domain, UTF_ENCODE);
			postdata += "&" + URLEncoder.encode("project", UTF_ENCODE) + EQUALS_SYMBOL + URLEncoder.encode(project, UTF_ENCODE);

			if (!cmServerURL.endsWith(URL_SEPARATOR)) {
				cmServerURL += URL_SEPARATOR;
			}

			URL url = new URL(cmServerURL + "cbdata.php");
			System.out.println("CM Server URL is =" + url.toString());
			 System.out.println("Data is - " + postdata);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(postdata);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				// Process line...
				completeString.append(line);
			}
			System.out.println("Result of post is - " + completeString.toString());
			wr.close();
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getMapperContent(String cmURL, String project, String domain) {
		String content = "";
		try {
			URLGenerator queryURL = new URLGenerator();
			queryURL.setWeb(cmURL);
			queryURL.setFileName("cbdata.php");
			queryURL.addArgument(USERNAME_KEY, StringConstants.CBDATAUSER);
			queryURL.addArgument(CONTEXT_KEY, "mapper");
			queryURL.addArgument("project", project);
			queryURL.addArgument(DOMAIN_KEY, domain);
			String url = queryURL.getURL(true);
			System.out.println("QueryURL : " + url);
			content = CloudboxURLReader.getContent(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;

	}

}
