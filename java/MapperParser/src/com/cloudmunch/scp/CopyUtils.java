package com.cloudmunch.scp;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jscape.inet.scp.Scp;
import com.jscape.inet.scp.ScpException;
import com.jscape.inet.ssh.util.SshParameters;

public class CopyUtils {

	public static final int FILECOPY_SUCCESS = 0;
	public static final int SOURCENOTEXIST = 1;
	public static final int FILECOPY_FAILED = 0xFF;

	public static int doLocalToLocalCopy(String sourcePath, String destinationPath, String baseFolder) {
		int exitCode = 0;
		File sourceFile = new File(baseFolder, sourcePath);
		File destinationFile = new File(baseFolder, destinationPath);
		if (sourceFile.exists()) {
			if (sourceFile.isDirectory()) {
				// copy dir
				System.out.println("Directory Copy");
				try {
					FileUtils.copyDirectoryToDirectory(sourceFile, destinationFile);
					exitCode = FILECOPY_SUCCESS;
				} catch (IOException e) {
					System.out.println(e.getLocalizedMessage());
					exitCode = FILECOPY_FAILED;
				}
			} else {
				// copy file
				System.out.println("File Copy");
				try {
					FileUtils.copyFileToDirectory(sourceFile, destinationFile);
					exitCode = FILECOPY_SUCCESS;
				} catch (IOException e) {
					System.out.println(e.getLocalizedMessage());
					exitCode = FILECOPY_FAILED;
				}
			}
		} else {
			System.out.println("Source Does not Exists");
			exitCode = SOURCENOTEXIST;
		}
		return exitCode;
	}

	public static void main(String[] args) throws JSONException {
		JSONObject remoteServerDetails = new JSONObject();
		remoteServerDetails.put("dnsName", "cloudboxonline.cloudmunch.com");
		remoteServerDetails.put("loginUser", "ec2-user");
		remoteServerDetails.put("privateKeyLoc", "E:/Downloads/dcloud.pem");

		{
			String fromFolder = "E:/temp/CBDemo/";
			String fromItem = "certificate";
			String to = "/var/cloudbox/temp/";

			System.out.println("Starting to copy from Local (" + fromFolder + fromItem + ") to remote (" + remoteServerDetails.getString("dnsName") + ":" + to + ")");
			copyLocalToRemote(remoteServerDetails, to, fromFolder, fromItem);
			System.out.println("Finished Copying");
		}

		{
			String from = "/var/cloudbox/temp/certificate";
			String toFolder = "E:/temp/CBDemoDupe1/";
			String toItem = "certificate";

			System.out.println("Starting to copy from Remote (" + remoteServerDetails.getString("dnsName") + ":" + from + ") to local (" + toFolder + toItem + ")");
			copyRemoteToLocal(remoteServerDetails, from, toFolder, toItem);
			System.out.println("Finished Copying");
		}
	}

