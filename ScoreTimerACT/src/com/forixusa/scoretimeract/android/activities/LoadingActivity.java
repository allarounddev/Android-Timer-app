package com.forixusa.scoretimeract.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class LoadingActivity extends Activity {
	private static final String TAG = LoadingActivity.class.getSimpleName();

	protected boolean mActive = true;
	protected int mSplashTime = 3000; // time to display the splash screen in ms

	private PackageInfo getPackageInfo() {
		PackageInfo pi = null;
		try {
			pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (final PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_activity);
		Log.d(TAG, "onCreate");

		final Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (mActive && waited < mSplashTime) {
						sleep(100);
						if (mActive) {
							waited += 100;
						}
					}
				} catch (final InterruptedException e) {
					e.printStackTrace();
				} finally {

					finish();
					final Intent intent = new Intent(LoadingActivity.this, ScoreTimerActivity.class);
					startActivity(intent);
				}
			}
		};
		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "onTouchEvent");
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mActive = false;
		}
		return true;
	}
}