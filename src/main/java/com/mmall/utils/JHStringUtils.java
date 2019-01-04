package com.mmall.utils;

import org.apache.commons.lang3.StringUtils;

public class JHStringUtils {
	public static boolean isNotBlank(String string) {
		return StringUtils.isNotBlank(string);
	}

	public static boolean isBlank(String string) {
		return StringUtils.isBlank(string);
	}

	public static boolean equals(String str1, String str2) {
		return StringUtils.equals(str1, str2);
	}
}
