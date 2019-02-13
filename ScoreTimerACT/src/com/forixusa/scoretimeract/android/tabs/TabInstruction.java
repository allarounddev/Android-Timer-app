package com.forixusa.scoretimeract.android.tabs;

import android.os.Bundle;
import android.util.Log;

import com.forixusa.scoretimeract.android.view.InstructionView;

public class TabInstruction extends ActivityGroupBase {

	private static final String TAG = TabInstruction.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		// Intent intentInstruction = new Intent(this,
		// InstructionActivity.class);
		// startChildActivity("InstructionActivity", intentInstruction);

		final InstructionView view = new InstructionView(this);
		setContentView(view);
	}

}