	public static int copyRemoteToLocal(JSONObject remoteServerDetails, String remoteSourcePath, String localBaseDir, String localDestinationPath) {
		int exitCode = 0;
		Scp scp = null;
		boolean isSelfDir = localDestinationPath.equalsIgnoreCase(".");
		try {
			SshParameters params = new SshParameters(remoteServerDetails.getString("dnsName"), remoteServerDetails.getString("loginUser"), "", new File(remoteServerDetails.getString("privateKeyLoc")));

			scp = new Scp(params);
			scp.connect();

			File localFile = new File(localBaseDir, localDestinationPath);
			if (localFile.exists() && !isSelfDir) {
				System.out.println("Sorry local path already exists (" + localDestinationPath + "), cant download to same path");
				exitCode = 3;
			} else {
				scp.setLocalDir(localFile.getParentFile());
				String remoteDir = "";
				String remoteFile = "";
				if (remoteSourcePath.endsWith(File.separator)) {
					// Directory
					remoteDir = FilenameUtils.getFullPath(FilenameUtils.getFullPathNoEndSeparator(remoteSourcePath));
					remoteFile = FilenameUtils.getName(FilenameUtils.getFullPathNoEndSeparator(remoteSourcePath));
				} else {
					// File
					remoteDir = FilenameUtils.getFullPath(remoteSourcePath);
					// String remoteFile =
					// FilenameUtils.getName(FilenameUtils.getFullPathNoEndSeparator(remoteSourcePath));
					remoteFile = FilenameUtils.getName(remoteSourcePath);
				}
				try {
					scp.download(remoteDir, remoteFile);
				} catch (IOException e) {
					String s = e.getMessage();
					if (s.indexOf("not a regular file") != -1) {
						if (isSelfDir) {
							File removeableFile = new File(localFile.getParentFile(), remoteFile);
							if (removeableFile.isFile()) {
								removeableFile.delete();
							} else {
								System.out.println("Local Destination already exists (" + remoteFile + ")");
							}
						} else {
							localFile.delete();
						}
						scp.downloadDir(remoteDir, remoteFile);
					} else {
						e.printStackTrace();
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			exitCode = 4;
		} catch (ScpException e) {
			e.printStackTrace();
			exitCode = 4;
		} catch (IOException e) {
			e.printStackTrace();
			exitCode = 4;
		}

		if (scp != null) {
			scp.disconnect();
		}

		return exitCode;
	}

	public static int copyLocalToRemote(JSONObject remoteServerDetails, String remoteDestinationPath, String localBaseDir, String localSourcePath) {
		int exitCode = 0;
		Scp scp = null;
		try {
			SshParameters params = new SshParameters(remoteServerDetails.getString("dnsName"), remoteServerDetails.getString("loginUser"), "", new File(remoteServerDetails.getString("privateKeyLoc")));

			scp = new Scp(params);
			scp.connect();

			File localFile = new File(localBaseDir, localSourcePath);
			if (localFile.exists()) {
				if (localFile.isDirectory()) {
					{
						String nSource = localFile.getName();
						String nDestination = FilenameUtils.getName(FilenameUtils.getFullPathNoEndSeparator(remoteDestinationPath));
						if (nSource.equalsIgnoreCase(nDestination)) {
							remoteDestinationPath = FilenameUtils.getFullPath(FilenameUtils.getFullPathNoEndSeparator(remoteDestinationPath));
						}
					}
					scp.uploadDir(localFile, remoteDestinationPath);
				} else {
					{
						String nSource = localFile.getName();
						String nDestination = FilenameUtils.getName(remoteDestinationPath);
						if (nSource.equalsIgnoreCase(nDestination)) {
							remoteDestinationPath = FilenameUtils.getFullPath(remoteDestinationPath);
						}
					}
					scp.upload(localFile, remoteDestinationPath);
				}
			} else {
				System.out.println("Sorry local path specified to be uploaded does not exists (" + localSourcePath + ")");
				exitCode = 3;
			}

		} catch (JSONException e) {
			e.printStackTrace();
			exitCode = 4;
		} catch (ScpException e) {
			e.printStackTrace();
			exitCode = 4;
		} catch (IOException e) {
			e.printStackTrace();
			exitCode = 4;
		}
		if (scp != null) {
			scp.disconnect();
		}
		return exitCode;
	}

	public static int copyRemoteToRemote(JSONObject serverSource, String sourcePath, JSONObject serverDestination, String destinationPath, String localTemp) {
		int exitCode = 0;
		String localTempPath = findTempDirectory(localTemp);
		System.out.println("Copying the resource from SourceServer to local Server started");
		exitCode = copyRemoteToLocal(serverSource, sourcePath, localTempPath, ".");
		if (exitCode == 0) {
			System.out.println("Copying the resource from SourceServer to local Server completed");
			System.out.println("Copying the resource to the destination server started");
			exitCode = copyLocalToRemote(serverDestination, destinationPath, localTempPath, FilenameUtils.getName(sourcePath));
		} else {
			System.out.println("Copying the resource from SourceServer to local Server failed");
			return exitCode;
		}

		if (exitCode == 0) {
			System.out.println("Copying the resource to the destination server is complete");
		} else {
			System.out.println("Failed to copy the resource to the destination server.");
			exitCode = 3;
		}

		System.out.println("Performing Clean Up");
		FileUtils.deleteQuietly(new File(localTempPath));
		return exitCode;
	}

	private static String findTempDirectory(String localTemp) {
		File lt = null;
		int counter = 0;
		String subFolderName = "scpDirCopyRemote";
		do {
			counter++;
			lt = new File(localTemp, subFolderName + counter);
		} while (lt.exists());
		lt.mkdirs();
		return lt.getAbsolutePath();
		// return subFolderName + counter;
	}
}
