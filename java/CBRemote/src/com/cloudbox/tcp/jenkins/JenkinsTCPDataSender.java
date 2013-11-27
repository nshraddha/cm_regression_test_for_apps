package com.cloudbox.tcp.jenkins;

import java.io.File;
import java.io.InputStream;
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
import com.cloudbox.utils.CloudboxWriter;

final public class JenkinsTCPDataSender {

	private JenkinsTCPDataSender() {

	}

	public static Object sendData(String host, int port, Object data) {

		try {
			final InetAddress add = InetAddress.getByName(host);
			final Socket soc = new Socket(add, port);
			final OutputStream out = soc.getOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(out);

			oos.writeObject(data);

			final InputStream is = soc.getInputStream();
			final ObjectInputStream ois = new ObjectInputStream(is);
			final Object o = ois.readObject();

			soc.close();
			out.close();
			oos.close();

			return o;
		} catch (Exception e) {
			CloudboxWriter.println(e);
		}
		return null;
	}

	public static Object sendAndReceiveFile(String host, int port, CloudboxFileTransfer data) {
		try {
			final InetAddress add = InetAddress.getByName(host);
			final Socket soc = new Socket(add, port);
			final OutputStream out = soc.getOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(out);

			oos.writeObject(data);

			final InputStream is = soc.getInputStream();
			final ObjectInputStream ois = new ObjectInputStream(is);
			final Object o = ois.readObject();

			if (o instanceof FileDefinition) {
				FileDefinition fd = (FileDefinition) o;
				if (fd.isValid()) {
					String destPath = data.getLocalPath() + File.separator + fd.getFromFile();
					FileTransferUtils.receiveData(ois, fd, destPath);
					if (fd.isCompressed()) {
						Expand e = new Expand();
						e.setProject(new Project());
						e.getProject().setName("CMExpand");
						File dest = new File(data.getLocalPath());
						dest.mkdirs();
						e.setDest(dest);
						e.setSrc(new File(data.getLocalPath() + File.separator + fd.getFromFile()));
						e.execute();
					}
				}
			}

			soc.close();
			out.close();
			oos.close();

			return o;
		} catch (Exception e) {
			CloudboxWriter.println(e);
		}
		return null;
	}
}
