package com.mmall.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class JHFTPUtils {

	private static final Logger logger = LoggerFactory.getLogger(JHFTPUtils.class);

	private static String ftpIp = JHPropertiesUtil.getProperty("ftp.server.ip");
	private static String ftpUser = JHPropertiesUtil.getProperty("ftp.user");
	private static String ftpPass = JHPropertiesUtil.getProperty("ftp.pass");

	private String ip;
	private int port;
	private String user;
	private String pwd;
	private FTPClient ftpClient;

	public JHFTPUtils(String ip, int port, String user, String pwd) {
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.pwd = pwd;
	}

	public static boolean uploadFiles(List<File> fileList) {
		JHFTPUtils ftpUtils = new JHFTPUtils(ftpIp, 21, ftpUser, ftpPass);
		logger.info("开始连接ftp服务器");
		boolean result = ftpUtils.uploadFiles("img", fileList);
		logger.info("开始连接ftp服务器，结束上传，上传结果:{}");
		return result;
	}

	private boolean uploadFiles(String remotePath, List<File> fileList) {
		boolean upload = true;
		FileInputStream fis = null;
		if (connectServer(this.getIp(), this.getPort(), this.getUser(), this.getPwd())) {
			try {
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				for (File item : fileList) {
					fis = new FileInputStream(item);
					ftpClient.storeFile(item.getName(), fis);
				}
			} catch (IOException e) {
				logger.error("上传文件异常", e);
				upload = false;
			} finally {
				try {
					fis.close();
					ftpClient.disconnect();
				} catch (IOException e) {
					logger.error("关闭流或者关闭ftpClient异常", e);
				}
			}
		}
		return upload;
	}

	private boolean connectServer(String ip, int port, String user, String pwd) {
		boolean isSuccess = false;
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(ip);
			isSuccess = ftpClient.login(user, pwd);
		} catch (IOException e) {
			logger.error("连接FTP服务器异常", e);
		}
		return isSuccess;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
}
