package com.cloudmunch.helper.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class SSHShellParser {

	public static Map<String, List<String>> parseShellOutput(List<String> command, InputStream output) throws IOException {
		Map<String, List<String>> parseOutput = new HashMap<String, List<String>>();
		List<String> l = IOUtils.readLines(output);
		String prompt = l.get(0).trim();
		{
			List<String> newL = new ArrayList<String>();
			for(int i=0; i<l.size(); i++){
				String s = StringUtils.remove(l.get(i), prompt).trim();
				if(s.length() > 0){
					newL.add(s);
				}
			}
			l = newL;
		}
		int outputIndex = 0;
		System.out.println(l);

		for (int i = 0; i < command.size(); i++) {
			String currentCommand = command.get(i);
			String currentOutput = l.get(outputIndex++);
			if(currentOutput.indexOf(currentCommand) > -1){
				List<String> listOfOutput = new ArrayList<String>();
				parseOutput.put(currentCommand, listOfOutput);
				if(command.size()-1 == i){ //Special Case for last Command
					for(int j=outputIndex; j<l.size(); j++){
						listOfOutput.add(l.get(j));
					}
				} else {
					String nextCommand = command.get(i+1);
					while(l.get(outputIndex++).indexOf(nextCommand) > -1){
						listOfOutput.add(l.get(outputIndex-1));						
					}
				}
			}
		}

		return parseOutput;
	}
}
