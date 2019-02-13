package com.forixusa.scoretimer.android.activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.forixusa.android.activities.BaseActivity;
import com.forixusa.scoretimer.android.tabs.TabActivityGroup;

public class ScoreTimerBaseActivity extends BaseActivity {

	private static final String TAG = ScoreTimerActivity.class.getSimpleName();

	protected void gotoShare() {
		final Intent intentShare = new Intent(getParent(), ShareActivity.class);
		final TabActivityGroup parentActivity = (TabActivityGroup) getParent();
		parentActivity.startChildActivity("ShareActivity", intentShare);
	}

	protected final OnClickListener mBtnShareListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i(TAG, "onClick " + " btnShare");
			gotoShare();
		}
	};
}
