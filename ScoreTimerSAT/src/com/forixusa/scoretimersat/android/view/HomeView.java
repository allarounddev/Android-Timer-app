package com.forixusa.scoretimersat.android.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimersat.android.ScoreTimerApplication;
import com.forixusa.scoretimersat.android.activities.R;

public class HomeView extends ContentView implements OnClickListener {
	private static final String TAG = HomeView.class.getSimpleName();

	private Button mBtnMath;
	private Button mBtnReading;
	private Button mBtnWriting;

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

		mBtnMath = (Button) findViewById(R.id.btnMath);
		mBtnReading = (Button) findViewById(R.id.btnReading);
		mBtnWriting = (Button) findViewById(R.id.btnWriting);

	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnMath.setOnClickListener(this);
		mBtnReading.setOnClickListener(this);
		mBtnWriting.setOnClickListener(this);
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

		case R.id.btnMath:
			Log.i(TAG, "btnMath");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.SAT_MATH;
			final TimerView timerView0 = new TimerView(mContext);
			mActivity.setContentView(timerView0);
			break;
		case R.id.btnReading:
			Log.i(TAG, "btnReading");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.SAT_READING;
			final TimerView timerView1 = new TimerView(mContext);
			mActivity.setContentView(timerView1);
			break;
		case R.id.btnWriting:
			Log.i(TAG, "btnWriting");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.SAT_WRITING;
			final TimerView timerView2 = new TimerView(mContext);
			mActivity.setContentView(timerView2);
			break;
		}
	}

}
