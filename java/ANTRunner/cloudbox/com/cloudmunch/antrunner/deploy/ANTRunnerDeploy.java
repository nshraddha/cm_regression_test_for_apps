package com.cloudmunch.antrunner.deploy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloudmunch.argumentparser.ArgumentParser;
import com.cloudmunch.argumentparser.ParameterHandler;
import com.cloudmunch.helper.SSHHelper;
import com.cloudmunch.helper.ssh.SSHRunner;
import com.cloudmunch.mapper.parser.CBDataFinder;
import com.cloudmunch.mapper.parser.CBDataReader;

//project:Viswanath_Test4
//username:viswanath@cloudmunch.com
//buildloc:/var/cloudbox/jenkins/jobs/cloudboxstaging3-Viswanath_Test4-build-adhoc/builds/2012-10-22_03-49-28/archive
//domain:cloudboxstaging3
//target:deploy
//job:TestWithANTRunner
//buildfilepath:"/var/cloudbox/jenkins/jobs/cloudboxstaging3-Viswanath_Test4-test-TestWithANTRunner/workspace/CloudBox/Other Projects/CBApp/"
//buildname:4
//ant.lib:/var/cloudbox/ant1.8/lib/
//lookup:"{"DeployTest":{"runtimeValues":{},"constants":{"":""},"combined":{"":""}},"sources":{"CBDeployApp":"CloudBox/Other Projects/CBApp/","IVYSettings":"CloudBox/Build/ivysettings.xml"},"serverMap":{"DeployTest":"test_cloudboxstaging3"}}"
//servername:test_cloudboxstaging3

/*
 * 1. Get the domain URL
 * 2. get the deployserver.json
 * 3. get the key
 * 4. run the ant
 * */

public class ANTRunnerDeploy {

