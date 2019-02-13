package com.forixusa.scoretimer.android.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.forixusa.scoretimer.android.activities.HomeActivity;
import com.forixusa.scoretimer.android.activities.InstructionActivity;

public class TabHome extends TabActivityGroup {

	private static final String TAG = TabHome.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		Intent intentInstruction = new Intent(this, HomeActivity.class);
		startChildActivity("HomeActivity", intentInstruction);
	}

}
