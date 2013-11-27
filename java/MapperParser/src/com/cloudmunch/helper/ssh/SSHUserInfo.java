package com.cloudmunch.helper.ssh;

import com.jcraft.jsch.UserInfo;

public class SSHUserInfo implements UserInfo {

	String password;
	String passphrase;

	public SSHUserInfo(String password) {
		this.password = password;
		this.passphrase = password;

	}

	public String getPassphrase() {
		return passphrase;
	}

	public String getPassword() {
		return password;
	}

	public boolean promptPassphrase(String arg0) {
		return false;
	}

	public boolean promptPassword(String arg0) {
		return false;
	}

	public boolean promptYesNo(String arg0) {
		return false;
	}

	public void showMessage(String arg0) {
		System.out.println(arg0);
	}

}
