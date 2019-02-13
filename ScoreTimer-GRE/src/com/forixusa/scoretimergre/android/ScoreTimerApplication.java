package com.forixusa.scoretimergre.android;

import com.forixusa.android.activities.BaseApplication;

public class ScoreTimerApplication extends BaseApplication {

	public static final int GRE_MATH = 0;
	public static final int GRE_VERBAL = 1;

	public static boolean sNeedToRefresh;
	public static boolean sIsFromTimer;

	public static int sTimerTestOption;
	public static int sSummaryTestOption;

	private static ScoreTimerApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		sNeedToRefresh = false;
		sIsFromTimer = false;
		sTimerTestOption = ScoreTimerApplication.GRE_MATH;
		sSummaryTestOption = ScoreTimerApplication.GRE_MATH;
		// sIsFirstTime = true;

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
