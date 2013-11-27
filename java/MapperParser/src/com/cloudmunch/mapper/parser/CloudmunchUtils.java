package com.cloudmunch.mapper.parser;

import com.cloudmunch.mapper.constants.StringConstants;
import com.cloumunch.mapper.utils.CloudboxURLReader;
import com.cloumunch.mapper.utils.URLGenerator;

public class CloudmunchUtils {
	public static String getURLData(String domainURL, String key, String value) {
		URLGenerator urlGenerator = new URLGenerator();
		urlGenerator.setWeb(domainURL);
		urlGenerator.setFileName(StringConstants.CBDATA_PHP);
		urlGenerator.setApp("");
		urlGenerator.addArgument(StringConstants.CBDATA_USERNAME_STR, StringConstants.CBDATA_USERNAME);
		// urlGenerator.addArgument("username", "box@cloudboxonline.com");
		// urlGenerator.addArgument("password", "box");
		urlGenerator.addArgument(key, value);

		// String url = urlGenerator.getURL(true);
		CloudboxURLReader.setUrlGenerator(urlGenerator);
		String str = CloudboxURLReader.getGETContent();
		// System.out.println(str);

		return str;
	}

}
