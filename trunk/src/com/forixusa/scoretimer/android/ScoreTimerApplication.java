package com.forixusa.scoretimer.android;

import com.forixusa.android.activities.BaseApplication;

public class ScoreTimerApplication extends BaseApplication {

	// public static final int BIOLOGICAL_SCIENCES = 2; // 0
	// public static final int VERBAL_REASONING = 0; // 1
	// public static final int PHYSICAL_SCIENCES = 1; // 2

	public static final int LOGICAL_REASONING = 0; // 0
	public static final int LOGIC_GAME = 1; // 1
	public static final int READING_COMPREHENSION = 2; // 2

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
		sTimerTestOption = ScoreTimerApplication.LOGICAL_REASONING;
		sSummaryTestOption = ScoreTimerApplication.LOGICAL_REASONING;
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
