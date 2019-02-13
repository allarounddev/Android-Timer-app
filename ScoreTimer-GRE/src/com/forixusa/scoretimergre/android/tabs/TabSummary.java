package com.forixusa.scoretimergre.android.tabs;

import android.os.Bundle;
import android.util.Log;

import com.forixusa.scoretimergre.android.view.SummaryView;

public class TabSummary extends ActivityGroupBase {
	private static final String TAG = TabSummary.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		// Intent intentSummary = new Intent(this, SummaryActivity.class);
		// startChildActivity("SummaryActivity", intentSummary);

		final SummaryView view = new SummaryView(this);
		setContentView(view);
	}

}
