package com.mmall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class JHResponse<T> implements Serializable {

	private int code;
	private String msg;
	private T data;

	private JHResponse(int code) {
		this(code, null, null);
	}

	private JHResponse(int code, String msg) {
		this(code, msg, null);
	}

	private JHResponse(int code, T data) {
		this(code, null, data);
	}

	private JHResponse(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	@JsonIgnore
	public Boolean isSuccess() {
		return this.code <= JHResponseCode.successMaxCode;
	}

	public static <T> JHResponse<T> createBySuccess() {
		return createBySuccess(JHResponseCode.Success.getCode(), null, null);
	}

	public static <T> JHResponse<T> createBySuccess(JHResponseCode code) {
		return createBySuccess(code, null);
	}

	public static <T> JHResponse<T> createBySuccess(JHResponseCode code, T data) {
		return createBySuccess(code.getCode(), code.getMsg(), data);
	}

	public static <T> JHResponse<T> createBySuccess(int code, String msg) {
		return createBySuccess(code, msg, null);
	}

	public static <T> JHResponse createBySuccess(int code, T data) {
		return createBySuccess(code, null, data);
	}

	public static <T> JHResponse createBySuccess(int code, String msg, T data) {
		return new JHResponse(code, msg, data);
	}

	public static <T> JHResponse createByError(int code) {
		return createByError(code, null, null);
	}

	public static <T> JHResponse<T> createByError(JHResponseCode code) {
		return createBySuccess(code.getCode(), code.getMsg(), null);
	}

	public static <T> JHResponse createByError(int code, String msg) {
		return createByError(code, msg, null);
	}

	public static <T> JHResponse createByError(int code, T data) {
		return createByError(code, null, data);
	}

	public static <T> JHResponse createByError(int code, String msg, T data) {
		return new JHResponse(code, msg, data);
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}
}
