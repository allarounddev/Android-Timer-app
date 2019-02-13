package com.forixusa.scoretimer.android.activities;

import java.util.Date;

import com.forixusa.scoretimer.android.models.ScoreResult;
import com.forixusa.scoretimer.android.services.ScoreResultService;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends ScoreTimerBaseActivity implements OnClickListener {
	private static final String TAG = ResultActivity.class.getSimpleName();

	private Button mBtnSave;
	private Button mBtnAdjustInput;
	
	private TextView mTxtAccuracy;
	private TextView mTxtPace;
	private TextView mTxtEstimatedCorrect;
	private TextView mTxtEstimatedScore;
	private ScoreResult mScoreResult;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.result_activity);
		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);

		loadUIControls();
		listenerUIControls();
		

		final Bundle bundle = this.getIntent().getExtras();
	    if(bundle != null) {
	    	mScoreResult = new ScoreResult();
	    	mScoreResult.accuracy = bundle.getDouble("scoreResult.accuracy")*100;
	    	mScoreResult.pace = bundle.getDouble("scoreResult.pace");
	    	mScoreResult.estimationScore = bundle.getDouble("scoreResult.estimationCorrect");
			
	    	mTxtAccuracy.setText(String.valueOf(mScoreResult.accuracy) + "%");	
	    	mTxtPace.setText(String.valueOf(mScoreResult.pace));
	    	mTxtEstimatedCorrect.setText(String.valueOf(mScoreResult.estimationScore));
	    	mTxtEstimatedScore.setText(String.valueOf(bundle.getDouble("scoreResult.estimationScore")));
	    }
	}

	private void loadUIControls() {
		Log.i(TAG, "loadUIControls");

		mBtnSave = (Button) findViewById(R.id.btnSave);
		mBtnAdjustInput = (Button) findViewById(R.id.btnAdjustInput);
		
		mTxtAccuracy = (TextView) findViewById(R.id.txtAccuracy);
		mTxtPace = (TextView) findViewById(R.id.txtPace);
		mTxtEstimatedCorrect = (TextView) findViewById(R.id.txtEstimatedCorrect);
		mTxtEstimatedScore = (TextView) findViewById(R.id.txtEstimatedScore);
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnSave.setOnClickListener(this);
		mBtnAdjustInput.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnSave:
			if(mScoreResult != null) {
				mScoreResult.date = new Date();			
				ScoreResultService.save(getParent(), mScoreResult);
				ScoreTimerActivity.getInstance().displayTabSummary();
			}
			break;
		case R.id.btnAdjustInput:
			finish();
			break;
		}
	}

}
