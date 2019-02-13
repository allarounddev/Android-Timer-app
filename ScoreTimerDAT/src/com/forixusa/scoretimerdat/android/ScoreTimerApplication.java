package com.forixusa.scoretimerdat.android;

import com.forixusa.android.activities.BaseApplication;

public class ScoreTimerApplication extends BaseApplication {

	public static final int DAT_SCIENCE = 0;
	public static final int DAT_QR = 1;
	public static final int DAT_RC = 2;
	public static final int DAT_PAT = 3;
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
		sTimerTestOption = ScoreTimerApplication.DAT_SCIENCE;
		sSummaryTestOption = ScoreTimerApplication.DAT_SCIENCE;
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
