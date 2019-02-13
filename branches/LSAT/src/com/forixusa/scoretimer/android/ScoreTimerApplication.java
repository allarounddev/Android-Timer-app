package com.forixusa.scoretimer.android;

import com.forixusa.android.activities.BaseApplication;

public class ScoreTimerApplication extends BaseApplication {

	// public static boolean sIsBiological = true;
	// public static boolean sIsVerbal = false;
	// public static boolean sIsPhysical = false;
	private static ScoreTimerApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static int getHeightScreen() {
		final int height = mInstance.getResources().getDisplayMetrics().heightPixels;
		return height;
	}

	public static int getWidthScreen() {
		final int width = mInstance.getResources().getDisplayMetrics().widthPixels;
		return width;
	}
}
