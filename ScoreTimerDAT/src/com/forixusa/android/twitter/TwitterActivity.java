package com.forixusa.android.twitter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.forixusa.android.activities.BaseApplication;
import com.forixusa.android.twitter.TwitterUtil.TwitterDelegate;
import com.forixusa.android.twitter.TwitterUtil.TwitterStore;

public class TwitterActivity extends Activity implements TwitterDelegate {
	public static final String TAG = TwitterActivity.class.getSimpleName();

	// // Pirq
	// public static final String CONSUMER_KEY = "wARtlyHarj7769zBGcUw";
	// public static final String CONSUMER_SECRET =
	// "4aHH7WZgbmE2D64SyEW9hXBQOyyMVg3CHrsyhhqI";

	public static final String CONSUMER_KEY = "c4rjiNbnGtCxzWUEscNP5w";
	public static final String CONSUMER_SECRET = "IGB6AQJ79fCqnJHYbPYZOeqAkv7uZ10Eo6ake386Zs";

	private int countResume = 0;

	private void startTwitter() {
		BaseApplication.sTwitterUtil.doAuthenticateRequest();
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		BaseApplication.sTwitterUtil = new TwitterUtil(this, this);
		BaseApplication.sTwitterUtil.setSecretKeyForConsumerKey(CONSUMER_KEY, CONSUMER_SECRET);
		BaseApplication.sTwitterUtil.setSchemeAndHost("myapp", "mainactivity_dat");

		startTwitter();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		countResume++;

		if (countResume == 2 && !mDoAuthenticatReceiveWithURI) {
			finish();
		}
	}

	@Override
	public void authenticateComplete(final TwitterStore data) {
		Log.i(TAG, "authenticateComplete");
		if (BaseApplication.sTwitterStore == null) {
			BaseApplication.sTwitterStore = new TwitterStore();
			BaseApplication.sTwitterStore.copyFrom(data);
		}

		Log.i(TAG, "complete oauth twitter");
		BaseApplication.sTwitterStatus = 1;
		finish();
	}

	@Override
	public void authenticateFault() {
		BaseApplication.sTwitterStatus = 2;
		finish();
	}

	private boolean mDoAuthenticatReceiveWithURI = false;

	@Override
	public void onNewIntent(final Intent intent) {
		super.onNewIntent(intent);

		final Uri uri = intent.getData();
		Log.i(TAG, "onNewIntent uri = >>>>> " + uri);
		if (uri != null && uri.getScheme().equals("myapp")) {
			mDoAuthenticatReceiveWithURI = true;
			BaseApplication.sTwitterUtil.doAuthenticatReceiveWithURI(uri);
		}
	}
}
