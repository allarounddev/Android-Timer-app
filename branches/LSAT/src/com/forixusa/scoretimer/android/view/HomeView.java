package com.forixusa.scoretimer.android.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimer.android.activities.R;

public class HomeView extends ContentView implements OnClickListener {
	private static final String TAG = HomeView.class.getSimpleName();

	private Button mBtnBiological;
	private Button mBtnVerbal;
	private Button mBtnPhysical;

	public HomeView(Context context) {
		super(context);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.home_activity, this, true);
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
		final TimerView timerView = new TimerView(mContext);
		switch (v.getId()) {

		case R.id.btnBiological:
			mActivity.setContentView(timerView);
			break;
		case R.id.btnVerbal:
			mActivity.setContentView(timerView);
			break;
		case R.id.btnPhysical:
			mActivity.setContentView(timerView);
			break;
		}
	}

}
