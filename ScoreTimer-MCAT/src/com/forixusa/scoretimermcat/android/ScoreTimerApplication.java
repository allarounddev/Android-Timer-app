package com.forixusa.scoretimermcat.android;

import com.forixusa.android.activities.BaseApplication;

public class ScoreTimerApplication extends BaseApplication {

	public static final int BIOLOGICAL_SCIENCES = 0;
	public static final int PHYSICAL_SCIENCES = 1;
	public static final int VERBAL_REASONING = 2;
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
		sTimerTestOption = ScoreTimerApplication.BIOLOGICAL_SCIENCES;
		sSummaryTestOption = ScoreTimerApplication.BIOLOGICAL_SCIENCES;
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
