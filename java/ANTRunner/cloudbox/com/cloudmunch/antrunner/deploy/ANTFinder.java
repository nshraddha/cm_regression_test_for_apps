package com.cloudmunch.antrunner.deploy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONException;

import com.cloudmunch.argumentparser.ArgumentParser;
import com.cloudmunch.helper.SSHHelper;

public class ANTFinder {

	private static String antCommand = "";

	public enum ANTBASE {
		NONE, PATH, ANTHOME, CUSTOMANTHOME
	}

	public static String getAntCommand() {
		return antCommand;
	}

	public static ANTBASE findANTInstallation(String host, String uname, String keyFile) throws IOException, JSONException {

		System.out.println("Finding ANT in System PATH Variable");
		boolean antCommandSuccess = tryANTPath(host, uname, keyFile);

		if (antCommandSuccess) {
			return ANTBASE.PATH;
		}
		System.out.println("ANT is not set in System PATH Variable");
		System.out.println("Finding ANT at ANT_HOME Environment Variable");
		antCommandSuccess = tryANTHome(host, uname, keyFile);

		if (antCommandSuccess) {
			return ANTBASE.ANTHOME;
		}
		System.out.println("System Variable ANT_HOME is either not set or is not valid");

		System.out.println("Finding ANT using Custom ANT Path");
		antCommandSuccess = tryCustomANTHome(host, uname, keyFile);

		if (antCommandSuccess) {
			return ANTBASE.CUSTOMANTHOME;
		}
		System.out.println("ANT Not found in any of the possible Variable");

		return ANTBASE.NONE;
	}

	private static boolean tryCustomANTHome(String host, String uname, String keyFile) throws IOException, JSONException {
		if (ArgumentParser.inputParams.has("custom.ant.home")) {
			String antHome = ArgumentParser.inputParams.getString("custom.ant.home");
			if (antHome != null && antHome.isEmpty() == false) {
				String foundPath = antHome + File.separator + "bin" + File.separator + "ant -version";
				String oneOfcommand = FilenameUtils.separatorsToSystem(foundPath);
//				String oneOfcommand = FilenameUtils.separatorsToUnix(foundPath);
				List<String> command = new ArrayList<String>();
				command.add(oneOfcommand);
				command.add("echo $?");
				Map<String, String> output = SSHHelper.runCommand(host, uname, keyFile, command);
				String outputCode = output.get("echo $?").trim();
				int exitCode = NumberUtils.toInt(outputCode);
				if (exitCode == 0) {
					antCommand = antHome + File.separator + "bin" + File.separator + "ant";
//					antCommand = FilenameUtils.separatorsToUnix(antCommand);
					antCommand = FilenameUtils.separatorsToSystem(antCommand);
					return true;
				} else {
					return false;
				}
			}
		} else {
			System.out.println("ANT Home Value not passed as Param");
		}
		return false;
	}

	private static boolean tryANTHome(String host, String uname, String keyFile) throws IOException {
		String antHome = findANTHome(host, uname, keyFile).trim();
		if (antHome != null && antHome.isEmpty() == false) {
			String foundPath = antHome + File.separator + "bin" + File.separator + "ant -version";
			String oneOfcommand = FilenameUtils.separatorsToSystem(foundPath);
//			String oneOfcommand = FilenameUtils.separatorsToUnix(foundPath);
			List<String> command = new ArrayList<String>();
			command.add(oneOfcommand);
			command.add("echo $?");
			Map<String, String> output = SSHHelper.runCommand(host, uname, keyFile, command);
			String outputCode = output.get("echo $?").trim();
			int exitCode = NumberUtils.toInt(outputCode);
			if (exitCode == 0) {
				antCommand = antHome + File.separator + "bin" + File.separator + "ant";
//				antCommand = FilenameUtils.separatorsToUnix(antCommand);
				antCommand = FilenameUtils.separatorsToSystem(antCommand);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private static String findANTHome(String host, String uname, String keyFile) throws IOException {
		List<String> command = new ArrayList<String>();
		command.add("echo $ANT_HOME");
		command.add("echo $?");
		Map<String, String> output = SSHHelper.runCommand(host, uname, keyFile, command);
//		String outputCode = output.get("echo $?").trim();
//		int exitCode = NumberUtils.toInt(outputCode);///
		String retString = output.get("echo $ANT_HOME").trim();
		return retString;
	}

	private static boolean tryANTPath(String host, String uname, String keyFile) throws IOException {
		List<String> command = new ArrayList<String>();
		command.add("ant -version");
		command.add("echo $?");
		Map<String, String> output = SSHHelper.runCommand(host, uname, keyFile, command);
		String outputCode = output.get("echo $?").trim();
		int exitCode = NumberUtils.toInt(outputCode);
		if (exitCode == 0) {
			antCommand = "ant";
			return true;
		} else {
			return false;
		}
	}

}
