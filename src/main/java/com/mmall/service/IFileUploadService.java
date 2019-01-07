package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadService {

	String upload(MultipartFile file, String path);
}
