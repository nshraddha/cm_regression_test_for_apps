package com.cloudmunch.checkstyle;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ArgumentParser {

	public final static List<ParameterHandler> REQUIREDPARAMS = new ArrayList<ParameterHandler>();
	static {
		InputStream is = ArgumentParser.class.getResourceAsStream("arguments.json");
		try {
			JSONObject jObj = new JSONObject(IOUtils.toString(is));
			String[] keys = JSONObject.getNames(jObj);
			System.out.println("Possible arguments are - "+ ArrayUtils.toString(keys));
			for (String key : keys) {
				REQUIREDPARAMS.add(new ParameterHandler().setParameter(key).setRequired(jObj.getBoolean(key)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int parseArguments(String[] args) {
		Options options = new Options();

		Option jsonInputOption = new Option("i", "jsoninput", true, "List of User arguments in the form of JSON");
		jsonInputOption.setRequired(true);

		options.addOption(jsonInputOption);

		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption(jsonInputOption.getLongOpt())) {
				String input = cmd.getOptionValue(jsonInputOption.getLongOpt());
				CheckStyleRunner.setStrInputParams(input) ;
//				System.out.println("String Input Params is - " +input);
				CheckStyleRunner.setInputParams( new JSONObject(input));
//				System.out.println("JSON - " +CheckStyleRunner.getInputParams());
			}

			if (CheckStyleRunner.getInputParams().length() > 0) {
				List<String> unAvailableParamsList = new ArrayList<String>();
				for (int i = 0; i < REQUIREDPARAMS.size(); i++) {
					if (REQUIREDPARAMS.get(i).isRequired()) {
						if (CheckStyleRunner.getInputParams().has(REQUIREDPARAMS.get(i).getParameter())) {
							continue;
						} else {
							unAvailableParamsList.add(REQUIREDPARAMS.get(i).getParameter());
						}
					} else {
					}
				}

				if (unAvailableParamsList.size() > 0) {
					System.out.println("Arguments that are marked as Mandatory are missing - " + unAvailableParamsList.toString());
					return unAvailableParamsList.size();
					// throw new
					// MissingOptionException("Required Parameters are missing : "
					// + unAvailableParamsList);
				}
			} else {
				return REQUIREDPARAMS.size();
				// throw new
				// MissingOptionException("Required Parameters are missing");
			}
			return 0;
		} catch (MissingOptionException e) {
			@SuppressWarnings("unchecked")
			List<String> missedOptions = e.getMissingOptions();
			for (int i = 0; i < missedOptions.size(); i++) {
				System.out.println(options.getOption(missedOptions.get(i)));
				CheckStyleRunner.getMessage().append("Missing Option : " + options.getOption(missedOptions.get(i))).append("\r\n");
			}
			System.exit(1);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			CheckStyleRunner.getMessage().append(e.getMessage()).append("\r\n");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			CheckStyleRunner.getMessage().append(e.getMessage()).append("\r\n");
		}
		return REQUIREDPARAMS.size();
	}
}
