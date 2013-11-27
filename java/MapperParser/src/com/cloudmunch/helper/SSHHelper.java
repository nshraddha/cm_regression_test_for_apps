package com.cloudmunch.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.cloudmunch.helper.ssh.SSHRunner;
import com.jcraft.jsch.Channel;

public class SSHHelper {

	public static final String EXIT_CODE = "exitCode";
	public static final String CONSOLE_LOG = "consoleLog";

	public static Map<String, String> runCommand(String host, String uname, String keyFile, List<String> command) throws IOException {
		Map<String, String> retList = new HashMap<String, String>();

		// ////////////////////////////////////////////////////////////
		String port = "22";
		SSHRunner runner = new SSHRunner(host, port, uname, "", keyFile);
		Channel channel = runner.executeCommandThroughShell(System.out);

		if (channel == null) {
			System.out.println("Couldnt Connect to the Server");
			return null;
		}

		PrintStream shellStream = new PrintStream(channel.getOutputStream());
		String prompt = "";
		prompt = getPrompt(channel, shellStream);
		for (int i = 0; i < command.size(); i++) {
			System.out.println("Executing Command " + i + " " + command.get(i));
			String output = runCommandAndGetOutput(channel, shellStream, command.get(i), prompt);
			retList.put(command.get(i), removePromptAndCommand(output, prompt, command.get(i)));
		}

		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// command.add(0, System.getProperty("line.separator"));
		// runner.executeCommandThroughShell2(System.out, command, baos);
		// command.remove(System.getProperty("line.separator"));
		// SSHShellParser.parseShellOutput(command, new
		// ByteArrayInputStream(baos.toByteArray()));
		// ////////////////////////////////////////////////////////////

		runner.close(channel);

		return retList;
	}

	private static String removePromptAndCommand(String output, String prompt, String command) {
		StringBuffer sb = new StringBuffer();
		try {
			List<String> linesFromUtil = IOUtils.readLines(new ByteArrayInputStream(output.getBytes()));
			for (int i = 0; i < linesFromUtil.size(); i++) {
				String currentLine = linesFromUtil.get(i).trim();
				if (currentLine.indexOf(prompt) > -1 || currentLine.indexOf(command) > -1) {

				} else {
					sb.append(currentLine).append(System.getProperty("line.separator"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	private static String runCommandAndGetOutput(Channel channel, PrintStream shellStream, String command, String prompt) {
		if (channel.isConnected()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			channel.setOutputStream(baos, false);
			shellStream.println(command);
			shellStream.flush();
			while (true) {
				List<String> linesFromUtil;
				try {
					linesFromUtil = IOUtils.readLines(new ByteArrayInputStream(baos.toByteArray()));
					String lastLine = "";
					while (linesFromUtil.size() == 0) {
						sleep(100);
						linesFromUtil = IOUtils.readLines(new ByteArrayInputStream(baos.toByteArray()));
					}
					lastLine = linesFromUtil.get(linesFromUtil.size() - 1);
					if (lastLine.indexOf(prompt) > -1) {
						break;
					}
					sleep(1000);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return baos.toString();
		} else {
			return "";
		}
	}

	private static String getPrompt(Channel channel, PrintStream shellStream) throws IOException {
		// Get the prompt
		sleep(500);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		channel.setOutputStream(baos, false);
		shellStream.println(System.getProperty("line.separator"));
		shellStream.flush();
		sleep(500);
		// System.out.println(baos.toString());
		List<String> linesFromUtil = IOUtils.readLines(new ByteArrayInputStream(baos.toByteArray()));
		List<String> lines = new ArrayList<String>();
		for (int i = 0; i < linesFromUtil.size(); i++) {
			String nextLine = linesFromUtil.get(i).trim();
			if (!lines.contains(nextLine) && nextLine.length() > 0)
				lines.add(nextLine);
		}
		// System.out.println(lines);
		if (lines.size() > 0)
			return lines.get(0).trim();
		else
			return "";
	}

	public static void main(String[] args) throws IOException {
		List<String> command = new ArrayList<String>();
		command.add("echo $ANT_HOME");
		command.add("ant -f /var/cloudbox/CBApp/CBAppDeploy.xml");
		// command.add("exit");
		Map<String, String> m = runCommand("cloudboxstaging3.cloudmunch.com", "ec2-user", "E:/Downloads/dcloud.pem", command);
		System.out.println("echo $ANT_HOME : " + m.get("echo $ANT_HOME"));
		String nextCommand = m.get("echo $ANT_HOME").trim() + "/bin/ant -version";
		command.add(nextCommand);
		m = runCommand("cloudboxstaging3.cloudmunch.com", "ec2-user", "E:/Downloads/dcloud.pem", command);
		System.out.println(nextCommand + m.get(nextCommand));

	}

	private static void sleep(int millisec) {
		try {
			Thread.sleep(millisec);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

}
