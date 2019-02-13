package com.forixusa.scoretimerdat.android.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimerdat.android.ScoreTimerApplication;
import com.forixusa.scoretimerdat.android.activities.R;

public class HomeView extends ContentView implements OnClickListener {
	private static final String TAG = HomeView.class.getSimpleName();

	private Button mBtnEnglish;
	private Button mBtnMath;
	private Button mBtnReading;
	private Button mBtnScience;

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

		mBtnEnglish = (Button) findViewById(R.id.btnEnglish);
		mBtnMath = (Button) findViewById(R.id.btnMath);
		mBtnReading = (Button) findViewById(R.id.btnReading);
		mBtnScience = (Button) findViewById(R.id.btnScience);

	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnEnglish.setOnClickListener(this);
		mBtnMath.setOnClickListener(this);
		mBtnReading.setOnClickListener(this);
		mBtnScience.setOnClickListener(this);
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

		case R.id.btnEnglish:
			Log.i(TAG, "btnEnglish");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.DAT_SCIENCE;
			final TimerView timerView0 = new TimerView(mContext);
			mActivity.setContentView(timerView0);
			break;
		case R.id.btnMath:
			Log.i(TAG, "btnMath");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.DAT_QR;
			final TimerView timerView1 = new TimerView(mContext);
			mActivity.setContentView(timerView1);
			break;
		case R.id.btnReading:
			Log.i(TAG, "btnReading");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.DAT_RC;
			final TimerView timerView2 = new TimerView(mContext);
			mActivity.setContentView(timerView2);
			break;
		case R.id.btnScience:
			Log.i(TAG, "btnScience");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.DAT_PAT;
			final TimerView timerView3 = new TimerView(mContext);
			mActivity.setContentView(timerView3);
			break;
		}
	}

}
