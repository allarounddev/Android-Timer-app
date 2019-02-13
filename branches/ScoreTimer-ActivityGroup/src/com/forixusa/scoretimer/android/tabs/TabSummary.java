package com.forixusa.scoretimer.android.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.forixusa.scoretimer.android.activities.SummaryActivity;

public class TabSummary extends TabActivityGroup {
	private static final String TAG = TabSummary.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		Intent intentSummary = new Intent(this, SummaryActivity.class);
		startChildActivity("SummaryActivity", intentSummary);
	}

}
