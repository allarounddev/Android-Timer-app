package com.forixusa.scoretimergre.android.tabs;

import android.os.Bundle;
import android.util.Log;

import com.forixusa.scoretimergre.android.view.AboutUsView;

public class TabAboutUs extends ActivityGroupBase {
	private static final String TAG = TabAboutUs.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");

		// Intent intentAboutUs = new Intent(this, AboutUsActivity.class);
		// startChildActivity("AboutUsActivity", intentAboutUs);

		final AboutUsView view = new AboutUsView(this);
		setContentView(view);
	}

}
