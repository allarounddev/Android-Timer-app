package com.forixusa.android.utils;

import java.text.DecimalFormat;

public class NumberHelper {

	public static double roundTwoDecimals(double d) {
		final DecimalFormat twoDForm = new DecimalFormat("###.##");
		return Double.valueOf(twoDForm.format(d));
	}
}
