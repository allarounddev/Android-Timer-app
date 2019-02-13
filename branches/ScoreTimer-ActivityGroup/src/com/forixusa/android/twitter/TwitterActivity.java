package com.forixusa.android.twitter;

import com.forixusa.android.twitter.TwitterUtil;
import com.forixusa.android.twitter.TwitterUtil.TwitterDelegate;
import com.forixusa.android.twitter.TwitterUtil.TwitterStore;
import com.forixusa.scoretimer.android.ScoreTimerApplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class TwitterActivity extends Activity implements TwitterDelegate {
	public static final String TAG = TwitterActivity.class.getSimpleName();
	
	// Pirq
	public static final String CONSUMER_KEY = "wARtlyHarj7769zBGcUw";
	public static final String CONSUMER_SECRET 
								= "4aHH7WZgbmE2D64SyEW9hXBQOyyMVg3CHrsyhhqI";
	
	private int countResume = 0;
	
	private void startTwitter() {
		ScoreTimerApplication.sTwitterUtil.doAuthenticateRequest();
	}
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		 Log.i(TAG, "onCreate");
		 super.onCreate(savedInstanceState);
		 
		 ScoreTimerApplication.sTwitterUtil = new TwitterUtil(this, this);
		 ScoreTimerApplication.sTwitterUtil.setSecretKeyForConsumerKey(CONSUMER_KEY, 
				 											CONSUMER_SECRET);
		 ScoreTimerApplication.sTwitterUtil.setSchemeAndHost("myapp", "mainactivity");
	     
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
		if (ScoreTimerApplication.sTwitterStore == null) {
			ScoreTimerApplication.sTwitterStore = new TwitterStore();
			ScoreTimerApplication.sTwitterStore.copyFrom(data);
		}
		
		Log.i(TAG, "complete oauth twitter");
		ScoreTimerApplication.sTwitterStatus  = 1;
		finish();
	}
	
	@Override
	public void authenticateFault() {
		ScoreTimerApplication.sTwitterStatus  = 2;
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
			ScoreTimerApplication.sTwitterUtil.doAuthenticatReceiveWithURI(uri);
		}
	}
}