	/**
	 * @param args
	 * @throws JSONException
	 */
	public static void main(String[] args) throws JSONException {
		ArgumentParser.requiredParams.clear();
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("execute.method").setRequired(true));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("domainurl").setRequired(true));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("servername").setRequired(true));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("custom.ant.home").setRequired(false));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("buildfilepath").setRequired(true));// T
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("target").setRequired(false));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("Ant Targets").setRequired(false));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("antproperty").setRequired(false));// F

		final int requiredParams = ArgumentParser.parseArguments(args);
		if (requiredParams == 0) {
			// String url = System.getenv("cloudbox.domainurl");
			// url = "http://cloudboxonline.cloudmunch.com";
			String url = ArgumentParser.inputParams.getString("domainurl");
			final JSONArray deployServer = CBDataReader.readDeployServer(url);
			// System.out.println("deployServer" + deployServer);
			// deployServer = populateDeployServer(deployServer);
			final JSONObject serverDetail = CBDataFinder.findFromDeployServer(deployServer, ArgumentParser.inputParams.getString("servername"));
			String deployTempLoc = serverDetail.getString("deployTempLoc");
			// System.out.println("serverDetail" + serverDetail);

			// todo remove this
			// serverDetail.put("privateKeyLoc", "E:/Downloads/dcloud.pem");

			int exitCode = 0;
			if (ArgumentParser.inputParams.getString("execute.method").equalsIgnoreCase("SHELLLOGIN")) {
				exitCode = shellLoginMethod(serverDetail, deployTempLoc);
			} else if (ArgumentParser.inputParams.getString("execute.method").equalsIgnoreCase("REMOTECOMMAND")) {
				exitCode = remoteCommandMethod(serverDetail, deployTempLoc);
			} else {
				System.out.println("Invalid execution method, Valid Execuition methods are SHELLLOGIN or REMOTECOMMAND");
				exitCode = 2;
			}
			System.exit(exitCode);
		} else {
			System.out.println("Required Number of arguments not found");
			System.out.println(requiredParams + " arguments missing");
			System.exit(2);
		}
	}

	private static int remoteCommandMethod(JSONObject serverDetail, String deployTempLoc) throws JSONException {
		String antLoc = "";
		if (serverDetail.has("antLoc")) {
			antLoc = serverDetail.getString("antLoc");
		} else {
			antLoc = ArgumentParser.inputParams.getString("custom.ant.home");
		}
		antLoc = FilenameUtils.separatorsToSystem(antLoc + File.separator + "bin" + File.separator + "ant");
		final StringBuffer commandString = new StringBuffer();
		{
			commandString.append(antLoc + " ");
			{
				String buildFilePath = ArgumentParser.inputParams.getString("buildfilepath");
				String absoluteBuildFilePath = "";
//				if (buildFilePath.startsWith(File.separator)) {
				if (buildFilePath.startsWith("/")) {
					absoluteBuildFilePath = buildFilePath;
				} else {
					absoluteBuildFilePath = deployTempLoc + File.separator + buildFilePath;
				}
				commandString.append("-f " + absoluteBuildFilePath + " ");
			}
			if (ArgumentParser.inputParams.has("Ant Targets")) {
				final String[] targetArray = StringUtils.split(ArgumentParser.inputParams.getString("Ant Targets"), ",");
				for (int i = 0; i < targetArray.length; i++) {
					commandString.append(targetArray[i].trim() + " ");
				}
			} else if (ArgumentParser.inputParams.has("target")) {
				final String[] targetArray = StringUtils.split(ArgumentParser.inputParams.getString("target"), ",");
				for (int i = 0; i < targetArray.length; i++) {
					commandString.append(targetArray[i].trim() + " ");
				}
			}
			if (ArgumentParser.inputParams.has("antproperty")) {
				final String[] propertyArray = StringUtils.split(ArgumentParser.inputParams.getString("antproperty"), ",");
				for (int i = 0; i < propertyArray.length; i++) {
					commandString.append(propertyArray[i].trim() + " ");
				}

				// commandString.append(ArgumentParser.inputParams.has("antproperty"));
			}

		}
		final String command = FilenameUtils.separatorsToSystem(commandString.toString());
		// final String commandString =
		// FilenameUtils.separatorsToUnix(constructCommand(antLoc));
		SSHRunner runner = new SSHRunner(serverDetail.getString("dnsName"), "22", serverDetail.getString("loginUser"), "", serverDetail.getString("privateKeyLoc"));
		System.out.println("Executing Command : " + command);
		return runner.executeCommand(System.out, command, null);
		// return runner.executeCommand(System.out, "abcd", null);
	}

	private static int shellLoginMethod(final JSONObject serverDetail, String deployTempLoc) throws JSONException {
		try {
			boolean canConnect = validateServerConnection(serverDetail.getString("dnsName"), serverDetail.getString("loginUser"), serverDetail.getString("privateKeyLoc"));
			if (canConnect) {
				final ANTFinder.ANTBASE antBase = ANTFinder.findANTInstallation(serverDetail.getString("dnsName"), serverDetail.getString("loginUser"), serverDetail.getString("privateKeyLoc"));
				if (antBase != ANTFinder.ANTBASE.NONE) {
					if (antBase == ANTFinder.ANTBASE.ANTHOME) {
						System.out.println("Using ANT_HOME");
					} else if (antBase == ANTFinder.ANTBASE.PATH) {
						System.out.println("Using ant from PATH");
					} else if (antBase == ANTFinder.ANTBASE.CUSTOMANTHOME) {
						System.out.println("Using ANT path specified in this Apps parameter");
					}
					System.out.println("Running Remote ANT Command");

					final String antCommand = ANTFinder.getAntCommand();

					final String commandString = constructCommand(antCommand);

					final List<String> command = new ArrayList<String>();
					System.out.println("Deploy Temp Loc is " + deployTempLoc);
					deployTempLoc = "cd " + deployTempLoc;
					if (deployTempLoc != null && deployTempLoc.trim().length() > 0) {
						command.add(deployTempLoc);
					} else {
						System.out.println("Deploy Temp Loc is empty");
					}
					command.add(commandString.toString());
					command.add("echo $?");

					System.out.println("Command Executed : " + commandString.toString());
					final Map<String, String> outputOfCommand = SSHHelper.runCommand(serverDetail.getString("dnsName"), serverDetail.getString("loginUser"), serverDetail.getString("privateKeyLoc"),
							command);
					if (outputOfCommand == null) {
						return 5;
					}
					final String outputCode = outputOfCommand.get("echo $?").trim();
					final int exitCode = NumberUtils.toInt(outputCode);
					{
						if (outputOfCommand.containsKey(deployTempLoc)) {
							final String deployString = outputOfCommand.get(deployTempLoc).trim();
							System.out.println(deployString);
						}
					}
					final String outputString = outputOfCommand.get(commandString.toString()).trim();
					System.out.println(outputString);
					if (exitCode == 0) {
						System.out.println("ANT Execution Success");
					} else {
						System.out.println("ANT Execution Failed");
						return 4;
					}
				} else {
					return 3;
				}
				// Map<String, Object> outputOfCommand =
				// SSHHelper.runCommand(serverDetail.getString("dnsName"),
				// serverDetail.getString("loginUser"),
				// serverDetail.getString("privateKeyLoc"), command
				// .toString().trim());
				// SSHHelper.runCommand("cloudboxstaging3.cloudmunch.com",
				// "ec2-user", "E:/Downloads/dcloud.pem",
				// command.toString().trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}
		return 0;
	}

	private static String constructCommand(final String antCommand) throws JSONException {
		final StringBuffer commandString = new StringBuffer();
		commandString.append(antCommand + " ");
		commandString.append("-f " + ArgumentParser.inputParams.getString("buildfilepath") + " ");
		if (ArgumentParser.inputParams.has("target")) {
			final String[] targetArray = StringUtils.split(ArgumentParser.inputParams.getString("target"));
			for (int i = 0; i < targetArray.length; i++) {
				commandString.append(targetArray[i] + " ");
			}
		}
		if (ArgumentParser.inputParams.has("antproperty")) {
			commandString.append(ArgumentParser.inputParams.has("antproperty"));
		}
		return commandString.toString();
	}

	private static boolean validateServerConnection(String host, String uname, String keyFile) throws IOException {
		// List<String> command = new ArrayList<String>();
		// command.add("ls");
		// Map<String, String> outputOfCommand = SSHHelper.runCommand(host,
		// uname, keyFile, command);

		return true;
	}

	// private static JSONArray populateDeployServer(JSONArray deployServer)
	// throws JSONException {
	// try {
	// File f = new File("E:/temp/Deploy Server/deployserver.json");
	// String j = FileUtils.readFileToString(f);
	// deployServer = new JSONArray(j);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return deployServer;
	// }

}
