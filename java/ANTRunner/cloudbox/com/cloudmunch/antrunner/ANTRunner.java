package com.cloudmunch.antrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.launch.LaunchException;
import org.json.JSONException;

import com.cloudmunch.argumentparser.ArgumentParser;
import com.cloudmunch.argumentparser.ParameterHandler;

public class ANTRunner {

	public enum PARAMETERS {
		buildfilepath, antproperty, target, outputfile
	}

	/**
	 * @param args
	 * @throws JSONException
	 */
	public static void main(String[] args) throws JSONException {
		ArgumentParser.requiredParams.clear();
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("ant.lib").setRequired(true));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("custom.lib").setRequired(false));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("custom.lib2").setRequired(false));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("buildfilepath").setRequired(true));// T
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("target").setRequired(false));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("Ant Targets").setRequired(false));
		ArgumentParser.requiredParams.add(new ParameterHandler().setParameter("antproperty").setRequired(false));// F

		// ArgumentParser.requiredParams.add(new
		// ParameterHandler().setParameter("ant.home").setRequired(false));//T
		// ArgumentParser.requiredParams.add(new
		// ParameterHandler().setParameter("outputfile").setRequired(false));//F

		int s = ArgumentParser.parseArguments(args);
		System.out.println(s);
		int exitCode = 0;

		if (s == 0) {
			// Launcher.main(new
			// String[]{"-f","E:/GITWorkSpace_new1/CloudBox/Other Projects/ANTRunner/build.xml","resolve"});
			// Launcher.main(new
			// String[]{"-cp","E:/GITWorkSpace_new1/CloudBox/Other Projects/ANTRunner/lib2/ivy-2.2.0.jar","-f","build.xml","resolve"});
			List<String> cmdAntArgs = new ArrayList<String>();
			cmdAntArgs.add("-lib");
			cmdAntArgs.add(ArgumentParser.inputParams.getString("ant.lib"));
			cmdAntArgs.add("-f");
			cmdAntArgs.add(ArgumentParser.inputParams.getString("buildfilepath"));
			String targetList = "";
			{
				if (ArgumentParser.inputParams.has("target")) {
					targetList = ArgumentParser.inputParams.getString("target");
				} else if (ArgumentParser.inputParams.has("Ant Targets")) {
					targetList = ArgumentParser.inputParams.getString("Ant Targets");
				}
			}
			if (targetList.indexOf(",") >= 0) {
				targetList = StringUtils.replace(targetList, ",", " ");
				StringTokenizer st = new StringTokenizer(targetList, " ");
				while (st.hasMoreTokens()) {
					cmdAntArgs.add(st.nextToken().trim());
				}
			} else {
				if (targetList.trim().length() > 0) {
					cmdAntArgs.add(targetList);
				}
			}
			if (ArgumentParser.inputParams.has("custom.lib")) {
				cmdAntArgs.add("-lib");
				cmdAntArgs.add(ArgumentParser.inputParams.getString("custom.lib"));
			}
			if (ArgumentParser.inputParams.has("custom.lib2")) {
				cmdAntArgs.add("-lib");
				cmdAntArgs.add(ArgumentParser.inputParams.getString("custom.lib2"));
			}

			if (ArgumentParser.inputParams.has("antproperty")) {
				String[] strArray = StringUtils.split(ArgumentParser.inputParams.getString("antproperty"), " ");
				for (int i = 0; i < strArray.length; i++) {
					cmdAntArgs.add(strArray[i].trim());
				}
			}

			ANTLauncher launcher = new ANTLauncher();
			try {
				exitCode = launcher.run(cmdAntArgs.toArray(new String[cmdAntArgs.size()]));
			} catch (LaunchException e) {
				exitCode = 2;
				// System.err.println(e.getMessage());
			} catch (Throwable t) {
				exitCode = 2;
				// t.printStackTrace(System.err);
			}

			// Main m = new Main();
			// m.startAnt(new String[]{"-f","build.xml","resolve"}, null, null);
		} else {
			exitCode = 1;
		}
		System.exit(exitCode);
	}

	private static String formatInput(String category, String value) {
		PARAMETERS p = PARAMETERS.valueOf(category);
		String retValue = "";
		switch (p) {
		// ProjectPath, Configuration, BUILD_NUMBER, Dest, OutputDeploy
		case buildfilepath:
			retValue = "\"" + value + "\"";
			break;
		case antproperty:
			retValue = "\"" + value + "\"";
			break;
		case target:
			retValue = value;
			break;
		case outputfile:
			retValue = value;
			break;
		default:
			retValue = value;
		}
		return retValue;
	}

}
