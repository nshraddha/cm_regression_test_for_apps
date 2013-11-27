package com.cloudmunch.checkstyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.DefaultLogger;
import com.puppycrawl.tools.checkstyle.Main;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.XMLLogger;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Utils;

/**
 * Wrapper command line program for the Checker overwritten within Cloudmunch as actual Checker Program exits immediately once the check completed.
 *  
 * @author Leela
 * 
 */
public class CMCheckStyleMain {

	/** the options to the command line */
	private static final Options OPTS = new Options();
	static {
		OPTS.addOption("c", true, "The check configuration file to use.");
		OPTS.addOption("r", true, "Traverse the directory for source files");
		OPTS.addOption("o", true, "Sets the output file. Defaults to stdout");
		OPTS.addOption("p", true, "Loads the properties file");
		OPTS.addOption("f", true, "Sets the output format. (plain|xml). Defaults to plain");
	}

	private static int numberOfErrors;

	public static int getNumberOfErrors() {
		return numberOfErrors;
	}

	/** Stop instances being created. */
	private CMCheckStyleMain() {
	}

	/**
	 * Loops over the files specified checking them for errors. The exit code is the number of errors found in all the files.
	 * Removed System.exit method from this main, so the control can come back to app even after validation.
	 * 
	 * @param aArgs
	 *            the command line arguments
	 **/
	public static void main(String[] aArgs) {
		// parse the parameters
		final CommandLineParser clp = new PosixParser();
		CommandLine line = null;
		try {
			line = clp.parse(OPTS, aArgs);
		} catch (final ParseException e) {
			e.printStackTrace();
			usage();
		}
		assert line != null;

		// setup the properties
		final Properties props = line.hasOption("p") ? loadProperties(new File(line.getOptionValue("p"))) : System.getProperties();

		// ensure a config file is specified
		if (!line.hasOption("c")) {
			System.out.println("Must specify a config XML file.");
			usage();
		}

		final Configuration config = loadConfig(line, props);

		// setup the output stream
		OutputStream out = null;
		boolean closeOut = false;
		if (line.hasOption("o")) {
			final String fname = line.getOptionValue("o");
			try {
				out = new FileOutputStream(fname);
				closeOut = true;
			} catch (final FileNotFoundException e) {
				System.out.println("Could not find file: '" + fname + "'");
				System.exit(1);
			}
		} else {
			out = System.out;
			closeOut = false;
		}

		final AuditListener listener = createListener(line, out, closeOut);
		final List<File> files = getFilesToProcess(line);
		final Checker c = createChecker(config, listener);
		final int numErrs = c.process(files);
		c.destroy();
		System.out.println(numErrs);
		numberOfErrors = numErrs;
		// System.exit(numErrs);
	}

	/**
	 * Loads properties from a File.
	 * 
	 * @param aFile
	 *            the properties file
	 * @return the properties in aFile
	 */
	private static Properties loadProperties(File aFile) {
		final Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(aFile);
			properties.load(fis);
		} catch (final IOException ex) {
			System.out.println("Unable to load properties from file: " + aFile.getAbsolutePath());
			ex.printStackTrace(System.out);
			System.exit(1);
		} finally {
			Utils.closeQuietly(fis);
		}

		return properties;
	}

	/**
	 * Loads the configuration file. Will exit if unable to load.
	 * 
	 * @param aLine
	 *            specifies the location of the configuration
	 * @param aProps
	 *            the properties to resolve with the configuration
	 * @return a fresh new configuration
	 */
	private static Configuration loadConfig(CommandLine aLine, Properties aProps) {
		try {
			return ConfigurationLoader.loadConfiguration(aLine.getOptionValue("c"), new PropertiesExpander(aProps));
		} catch (final CheckstyleException e) {
			System.out.println("Error loading configuration file");
			e.printStackTrace(System.out);
			System.exit(1);
			return null; // can never get here
		}
	}

	/** Prints the usage information. **/
	private static void usage() {
		final HelpFormatter hf = new HelpFormatter();
		hf.printHelp("java " + Main.class.getName() + " [options] -c <config.xml> file...", OPTS);
		System.exit(1);
	}

	/**
	 * Creates the Checker object.
	 * 
	 * @param aConfig
	 *            the configuration to use
	 * @param aNosy
	 *            the sticky beak to track what happens
	 * @return a nice new fresh Checker
	 */
	private static Checker createChecker(Configuration aConfig, AuditListener aNosy) {
		Checker c = null;
		try {
			c = new Checker();

			final ClassLoader moduleClassLoader = Checker.class.getClassLoader();
			c.setModuleClassLoader(moduleClassLoader);
			c.configure(aConfig);
			c.addListener(aNosy);
		} catch (final Exception e) {
			System.out.println("Unable to create Checker: " + e.getMessage());
			e.printStackTrace(System.out);
			System.exit(1);
		}
		return c;
	}

	/**
	 * Determines the files to process.
	 * 
	 * @param aLine
	 *            the command line options specifying what files to process
	 * @return list of files to process
	 */
	private static List<File> getFilesToProcess(CommandLine aLine) {
		final List<File> files = Lists.newLinkedList();
		if (aLine.hasOption("r")) {
			final String[] values = aLine.getOptionValues("r");
			for (String element : values) {
				traverse(new File(element), files);
			}
		}

		final String[] remainingArgs = aLine.getArgs();
		for (String element : remainingArgs) {
			files.add(new File(element));
		}

		if (files.isEmpty() && !aLine.hasOption("r")) {
			System.out.println("Must specify files to process");
			usage();
		}
		return files;
	}

	/**
	 * Traverses a specified node looking for files to check. Found files are added to a specified list. Subdirectories are also traversed.
	 * 
	 * @param aNode
	 *            the node to process
	 * @param aFiles
	 *            list to add found files to
	 */
	private static void traverse(File aNode, List<File> aFiles) {
		if (aNode.canRead()) {
			if (aNode.isDirectory()) {
				final File[] nodes = aNode.listFiles();
				for (File element : nodes) {
					traverse(element, aFiles);
				}
			} else if (aNode.isFile()) {
				aFiles.add(aNode);
			}
		}
	}

	/**
	 * Create the audit listener
	 * 
	 * @param aLine
	 *            command line options supplied
	 * @param aOut
	 *            the stream to log to
	 * @param aCloseOut
	 *            whether the stream should be closed
	 * @return a fresh new <code>AuditListener</code>
	 */
	private static AuditListener createListener(CommandLine aLine, OutputStream aOut, boolean aCloseOut) {
		final String format = aLine.hasOption("f") ? aLine.getOptionValue("f") : "plain";

		AuditListener listener = null;
		if ("xml".equals(format)) {
			listener = new XMLLogger(aOut, aCloseOut);
		} else if ("plain".equals(format)) {
			listener = new DefaultLogger(aOut, aCloseOut);
		} else {
			System.out.println("Invalid format: (" + format + "). Must be 'plain' or 'xml'.");
			usage();
		}
		return listener;
	}

}
