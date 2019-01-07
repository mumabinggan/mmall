package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileUploadService;
import com.mmall.utils.JHFTPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileUploadService")
public class FileUploadServiceImpl implements IFileUploadService {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

	public String upload(MultipartFile file, String path) {
		boolean uploadSuccess = false;
		String fileName = file.getOriginalFilename();
		String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
		String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
		logger.info("开始上传文件，上传文件名:{}, 上传路径:{}, 新文件名{}", fileName, fileExtensionName, uploadFileName);

		File fileDir = new File(path);
		if (!fileDir.exists()) {
			fileDir.setWritable(true);
			fileDir.mkdirs();
		}
		File targetFile = new File(path, uploadFileName);

		try {
			//开始上传文件
			file.transferTo(targetFile);
			//上传成功

			//上传TargetFile到FTP服务器
			JHFTPUtils.uploadFiles(Lists.newArrayList(targetFile));

			//删除upload里面的文件
			targetFile.delete();

			uploadSuccess = true;

		} catch (IOException e) {
			logger.error("上传异常", e);
			uploadSuccess = false;
		}

		return uploadSuccess ? targetFile.getName() : null;
	}
}
