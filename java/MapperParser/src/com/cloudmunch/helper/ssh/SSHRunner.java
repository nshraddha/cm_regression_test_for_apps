package com.cloudmunch.helper.ssh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class SSHRunner {

	String hostname;
	int port;
	String username;
	String password;
	String keyfile;

	public static final Logger LOGGER = Logger.getLogger(SSHRunner.class.getName());

	public SSHRunner() {

	}

	public SSHRunner(String hostname, String port, String username, String password) {
		this.hostname = hostname;
		try {
			this.port = Integer.parseInt(port);
		} catch (Exception e) {
			System.out.println("Exception while reading port from configuration, hence Using Port 22");
			this.port = 22;
		}
		this.username = username;
		this.password = password;
	}

	public SSHRunner(String hostname, String port, String username, String passphrase, String keyfile) {
		this(hostname, port, username, passphrase);
		this.keyfile = keyfile;
	}

	public void testConnection(PrintStream logger) throws JSchException, IOException {
		Session session = createSession(logger);
		closeSession(logger, session, null);
	}

	private Session createSession(PrintStream logger) throws JSchException {
		JSch jsch = new JSch();

		Session session = jsch.getSession(username, hostname, port);
		if (this.keyfile != null && this.keyfile.length() > 0) {
			jsch.addIdentity(this.keyfile, this.password);
		} else {
			session.setPassword(password);
		}

		UserInfo ui = new SSHUserInfo(password);
		session.setUserInfo(ui);

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();

		return session;
	}

	private ChannelExec createChannel(PrintStream logger, Session session) throws JSchException {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		return channel;
	}

	private void closeSession(PrintStream logger, Session session, Channel channel) {
		if (channel != null) {
			channel.disconnect();
			channel = null;
		}
		if (session != null) {
			session.disconnect();
			session = null;
		}
	}

	public Channel executeCommandThroughShell(PrintStream logger) {
		Session session = null;
		Channel channel = null;
		try {
			session = createSession(logger);
			channel = session.openChannel("shell");
			// PrintStream shellStream = new
			// PrintStream(channel.getOutputStream());
			channel.connect(3 * 1000);
			int failTimingsInSec = 10;
			while (channel.isConnected() == false) {
				if (failTimingsInSec-- > 0) {
					sleep(500);
				} else {
					break;
				}
			}
			if(channel.isConnected() == false){
				session.disconnect();
				return null;
			}
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return channel;
	}

	public int executeCommandThroughShell2(PrintStream logger, List<String> command, ByteArrayOutputStream baos) {
		Session session = null;
		Channel channel = null;
		int status = -1;
		try {
			session = createSession(logger);
			channel = session.openChannel("shell");
			{
				// StringInputStream inputStream = new
				// StringInputStream(command);
				// channel.setInputStream(shellStream, false);
				// channel.setOutputStream(System.out, false);

				// channel.setOutputStream(new ByteArrayOutputStream(), false);
				PrintStream shellStream = new PrintStream(channel.getOutputStream());
				channel.connect();
				while (channel.isConnected() == false) {
					sleep(500);
				}
				channel.setOutputStream(baos, false);
				for (int i = 0; i < command.size(); i++) {
					shellStream.println(command.get(i));
					shellStream.flush();
				}
				sleep(1000);
				channel.disconnect();
				if (channel.isClosed()) {
					status = channel.getExitStatus();
					logger.println("[SSH] " + "exit-status: " + status);
					System.out.println("[SSH] " + "exit-status: " + status);
				}
			}
			closeSession(logger, session, channel);
		} catch (JSchException e) {
			logger.println("[SSH] Exception:" + e.getMessage());
			e.printStackTrace();
			if (channel != null && channel.isConnected()) {
				channel.disconnect();
			}
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
			session = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}

	private void sleep(int millisec) {
		try {
			Thread.sleep(millisec);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	public int executeCommand(PrintStream logger, String command, PrintStream logFileStream) {
		Session session = null;
		ChannelExec channelExec = null;
		int status = -1;
		try {
			session = createSession(logger);
			channelExec = createChannel(logger, session);
			channelExec.setCommand(command);
			channelExec.setOutputStream(logger);
			channelExec.setInputStream(null);
			InputStream in = channelExec.getInputStream();
			channelExec.connect();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					logger.println(new String(tmp, 0, i));
//					System.out.println(new String(tmp, 0, i));
					String line = Charset.defaultCharset().decode(ByteBuffer.wrap(tmp, 0, i)).toString();
					if (logFileStream != null) {
						logFileStream.append(line);
						logFileStream.append("\n");
					}
				}
				if (channelExec.isClosed()) {
					status = channelExec.getExitStatus();
					logger.println("[SSH] " + "exit-status: " + status);
//					System.out.println("[SSH] " + "exit-status: " + status);
					break;
				}
				sleep(1000);
			}
			closeSession(logger, session, channelExec);
		} catch (JSchException e) {
			logger.println("[SSH] Exception:" + e.getMessage());
			e.printStackTrace();
			if (channelExec != null && channelExec.isConnected()) {
				channelExec.disconnect();
			}
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
			session = null;
		} catch (IOException e) {
			e.printStackTrace(logger);
		}
		return status;
	}

	public void close(Channel c) {
		try {
			c.disconnect();
			c.getSession().disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
		}

	}
}
