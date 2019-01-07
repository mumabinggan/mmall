package com.mmall.vo;

public class FileUploadVO {

	private String url;
	private String uri;

	public FileUploadVO(String url, String uri) {
		this.url = url;
		this.uri = uri;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
