package com.forixusa.scoretimerdat.android.tabs;

import android.os.Bundle;
import android.util.Log;

import com.forixusa.scoretimerdat.android.view.HomeView;

public class TabHome extends ActivityGroupBase {

	private static final String TAG = TabHome.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		// Intent intentInstruction = new Intent(this, HomeActivity.class);
		// startChildActivity("HomeActivity", intentInstruction);

		final HomeView view = new HomeView(this);
		setContentView(view);

		// final TimerView view = new TimerView(this);
		// setContentView(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
	}

}
