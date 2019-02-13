package com.forixusa.scoretimergre.android.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimergre.android.ScoreTimerApplication;
import com.forixusa.scoretimergre.android.activities.R;

public class HomeView extends ContentView implements OnClickListener {
	private static final String TAG = HomeView.class.getSimpleName();

	private Button mBtnGreMath;
	private Button mBtnGreVerbal;

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

		mBtnGreMath = (Button) findViewById(R.id.btnGreMath);
		mBtnGreVerbal = (Button) findViewById(R.id.btnGreVerbal);
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnGreMath.setOnClickListener(this);
		mBtnGreVerbal.setOnClickListener(this);
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

		case R.id.btnGreMath:
			Log.i(TAG, "btnGreMath");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.GRE_MATH;
			final TimerView timerView0 = new TimerView(mContext);
			mActivity.setContentView(timerView0);
			break;
		case R.id.btnGreVerbal:
			Log.i(TAG, "btnGreVerbal");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.GRE_VERBAL;
			final TimerView timerView1 = new TimerView(mContext);
			mActivity.setContentView(timerView1);
			break;
		}
	}

}
