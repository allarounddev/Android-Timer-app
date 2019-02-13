package com.forixusa.scoretimerdat.android.view;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.forixusa.android.quickaction.ActionItem;
import com.forixusa.android.quickaction.QuickAction;
import com.forixusa.android.utils.NumberHelper;
import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimerdat.android.ScoreTimerApplication;
import com.forixusa.scoretimerdat.android.activities.R;
import com.forixusa.scoretimerdat.android.activities.ScoreTimerActivity;
import com.forixusa.scoretimerdat.android.models.ScoreResult;
import com.forixusa.scoretimerdat.android.services.ScoreResultService;

public class ResultView extends ContentView implements OnClickListener {
	private static final String TAG = ResultView.class.getSimpleName();

	private Button mBtnSave;
	private Button mBtnAdjustInput;

	private TextView mTxtAccuracy;
	private TextView mTxtPace;
	private TextView mTxtEstimatedCorrect;
	private TextView mTxtEstimatedScore;
	private ScoreResult mScoreResult;

	private ImageView mImgSaveHint;
	public static boolean sIsFromResultView;

	public ResultView(Context context, final Bundle bundle) {
		super(context);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.result_activity, this, true);

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);
		loadUIControls();
		listenerUIControls();

		if (bundle != null) {
			mScoreResult = new ScoreResult();
			mScoreResult.accuracy = NumberHelper.roundTwoDecimals(bundle.getDouble("scoreResult.accuracy") * 100);
			mScoreResult.pace = bundle.getDouble("scoreResult.pace");
			mScoreResult.estimationCorrect = bundle.getDouble("scoreResult.estimationCorrect");
			mScoreResult.estimationScore = bundle.getDouble("scoreResult.estimationScore");
			mScoreResult.testOption = bundle.getString("scoreResult.testOption");

			mTxtAccuracy.setText(String.valueOf(mScoreResult.accuracy) + "%");
			mTxtPace.setText(String.valueOf(mScoreResult.pace));
			mTxtEstimatedCorrect.setText(String.valueOf(mScoreResult.estimationCorrect));
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
		mImgSaveHint = (ImageView) findViewById(R.id.imgSaveHint);

	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnSave.setOnClickListener(this);
		mBtnAdjustInput.setOnClickListener(this);
		mImgSaveHint.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnSave:
			if (mScoreResult != null) {
				mScoreResult.date = new Date();
				ScoreResultService.save(mContext, mScoreResult);
				ScoreTimerApplication.sSummaryTestOption = ScoreTimerApplication.sTimerTestOption;
				mActivity.onBackPressed();
				ScoreTimerActivity.getInstance().displayTabSummary();
			}
			break;
		case R.id.btnAdjustInput:
			sIsFromResultView = true;
			mActivity.onBackPressed();
			break;
		case R.id.imgSaveHint:
			final QuickAction quickAction = new QuickAction(mContext, QuickAction.VERTICAL);
			ActionItem saveItem = new ActionItem(1,
					"If you want to keep this test, hit SAVE.                            ", null);

			// ActionItem saveItem = new ActionItem(1,
			// getResources().getString(R.string.hint_4), null);

			quickAction.addActionItem(saveItem);

			quickAction.show(v);

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					quickAction.dismiss();
				}
			}, 3000);

			break;
		}
	}

}
