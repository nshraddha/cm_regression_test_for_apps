package com.cloudbox.tcp.jenkins;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class JenkinsTCPSenderObject {

	private String targetCommandToRun = "";// cbRemote:host:port:command:argument:timeout:polltiming
	// command:Clean
	// command:ExecuteCommand
	// command::Check (Using new API)
	// command:Zip

	private boolean validCommand = false;
	private String host = "";
	private List<String> subCommandList = new ArrayList<String>();
	private int port = -1;
	private int timeout = -1;
	private int pollTiming = -1;

	public JenkinsTCPSenderObject(String targetCommandToRun) {
		this.targetCommandToRun = targetCommandToRun;
		parseCommand();
	}

	public JenkinsTCPSenderObject() {
	}

	public int getNumberOfSubCommands(){
		return subCommandList.size();
	}
	
	public String getSubCommandAtIndex(int index){
		return subCommandList.get(index);
	}
	
	public void addSubCommand(String subCommand){
		subCommandList.add(subCommand);
	}
	
	public List<String> getSubCommandList() {
		return subCommandList;
	}

	public void setSubCommandList(List<String> subCommandList) {
		this.subCommandList = subCommandList;
	}

	private void parseCommand() {
		validCommand = false;
		StringTokenizer st = new StringTokenizer(targetCommandToRun, ":");
		if (st.hasMoreTokens()) {
			host = st.nextToken();
			if (st.hasMoreTokens()) {
				port = Integer.parseInt(st.nextToken());
				if (st.hasMoreTokens()) {
					timeout = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens()) {
						pollTiming = Integer.parseInt(st.nextToken());
						validateCommand();
					}
				}
			}
		}
	}

	public String getTargetCommandToRun() {
		return targetCommandToRun;
	}

	public void setTargetCommandToRun(String targetCommandToRun) {
		this.targetCommandToRun = targetCommandToRun;
		parseCommand();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		validateCommand();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
		validateCommand();
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
		validateCommand();
	}

	public boolean isValidCommand() {
		return validCommand;
	}

	public int getPollTiming() {
		return pollTiming;
	}

	public void setPollTiming(int pollTiming) {
		this.pollTiming = pollTiming;
	}

	public boolean validateCommand() {
		// initiatedCommand = "", host="", port="", command="", argument = "",
		// timeout=""
		if (host.length() > 0 && port > 0 && timeout > 0) {
			validCommand = true;
		} else {
			validCommand = false;
		}
		return validCommand;
	}

	public String toString() {
		return host + ":" + port + ":" + timeout + ":" + pollTiming;
	}

}
