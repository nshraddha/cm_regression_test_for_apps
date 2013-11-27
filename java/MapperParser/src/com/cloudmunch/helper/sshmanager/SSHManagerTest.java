package com.cloudmunch.helper.sshmanager;

public class SSHManagerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("sendCommand");

		/**
		 * YOU MUST CHANGE THE FOLLOWING FILE_NAME: A FILE IN THE DIRECTORY
		 * USER: LOGIN USER NAME PASSWORD: PASSWORD FOR THAT USER HOST: IP
		 * ADDRESS OF THE SSH SERVER
		 **/
		String command = "ls";
		String userName = "ec2-user";
		String password = "";
		String connectionIP = "cloudboxstaging3.cloudmunch.com";
		String keyFile = "E:/Downloads/dcloud.pem";
		String knownHostFile = "C:/Users/tumstech/.ssh/known_hosts";
		SSHManager instance = new SSHManager(userName, password, connectionIP, knownHostFile, keyFile);
		String errorMessage = instance.connect();

		if (errorMessage != null) {
			System.out.println(errorMessage);
		}

		String expResult = "FILE_NAME\n";
		// call sendCommand for each command and the output
		// (without prompts) is returned
		String result1 = instance.sendCommand("ls");
		String result2 = instance.sendCommand("echo $ANT_HOME");
		String result3 = instance.sendCommand("ant -version");
		String result4 = instance.sendCommand("/var/cloudbox/ant1.8/bin/ant -version");
		// close only after all commands are sent
		instance.close();
		System.out.println(result1);
		System.out.println(result2);
		System.out.println(result3);
		System.out.println(result4);
	}

}
