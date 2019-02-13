package com.forixusa.scoretimer.android.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.forixusa.scoretimer.android.activities.InstructionActivity;

public class TabInstruction extends TabActivityGroup {

	private static final String TAG = TabInstruction.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		Intent intentInstruction = new Intent(this, InstructionActivity.class);
		startChildActivity("InstructionActivity", intentInstruction);
	}

}
