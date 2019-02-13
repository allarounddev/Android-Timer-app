package com.forixusa.scoretimer.android.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimer.android.ScoreTimerApplication;
import com.forixusa.scoretimer.android.activities.R;

public class HomeView extends ContentView implements OnClickListener {
	private static final String TAG = HomeView.class.getSimpleName();

	private Button mBtnLogicalReasoning;
	private Button mBtnLogicGame;
	private Button mBtnReadingComprehension;

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

		mBtnLogicalReasoning = (Button) findViewById(R.id.btnLogicalReasoning);
		mBtnLogicGame = (Button) findViewById(R.id.btnLogicGame);
		mBtnReadingComprehension = (Button) findViewById(R.id.btnReadingComprehension);

	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnLogicalReasoning.setOnClickListener(this);
		mBtnLogicGame.setOnClickListener(this);
		mBtnReadingComprehension.setOnClickListener(this);
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

		case R.id.btnLogicalReasoning:
			Log.i(TAG, "btnLogicalReasoning");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.LOGICAL_REASONING;
			final TimerView timerView0 = new TimerView(mContext);
			mActivity.setContentView(timerView0);
			break;
		case R.id.btnLogicGame:
			Log.i(TAG, "btnLogicGame");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.LOGIC_GAME;
			final TimerView timerView1 = new TimerView(mContext);
			mActivity.setContentView(timerView1);
			break;
		case R.id.btnReadingComprehension:
			Log.i(TAG, "btnReadingComprehension");
			ScoreTimerApplication.sTimerTestOption = ScoreTimerApplication.READING_COMPREHENSION;
			final TimerView timerView2 = new TimerView(mContext);
			mActivity.setContentView(timerView2);
			break;
		}
	}

}
