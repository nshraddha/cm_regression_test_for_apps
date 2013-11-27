package com.cloudmunch.argumentparser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.json.JSONException;
import org.json.JSONObject;

public class ArgumentParser {

	public static JSONObject inputParams = new JSONObject();
	public static String strInputParams = "";
	public static StringBuffer message = new StringBuffer();

	public final static List<ParameterHandler> requiredParams = new ArrayList<ParameterHandler>();

	public static int parseArguments(String[] args) {
		Options options = new Options();

		Option jsonInputOption = new Option("i", "jsoninput", true, "List of User arguments in the form of JSON");
		jsonInputOption.setRequired(true);

		options.addOption(jsonInputOption);

		// HelpFormatter formatter = new HelpFormatter();
		// formatter.printHelp("JavaInstrument", options);

		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption(jsonInputOption.getLongOpt())) {
				strInputParams = cmd.getOptionValue(jsonInputOption.getLongOpt());
				inputParams = new JSONObject(cmd.getOptionValue(jsonInputOption.getLongOpt()));
			}

			if (inputParams.length() > 0) {
				List<String> unAvailableParamsList = new ArrayList<String>();
				for (int i = 0; i < requiredParams.size(); i++) {
					if (requiredParams.get(i).isRequired()) {
						if (inputParams.has(requiredParams.get(i).getParameter())) {
							continue;
						} else {
							unAvailableParamsList.add(requiredParams.get(i).getParameter());
						}
					} else {
					}
				}

				if (unAvailableParamsList.size() > 0) {
					System.out.println("Parameters that are missing - "+ unAvailableParamsList);
					return unAvailableParamsList.size();
					// throw new
					// MissingOptionException("Required Parameters are missing : "
					// + unAvailableParamsList);
				}
			} else {
				return requiredParams.size();
				// throw new
				// MissingOptionException("Required Parameters are missing");
			}
			return 0;
		} catch (MissingOptionException e) {
			@SuppressWarnings("unchecked")
			List<String> missedOptions = e.getMissingOptions();
			for (int i = 0; i < missedOptions.size(); i++) {
				System.out.println(options.getOption(missedOptions.get(i)));
				message.append("Missing Option : " + options.getOption(missedOptions.get(i))).append("\r\n");
			}
			writeReport();
//			System.exit(1);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			message.append(e.getMessage()).append("\r\n");
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			message.append(e.getMessage()).append("\r\n");
		}
		return requiredParams.size();
	}
	
	public static void writeReport() {
		System.out.println(message.toString());
	}
}
