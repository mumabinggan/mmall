package com.mmall.utils;

import java.math.BigDecimal;

public class JHBigDecimalUtils {

	public static BigDecimal add(double value1, double value2) {
		BigDecimal big1 = BigDecimalWithDouble(value1);
		BigDecimal big2 = BigDecimalWithDouble(value2);
		return big1.add(big2);
	}

	public static BigDecimal sub(double value1, double value2) {
		BigDecimal big1 = BigDecimalWithDouble(value1);
		BigDecimal big2 = BigDecimalWithDouble(value2);
		return big1.subtract(big2);
	}

	public static BigDecimal mul(double value1, double value2) {
		BigDecimal big1 = BigDecimalWithDouble(value1);
		BigDecimal big2 = BigDecimalWithDouble(value2);
		return big1.multiply(big2);
	}

	public static BigDecimal div(double value1, double value2) {
		BigDecimal big1 = BigDecimalWithDouble(value1);
		BigDecimal big2 = BigDecimalWithDouble(value2);
		return big1.divide(big2, 2, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal BigDecimalWithDouble(double value) {
		return new BigDecimal(Double.toString(value));
	}
}
