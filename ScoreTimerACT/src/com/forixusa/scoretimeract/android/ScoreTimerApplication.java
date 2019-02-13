package com.forixusa.scoretimeract.android;

import com.forixusa.android.activities.BaseApplication;

public class ScoreTimerApplication extends BaseApplication {

	public static final int ACT_ENGLISH = 0;
	public static final int ACT_MATH = 1;
	public static final int ACT_READING = 2;
	public static final int ACT_SCIENCE = 3;
	public static boolean sNeedToRefresh;
	public static boolean sIsFromTimer;

	public static int sTimerTestOption;
	public static int sSummaryTestOption;
	// public static boolean sIsFirstTime;

	private static ScoreTimerApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		sNeedToRefresh = false;
		sIsFromTimer = false;
		sTimerTestOption = ScoreTimerApplication.ACT_ENGLISH;
		sSummaryTestOption = ScoreTimerApplication.ACT_ENGLISH;
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
