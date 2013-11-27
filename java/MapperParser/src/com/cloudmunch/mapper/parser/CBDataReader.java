package com.cloudmunch.mapper.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.cloumunch.mapper.utils.CloudboxURLReader;
import com.cloumunch.mapper.utils.URLGenerator;

public class CBDataReader {

	public static JSONArray readDeployServer(String url) throws JSONException {
		URLGenerator urlGenerator = new URLGenerator();
		urlGenerator.setWeb(url);
		urlGenerator.setFileName("cbdata.php");
		urlGenerator.addArgument("context", "server");
		urlGenerator.addArgument("username", "CI");
//		System.out.println(urlGenerator.toString());
		CloudboxURLReader.setUrlGenerator(urlGenerator);
		String str = CloudboxURLReader.getContent();

		try {
			return new JSONArray(str);
		} catch (Exception e) {
			return new JSONArray();
		}
	}
	
}
