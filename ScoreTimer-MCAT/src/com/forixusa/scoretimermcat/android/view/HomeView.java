package com.forixusa.scoretimermcat.android.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimermcat.android.ScoreTimerApplication;
import com.forixusa.scoretimermcat.android.activities.R;

public class HomeView extends ContentView implements OnClickListener {
	private static final String TAG = HomeView.class.getSimpleName();

	private Button mBtnBiological;
	private Button mBtnVerbal;
	private Button mBtnPhysical;

	public HomeView(Context context) {
		super(context);
		Log.i(TAG, "HomeView");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.home_activity, this, true);
		loadUIControls();
		listenerUIControls();

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);
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
	public void onResumeUI() {
		super.onResumeUI();
		Log.i(TAG, "onResumeUI");
	}

	@Override
	public void onClick(View v) {
		Log.i(TAG, "onClick");

		switch (v.getId()) {

		case R.id.btnBiological:
			Log.i(TAG, "btnBiological");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.BIOLOGICAL_SCIENCES;
			final TimerView timerView0 = new TimerView(mContext);
			mActivity.setContentView(timerView0);
			break;
		case R.id.btnPhysical:
			Log.i(TAG, "btnPhysical");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.PHYSICAL_SCIENCES;
			final TimerView timerView2 = new TimerView(mContext);
			mActivity.setContentView(timerView2);
			break;
		case R.id.btnVerbal:
			Log.i(TAG, "btnVerbal");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.VERBAL_REASONING;
			final TimerView timerView1 = new TimerView(mContext);
			mActivity.setContentView(timerView1);
			break;
		}
	}

}
