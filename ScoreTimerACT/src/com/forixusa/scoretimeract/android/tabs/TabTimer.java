package com.forixusa.scoretimeract.android.tabs;

import android.os.Bundle;
import android.util.Log;

import com.forixusa.scoretimeract.android.view.TimerView;

public class TabTimer extends ActivityGroupBase {
	private static final String TAG = TabTimer.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		// Intent intentTimer = new Intent(this, TimerActivity.class);
		// startChildActivity("TimerActivity", intentTimer);

		final TimerView view = new TimerView(this);
		setContentView(view);
	}

}
