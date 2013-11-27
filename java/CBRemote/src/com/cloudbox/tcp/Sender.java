package com.cloudbox.tcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

import com.cloudbox.tcp.ftp.CloudboxFileTransfer;
import com.cloudbox.tcp.ftp.FileDefinition;
import com.cloudbox.tcp.ftp.FileTransferUtils;
import com.cloudbox.tcp.objects.CloudboxExistingCommand;
import com.cloudbox.tcp.objects.CloudboxNewCommand;
import com.cloudbox.tcp.objects.ICloudboxCommand;
import com.cloudbox.utils.CloudboxWriter;

final class Sender {

	private Sender() {

	}

	public static String host = "";
	public static int port = 0;

	public static void main(String args[]) {
		Sender.host = args[0];
		Sender.port = Integer.parseInt(args[1]);
		String inputCommand = "", commandString = "";

		final InputStreamReader cin = new InputStreamReader(System.in);
		final BufferedReader br = new BufferedReader(cin);

		do {
			CloudboxWriter.println("1. Start a New Command.");
			CloudboxWriter.println("2. Check existing Command Status.");
			CloudboxWriter.println("3. Check existing Command Status.");
			CloudboxWriter.println("9. Exit.");

			try {
				inputCommand = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			switch (Integer.parseInt(inputCommand)) {
			case 1:
				CloudboxWriter.print("Input the Command : ");
				try {
					commandString = br.readLine();
					sendNewData(commandString);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				CloudboxWriter.print("Input the Reference ID : ");
				try {
					commandString = br.readLine();
					sendExistingData(Long.parseLong(commandString));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				CloudboxWriter.print("Input the File Path : ");
				try {
					commandString = br.readLine();
					sendAndReceiveFileData(commandString);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default: {
				break;
			}
			}

		} while (inputCommand.equalsIgnoreCase("9") == false);

		// String retValue = "";
		// retValue = sendNewData("host:123:RunTCLLauncher:ExecuteCommand");
		// CloudboxWriter.println(retValue);
		// // retValue = "12";
		// CloudboxExistingCommand cec = null;
		// do {
		// cec = sendExistingData(Long.parseLong(retValue));
		// CloudboxWriter.println("Command Status" + cec);
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// } while (cec.isRunning() == true);

	}

	private static boolean sendAndReceiveFileData(String filePath) {
		try {
			final InetAddress add = InetAddress.getByName(host);
			final Socket soc = new Socket(add, port);
			final OutputStream out = soc.getOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(out);
			final CloudboxFileTransfer cc = new CloudboxFileTransfer();
			cc.setRemotePathToCopy(filePath);
			// cc.setPathToCopy("E:/temp/dx.txt");
			oos.writeObject(cc);
			// oos.writeObject("host:123:RunTCLLauncher:CheckStatus");
			// oos.writeObject("exit");
			final InputStream is = soc.getInputStream();
			final ObjectInputStream ois = new ObjectInputStream(is);

			// BufferedReader input = new BufferedReader(new
			// InputStreamReader(soc.getInputStream()));
			FileDefinition fd = (FileDefinition) ois.readObject();
			CloudboxWriter.println(fd.toString());
			if (fd.isValid()) {
				FileTransferUtils.receiveData(ois, fd, "tempFileName");
				Expand e = new Expand();
				e.setProject(new Project());
				e.getProject().setName("CMExpand");
				File dest = new File(fd.getActualPath() + File.separator + "Reports");
				dest.mkdirs();
				e.setDest(dest);
				e.setSrc(new File(fd.getActualPath() + ".zip"));
				e.execute();
			}

			// CloudboxWriter.println(ois.readObject().toString());

			soc.close();
			out.close();
			oos.close();

			return true;
		} catch (Exception e) {
			CloudboxWriter.println(e);
		}
		return false;
	}

	private static CloudboxExistingCommand sendExistingData(long inputStringCommand) {
		try {
			final InetAddress add = InetAddress.getByName(host);
			final Socket soc = new Socket(add, port);
			final OutputStream out = soc.getOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(out);
			final ICloudboxCommand cc = new CloudboxExistingCommand(inputStringCommand);
			oos.writeObject(cc);
			// oos.writeObject("host:123:RunTCLLauncher:CheckStatus");
			// oos.writeObject("exit");
			final InputStream is = soc.getInputStream();
			final ObjectInputStream ois = new ObjectInputStream(is);

			// BufferedReader input = new BufferedReader(new
			// InputStreamReader(soc.getInputStream()));
			final CloudboxExistingCommand cec = (CloudboxExistingCommand) ois.readObject();
			CloudboxWriter.println("Valid Thread ID :" + cec.isThreadIDValid());
			CloudboxWriter.println("Running Status :" + cec.isRunning());

			soc.close();
			out.close();
			oos.close();

			return cec;
		} catch (Exception e) {
			CloudboxWriter.println(e);
		}

		return null;
	}

	private static String sendNewData(String inputStringCommand) {
		try {
			final InetAddress add = InetAddress.getByName(host);
			final Socket soc = new Socket(add, port);
			final OutputStream out = soc.getOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(out);
			final ICloudboxCommand cc = new CloudboxNewCommand(inputStringCommand, 15*60*1000);
			oos.writeObject(cc);
			// oos.writeObject("host:123:RunTCLLauncher:CheckStatus");
			// oos.writeObject("exit");
			final InputStream is = soc.getInputStream();
			final ObjectInputStream ois = new ObjectInputStream(is);

			// BufferedReader input = new BufferedReader(new
			// InputStreamReader(soc.getInputStream()));
			final String line = (String) ois.readObject();
			CloudboxWriter.println(line);

			soc.close();
			out.close();
			oos.close();

			return line;
		} catch (Exception e) {
			CloudboxWriter.println(e);
		}
		try {
			final InetAddress add = InetAddress.getByName(host);
			final Socket soc = new Socket(add, port);
			final OutputStream out = soc.getOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(out);
			final ICloudboxCommand cc = new CloudboxNewCommand(inputStringCommand, 15*60*1000);
			oos.writeObject(cc);
			// oos.writeObject("host:123:RunTCLLauncher:CheckStatus");
			// oos.writeObject("exit");
			final InputStream is = soc.getInputStream();
			final ObjectInputStream ois = new ObjectInputStream(is);

			// BufferedReader input = new BufferedReader(new
			// InputStreamReader(soc.getInputStream()));
			final String line = (String) ois.readObject();
			CloudboxWriter.println(line);

			soc.close();
			out.close();
			oos.close();

			return line;
		} catch (Exception e) {
			CloudboxWriter.println(e);
		}
		return "";
	}
}