package com.cloudbox.java.instrument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.esotericsoftware.wildcard.Paths;
import com.vladium.emma.instr.InstrProcessor;

public class JavaInstrument {

	public static String INCLUDE_STR = "include";
	public static String EXCLUDE_STR = "exclude";

	public static String METADATA_FILE = "cmmeta.emma";
	public static String TEMP_DIR_NAME = "cm_temp";
	public static String CLASSLOCATION_WAR = "WEB-INF" + File.separator + "classes";

	public static String sourceInputFile = "";
	public static String emmaMetaFileName = "";
	public static String destinationOutputDir = "";
	public static String[] inputJarsList = null;// , ignoreJarList = null;
	public static String mode = "include";

	public static StringBuffer message = new StringBuffer();

	public static List<String> jarsList = new ArrayList<String>();
	public static List<String> encounteredJars = new ArrayList<String>();

	// public static List<String> instrumentJarList = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		parseArguments(args);

		StringTokenizer st = new StringTokenizer(sourceInputFile, ",");
		String sourceFileItem = "";
		
		if(emmaMetaFileName.length() > 0)
			METADATA_FILE = emmaMetaFileName;
			
		FileUtils.deleteQuietly(new File(new File(destinationOutputDir), METADATA_FILE));

		while (st.hasMoreTokens()) {
			sourceFileItem = st.nextToken();

			File sourceFile = new File(sourceFileItem);
			File destinationDirectory = new File(destinationOutputDir);


			if (sourceFile.exists() == false) {
				System.out.println("Source File Does not Exists");
				message.append("Source File Does not Exists").append("\r\n");
			} else {

				if (destinationDirectory.exists() == false) {
					destinationDirectory.mkdirs();
				}

				if (destinationDirectory.getAbsolutePath().equalsIgnoreCase(sourceFile.getParent())) {
					System.out.println("Source and Destination directory cannot be same. As the instrumented file will have same name.");
					message.append("Source and Destination directory cannot be same. As the instrumented file will have same name.").append("\r\n");
				} else {

					File destWorking = new File(destinationDirectory.getAbsoluteFile() + File.separator + TEMP_DIR_NAME);
					try {
						FileUtils.deleteQuietly(destWorking);
						FileUtils.forceMkdir(destWorking);
					} catch (Exception e) {
						// ignore as we can still proceed
					}

					if (sourceFile.getName().endsWith(".jar")) {
						instrumentItem(destWorking.getAbsolutePath(), destinationDirectory.getAbsolutePath(), new String[] { "" + sourceFile.getAbsoluteFile() });
						FileUtils.moveFileToDirectory(new File(destWorking, "lib" + File.separator + sourceFile.getName()), destinationDirectory, false);
					} else if (sourceFile.getName().endsWith(".war")) {
						instrumentWAR(sourceFile, destinationDirectory, destinationDirectory, destWorking);
						FileUtils.deleteQuietly(destWorking);
					} else if (sourceFile.getName().endsWith(".ear")) {
						Expand ex = new Expand();
						ex.setProject(new Project());
						ex.getProject().setName("JavaInstrument");
						String fileName = sourceFile.getName();
						File earExtracted = new File(destWorking.getAbsolutePath(), fileName.substring(0, fileName.indexOf(".")));
						earExtracted.mkdirs();
						ex.setDest(earExtracted);
						ex.setSrc(sourceFile);
						ex.execute();

						String[] warList = findFileListInArchieve(earExtracted.getAbsolutePath(), "war");

						for (int i = 0; i < warList.length; i++) {
							// System.out.println(warList[i]);
							File workingDir = new File(earExtracted, TEMP_DIR_NAME);
							instrumentWAR(new File(warList[i]), earExtracted, destinationDirectory, workingDir);
							FileUtils.deleteQuietly(workingDir);
						}

						String[] jarList = findFileListInArchieve(earExtracted.getAbsolutePath() + File.separator + "lib", "jar");
						File workingDir = new File(earExtracted, TEMP_DIR_NAME);
						FileUtils.forceMkdir(workingDir);
						instrumentItem(workingDir.getAbsolutePath(), destinationDirectory.getAbsolutePath(), jarList);
						FileUtils.copyDirectory(new File(workingDir, "lib"), new File(earExtracted, "lib"));
						FileUtils.deleteQuietly(workingDir);

						Zip z = new Zip();
						z.setBasedir(earExtracted);
						z.setDestFile(new File(destWorking, sourceFile.getName()));
						z.setProject(new Project());
						z.getProject().setName("CMZipper");
						z.setCompress(true);
						z.setUpdate(false);
						try {
							z.execute();
							FileUtils.deleteQuietly(new File(destinationDirectory, z.getDestFile().getName()));
							FileUtils.moveFileToDirectory(z.getDestFile(), destinationDirectory, true);
						} catch (Exception e) {
							e.printStackTrace();
						}

						FileUtils.deleteQuietly(destWorking);
					}
					System.out.println("Encountered Jar List : " + encounteredJars);
					if (inputJarsList == null) {
						inputJarsList = new String[] { "" };
					}
					System.out.println("Input Jar List : " + Arrays.asList(inputJarsList));
					System.out.println("Mode of Filter : " + mode);
					System.out.println("Instrumented Jars List : " + jarsList);
				}
			}
		}
		{
			try {
				JSONObject report = new JSONObject();

				JSONArray arrayJson;

				arrayJson = new JSONArray(encounteredJars);
				report.put("Encountered Jar List", arrayJson);

				arrayJson = new JSONArray(jarsList);
				report.put("Instrumnted Jar List", arrayJson);

				JSONObject input = new JSONObject();

				// -jarlist log4j.jar,struts.jar -mode exclude

				if (inputJarsList == null) {
					inputJarsList = new String[] { "" };
				}

				arrayJson = new JSONArray(inputJarsList);
				input.put("Jar List", arrayJson);

				input.put("Filter Mode", mode);
				input.put("sourceArchive", sourceInputFile);
				input.put("destinationDir", destinationOutputDir);

				report.put("input", input);
				report.put("message", message.toString());

				File reportFile = new File(new File(destinationOutputDir), "report.json");
				FileUtils.writeByteArrayToFile(reportFile, report.toString().getBytes(), false);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private static void parseArguments(String[] args) {
		Options options = new Options();

		Option source = new Option("s", "source", true, "The JAR / WAR / EAR archives to instrument");
		source.setRequired(true);

		Option destination = new Option("d", "destinationdir", true, "Location to place the instrumented file(s)");
		destination.setRequired(true);

		Option includeList = new Option("j", "jarlist", true, "List of jars in the archive to apply filter");
		includeList.setRequired(false);

		Option modeOption = new Option("m", "mode", true, "Mode of the Filter, either Include in or Exclude from Instrumentation process");
		modeOption.setRequired(false);

		Option emmaMetaFile = new Option("e", "emmametafile", true, "Name of the Meta data file (Will be overwritten if the file already exists)");
		emmaMetaFile.setRequired(true);
		
		options.addOption(source);
		options.addOption(destination);
		options.addOption(includeList);
		options.addOption(modeOption);
		options.addOption(emmaMetaFile);

		// options.addOption("s", "source", true,
		// "The JAR / WAR / EAR archives to instrument");
		// options.addOption("d", "destinationdir", true,
		// "Location to place the instrumented file(s)");
		// options.addOption("i", "instrumentjarlist", true,
		// "List of jars in the archive to instrument");

		// HelpFormatter formatter = new HelpFormatter();
		// formatter.printHelp("JavaInstrument", options);

		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption(emmaMetaFile.getLongOpt())) {
				emmaMetaFileName = cmd.getOptionValue(emmaMetaFile.getLongOpt());
			}
			if (cmd.hasOption("source")) {
				sourceInputFile = cmd.getOptionValue("source");
			}
			if (cmd.hasOption("destinationdir")) {
				destinationOutputDir = cmd.getOptionValue("destinationdir");
			}
			if (cmd.hasOption("mode")) {
				mode = cmd.getOptionValue("mode");
			} else {
				mode = INCLUDE_STR;
			}
			if (cmd.hasOption("jarlist")) {
				inputJarsList = StringUtils.split(cmd.getOptionValue("jarlist"), ",");
				// StringUtils.indexOfAny("", new String[]{"",""});
			} else {
				mode = EXCLUDE_STR;
			}
		} catch (MissingOptionException e) {
			@SuppressWarnings("unchecked")
			List<String> missedOptions = e.getMissingOptions();
			for (int i = 0; i < missedOptions.size(); i++) {
				System.out.println(options.getOption(missedOptions.get(i)));
				message.append("Missing Option : " + options.getOption(missedOptions.get(i))).append("\r\n");
			}
			System.exit(1);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			message.append(e.getMessage()).append("\r\n");
		}
	}

