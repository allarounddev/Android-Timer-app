package com.forixusa.android.activities;

import com.forixusa.android.twitter.TwitterUtil;
import com.forixusa.android.twitter.TwitterUtil.TwitterStore;

import android.app.Application;
import android.graphics.Typeface;


public class BaseApplication extends Application {
	public static Typeface helveticaTypeface;

	public static TwitterStore sTwitterStore;
	public static TwitterUtil sTwitterUtil;
	public static int sTwitterStatus = -1;
	
	@Override
	public void onCreate() {
		super.onCreate();

		helveticaTypeface = Typeface.createFromAsset(this.getAssets(), "Helvetica.ttf");
	}

}
