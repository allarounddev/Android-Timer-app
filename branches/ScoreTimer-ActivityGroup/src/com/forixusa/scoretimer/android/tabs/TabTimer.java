package com.forixusa.scoretimer.android.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.forixusa.scoretimer.android.activities.TimerActivity;

public class TabTimer extends TabActivityGroup {
	private static final String TAG = TabTimer.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		Intent intentTimer = new Intent(this, TimerActivity.class);
		startChildActivity("TimerActivity", intentTimer);
	}

}