	private static void instrumentWAR(File sourceFile, File destinationDirectory, File metaDataDirectory, File destWorking) throws IOException {
		String[] sourceList = null;
		Expand ex = new Expand();
		ex.setProject(new Project());
		ex.getProject().setName("JavaInstrument");
		String fileName = sourceFile.getName();
		File warExtracted = new File(destWorking.getAbsolutePath(), fileName.substring(0, fileName.indexOf(".")));
		warExtracted.mkdirs();
		ex.setDest(warExtracted);
		ex.setSrc(sourceFile);
		ex.execute();
		sourceList = new String[1];
		File classessLocation = new File(warExtracted.getAbsolutePath(), CLASSLOCATION_WAR);
		if (classessLocation.exists()) {
			sourceList[0] = classessLocation.getAbsolutePath();
			instrumentItem(destWorking.getAbsolutePath(), metaDataDirectory.getAbsolutePath(), sourceList);
		}

		sourceList = findFileListInArchieve(warExtracted.getAbsolutePath() + File.separator + "WEB-INF/lib/", "jar");

		instrumentItem(destWorking.getAbsolutePath(), metaDataDirectory.getAbsolutePath(), sourceList);
		{
			if (classessLocation.exists()) {
				// replace instrumented .class files
				File srcFile = new File(destWorking, "classes" + File.separator);
				File destFile = new File(warExtracted, "WEB-INF" + File.separator);
				// System.out.println(srcFile.getAbsolutePath());
				// System.out.println(destFile.getAbsolutePath());
				FileUtils.copyDirectoryToDirectory(srcFile, destFile);
			}

			// replace instrumented .jars
			// Same Destination
			{
				File srcFile = new File(destWorking, "lib" + File.separator);
				File destFile = new File(warExtracted, "WEB-INF" + File.separator);
				// System.out.println(srcFile.getAbsolutePath());
				// System.out.println(destFile.getAbsolutePath());
				if (srcFile.exists()) {
					FileUtils.copyDirectoryToDirectory(srcFile, destFile);
				}
			}
		}
		// {
		// // replace instrumented .jars
		// File srcFile = new File(destWorking, "lib\\");
		// File destFile = new File(warExtracted, "WEB-INF\\");
		// System.out.println(srcFile.getAbsolutePath());
		// System.out.println(destFile.getAbsolutePath());
		// FileUtils.copyDirectoryToDirectory(srcFile, destFile);
		// }

		Zip z = new Zip();
		z.setBasedir(warExtracted);
		z.setDestFile(new File(destWorking, sourceFile.getName()));
		z.setProject(new Project());
		z.getProject().setName("CMZipper");
		z.setCompress(true);
		z.setUpdate(false);
		try {
			z.execute();
			FileUtils.copyFileToDirectory(z.getDestFile(), destinationDirectory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String[] findFileListInArchieve(String directory, String filter) {
		Paths p = new Paths();
		p.glob(directory, "*." + filter);
		List<String> listOfClassFiles = Arrays.asList(p.getRelativePaths());
		String[] sourceList = new String[listOfClassFiles.size()];
		for (int i = 0; i < listOfClassFiles.size(); i++) {
			// System.out.println(directory + File.separator +
			// listOfClassFiles.get(i));
			sourceList[i] = directory + File.separator + listOfClassFiles.get(i);
		}
		return sourceList;
	}

	private static void instrumentItem(String instrumentingDirectory, String metaDirectory, String[] sourceList) {
		sourceList = applyInclusionOrExclusionFilter(sourceList);
		if (sourceList != null && sourceList.length > 0) {
			InstrProcessor processor = InstrProcessor.create();
			processor.setInstrPath(sourceList, false);
			processor.setInstrOutDir(instrumentingDirectory);
			processor.setOutMode(InstrProcessor.OutMode.OUT_MODE_FULLCOPY);
			processor.setMetaOutFile(metaDirectory + File.separator + METADATA_FILE);
			processor.setMetaOutMerge(true);

			processor.run();
		}
	}

	private static String[] applyInclusionOrExclusionFilter(String[] sourceList) {
		List<String> sourceArrayList = new ArrayList<String>();
		if (sourceList != null && sourceList.length > 0) {
			for (int i = 0; i < sourceList.length; i++) {
				// String item = new File(sourceList[i]).getName();
				// String itemFullPath = sourceList[i];
				if (sourceList[i].endsWith(CLASSLOCATION_WAR)) {
					sourceArrayList.add(sourceList[i]);
					continue;
				}
				int index = StringUtils.indexOfAny(sourceList[i], inputJarsList);
				// System.out.println(index);
				encounteredJars.add(new File(sourceList[i]).getName());
				if (StringUtils.equalsIgnoreCase(mode, INCLUDE_STR) != true && StringUtils.equalsIgnoreCase(mode, EXCLUDE_STR)) {
					sourceArrayList.add(sourceList[i]);
					jarsList.add(new File(sourceList[i]).getName());
				} else {
					if (index != -1) {
						// found in the list
						if (StringUtils.equalsIgnoreCase(mode, INCLUDE_STR)) {
							sourceArrayList.add(sourceList[i]);
							jarsList.add(new File(sourceList[i]).getName());
						}
					} else {
						// not found in the list
						if (StringUtils.equalsIgnoreCase(mode, EXCLUDE_STR)) {
							sourceArrayList.add(sourceList[i]);
							jarsList.add(new File(sourceList[i]).getName());
						}
					}
				}
			}
		}
		return sourceArrayList.toArray(new String[sourceArrayList.size()]);
	}
}
