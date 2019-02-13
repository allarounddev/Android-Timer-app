package com.forixusa.scoretimer.android.activities;

import com.forixusa.scoretimer.android.ScoreTimerApplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends ScoreTimerBaseActivity implements OnClickListener {
	private static final String TAG = ScoreTimerActivity.class.getSimpleName();

	private Button mBtnBiological;
	private Button mBtnVerbal;
	private Button mBtnPhysical;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.home_activity);

		loadUIControls();
		listenerUIControls();
	}

	private void loadUIControls() {
		Log.i(TAG, "loadUIControls");
		
		mBtnBiological = (Button) findViewById(R.id.btnBiological);
		mBtnVerbal = (Button) findViewById(R.id.btnVerbal);
		mBtnPhysical = (Button) findViewById(R.id.btnPhysical);

	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnBiological.setOnClickListener(this);
		mBtnVerbal.setOnClickListener(this);
		mBtnPhysical.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.i(TAG, "onClick");
		switch (v.getId()) {

		case R.id.btnBiological:
			ScoreTimerApplication.sIsBiological = true;
			ScoreTimerApplication.sIsVerbal = false;
			ScoreTimerApplication.sIsPhysical = false;
			ScoreTimerActivity.getInstance().displayTabTimer();
			break;
		case R.id.btnVerbal:
			ScoreTimerApplication.sIsBiological = false;
			ScoreTimerApplication.sIsVerbal = true;
			ScoreTimerApplication.sIsPhysical = false;
			ScoreTimerActivity.getInstance().displayTabTimer();
			break;
		case R.id.btnPhysical:
			ScoreTimerApplication.sIsBiological = false;
			ScoreTimerApplication.sIsVerbal = false;
			ScoreTimerApplication.sIsPhysical = true;
			ScoreTimerActivity.getInstance().displayTabTimer();
			break;
		}
	}

}
