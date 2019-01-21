package com.mmall.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class JHDateTimeUtil {

	private static final String defaultFormat = "yyyy-MM-dd HH:mm:ss";

	public static Date strToDate(String dateTimeStr, String formatStr) {
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
		try {
			date = dateFormat.parse(dateTimeStr);
		} catch (ParseException e) {
			log.error("字符串转Date异常", e);
		}
		return date;
	}

	public static Date strToDate(String dateTimeStr) {
		return strToDate(dateTimeStr, defaultFormat);
	}

	public static String dateToStr(Date date, String formatStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
		return dateFormat.format(date);
	}

	public static String dateToStr(Date date) {
		return dateToStr(date, defaultFormat);
	}
}
