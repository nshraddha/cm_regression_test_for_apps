package com.cloudbox.tcp.jenkins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.cloudbox.tcp.ftp.CloudboxFileTransfer;
import com.cloudbox.tcp.objects.CloudboxExistingCommand;
import com.cloudbox.tcp.objects.CloudboxNewCommand;
import com.cloudbox.utils.CloudboxWriter;

public class JenkinsTCPSender {

	private JenkinsTCPSenderObject rObject = null;

	private static List<String> listOfArgument = new ArrayList<String>();
	private static CloudboxNewCommand cnc = new CloudboxNewCommand("", 0L);
	private static CloudboxFileTransfer cft = new CloudboxFileTransfer();

	public static void init() {
		listOfArgument.add("host");
		listOfArgument.add("port");
		listOfArgument.add("command");
		listOfArgument.add("timeout");
		listOfArgument.add("polling");
		listOfArgument.add("subcommand");
		listOfArgument.add("commandargument");
		listOfArgument.add("remotereportlocation");
		listOfArgument.add("localreportlocation");
	}

	public JenkinsTCPSender(String targetCommandToRun) {
		init();
		rObject = new JenkinsTCPSenderObject(targetCommandToRun);
	}

	public JenkinsTCPSender(JenkinsTCPSenderObject targetCommandToRun) {
		init();
		rObject = targetCommandToRun;
	}

	public JenkinsTCPSender() {
		init();
	}

	public JenkinsTCPSenderObject getrObject() {
		return rObject;
	}

	public void setrObject(JenkinsTCPSenderObject rObject) {
		this.rObject = rObject;
	}

	public static void main(String[] args) {
		JenkinsTCPSender.init();
		JenkinsTCPSenderObject senderObject = new JenkinsTCPSenderObject();
		if (args.length > 0) {
			int i = 0;
			while (i < args.length) {
				if (args[i].startsWith("-") && !args[i + 1].startsWith("-")) {
					parseValue(args[i++], args[i++], senderObject);
				} else {
					i++;
				}
			}

			CloudboxWriter.println(cnc);
			CloudboxWriter.println(senderObject);

			i = 0;
			boolean didTimeOut = false;
			while (i < senderObject.getNumberOfSubCommands()) {
				cnc.setSubArgument(senderObject.getSubCommandAtIndex(i++));
				Object o = JenkinsTCPDataSender.sendData(senderObject.getHost(), senderObject.getPort(), cnc);
				CloudboxWriter.println(o);

				if (o instanceof String) {
					String threadID = (String) o;
					CloudboxExistingCommand cec = new CloudboxExistingCommand(Long.parseLong(threadID));
					int timeOut = senderObject.getTimeout();
					do {
						cec = (CloudboxExistingCommand) JenkinsTCPDataSender.sendData(senderObject.getHost(), senderObject.getPort(), cec);
						timeOut -= senderObject.getPollTiming();
						try {
							CloudboxWriter.println("Waiting for " + senderObject.getPollTiming() + " second(s)");
							Thread.sleep(senderObject.getPollTiming() * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						CloudboxWriter.println("Thread ID : " + cec.getThreadID() + " : " + cec.isRunning() + " (Remaining timewait - " + timeOut + " sec)");
					} while (cec.isRunning() == true && timeOut > 0);
					if (timeOut > 0) {
						didTimeOut = false;
					} else {
						didTimeOut = true;
					}
					CloudboxWriter.println("Thread ID : " + cec.getThreadID() + " Completed");
				}
			}
			// get the report
			if (didTimeOut == false && cft.getRemotePathToCopy().isEmpty() == false) {
				if (cft.isValid()) {
					JenkinsTCPDataSender.sendAndReceiveFile(senderObject.getHost(), senderObject.getPort(), cft);
				} else {
					CloudboxWriter.println("File Transfer Arguments not valid");
				}
			}
		} else {
			CloudboxWriter.println("Required Arguments not Set");
		}
	}

	private static void parseValue(String key, String value, JenkinsTCPSenderObject senderObject) {
		switch (listOfArgument.indexOf(key.substring(1))) {
		case 0:// 'h':// host
			senderObject.setHost(value);
			break;
		case 1:// 'p':// port
			senderObject.setPort(Integer.parseInt(value));
			break;
		case 2:// 'c':// command
			cnc.setCommand(value);
			break;
		case 3:// 't':// timeout
			senderObject.setTimeout(Integer.parseInt(value));
			cnc.setExpireIn(senderObject.getTimeout() * 1000);
			break;
		case 4:// 'o':// polling
			senderObject.setPollTiming(Integer.parseInt(value));
			break;
		case 5:// 'a':// argument
		{
			StringTokenizer st = new StringTokenizer(value, ",");
			while (st.hasMoreTokens()) {
				senderObject.addSubCommand(st.nextToken());
			}
			// cnc.setSubArgument(value);
		}
			break;
		case 6:// 'r':// parameter argument
				// senderObject.set
			cnc.setParameterArgument(value);
			cnc.setUseCustomArgument(true);
			break;
		case 7: // remotereportlocation
			cft.setRemotePathToCopy(value);
			break;
		case 8: // localreportlocation
			cft.setLocalPath(value);
			File f = new File(value);
			if (f.exists()) {
				if (f.isDirectory() == false) {
					f.delete();
					f.mkdirs();
				}
			} else {
				f.mkdirs();
			}
			break;
		case -1:
			break;
		default:
			break;
		}

	}
}
