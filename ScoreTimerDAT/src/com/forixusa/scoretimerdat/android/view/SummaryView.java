package com.forixusa.scoretimerdat.android.view;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.forixusa.android.quickaction.ActionItem;
import com.forixusa.android.quickaction.QuickAction;
import com.forixusa.android.utils.NumberHelper;
import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimerdat.android.ScoreTimerApplication;
import com.forixusa.scoretimerdat.android.activities.R;
import com.forixusa.scoretimerdat.android.adapter.SummaryAdapter;
import com.forixusa.scoretimerdat.android.models.ScoreResult;
import com.forixusa.scoretimerdat.android.services.ScoreResultService;

public class SummaryView extends ContentView implements OnClickListener {
	private static final String TAG = SummaryView.class.getSimpleName();

	private ListView mListScoreResult;
	public SummaryAdapter mSummaryAdapter;

	private Button mBtnReset;
	private Button mBtnGraph;
	private Button mBtnTestOptions;

	private TextView mTxtAverageAccuracy;
	private TextView mTxtAveragePace;
	private TextView mTxtAverageEstimatedScore;
	private ArrayList<ScoreResult> mScoreResults;

	private ImageView mImgGraphHint;

	// public static int sTestOption =
	// ScoreTimerApplication.BIOLOGICAL_SCIENCES;

	public SummaryView(Context context) {
		super(context);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.summary_activity, this, true);

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);

		loadUIControls();
		listenerUIControls();

		mScoreResults = new ArrayList<ScoreResult>();
		mSummaryAdapter = new SummaryAdapter(mContext);
		mSummaryAdapter.setArrayItem(mScoreResults);
		mListScoreResult.setAdapter(mSummaryAdapter);
	}

	@Override
	public void onResumeUI() {
		super.onResumeUI();
		Log.i(TAG, "onResumeUI");
		if (ScoreTimerApplication.sSummaryTestOption == ScoreTimerApplication.DAT_SCIENCE) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt1);
		} else if (ScoreTimerApplication.sSummaryTestOption == ScoreTimerApplication.DAT_QR) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt2);
		} else if (ScoreTimerApplication.sSummaryTestOption == ScoreTimerApplication.DAT_RC) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt3);
		} else if (ScoreTimerApplication.sSummaryTestOption == ScoreTimerApplication.DAT_PAT) {
			mBtnTestOptions.setBackgroundResource(R.drawable.tt4);
		}

		new GetData().execute();
	}

	private void loadUIControls() {
		Log.i(TAG, "loadUIControls");

		mBtnReset = (Button) findViewById(R.id.btnReset);
		mBtnGraph = (Button) findViewById(R.id.btnGraph);

		mTxtAverageAccuracy = (TextView) findViewById(R.id.txtAverageAccuracy);
		mTxtAveragePace = (TextView) findViewById(R.id.txtAveragePace);
		mTxtAverageEstimatedScore = (TextView) findViewById(R.id.txtAverageEstimatedScore);

		mListScoreResult = (ListView) findViewById(R.id.lvSummary);
		mBtnTestOptions = (Button) findViewById(R.id.btnTestOptions);
		mImgGraphHint = (ImageView) findViewById(R.id.imgGraphHint);
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnReset.setOnClickListener(this);
		mBtnGraph.setOnClickListener(this);
		mBtnTestOptions.setOnClickListener(this);

		mImgGraphHint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final QuickAction quickAction = new QuickAction(mContext, QuickAction.VERTICAL);
				ActionItem graphItem = new ActionItem(1,
						"Hit GRAPH to graph your results and compare them to your prior results.", null);

				// ActionItem graphItem = new ActionItem(1,
				// getResources().getString(R.string.hint_5), null);

				quickAction.addActionItem(graphItem);
				quickAction.show(v);

				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						quickAction.dismiss();
					}
				}, 3000);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnReset:
			resetConfirmMessage(mContext, "Are you sure you want to delete ALL of your saves?");
			break;
		case R.id.btnGraph:
			final GraphView view = new GraphView(mContext);
			mActivity.setContentView(view);
			break;
		case R.id.btnTestOptions:
			ScoreTimerApplication.sNeedToRefresh = true;
			final TestOptionView testOptionView = new TestOptionView(mContext, ScoreTimerApplication.sSummaryTestOption);
			mActivity.setContentView(testOptionView);
			break;
		}
	}

	private void resetConfirmMessage(Context context, String message) {
		final AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
		alertbox.setTitle("Message");
		alertbox.setMessage(message);

		alertbox.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				ScoreResultService.deleteByTestOption(mContext,
						String.valueOf(ScoreTimerApplication.sSummaryTestOption));
				new GetData().execute();
				dialog.dismiss();
			}
		});

		alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		alertbox.show();
	}

	private class GetData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.i(TAG, "onPreExecute");
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.i(TAG, "onPostExecute");
			mSummaryAdapter.setArrayItem(mScoreResults);
			mSummaryAdapter.notifyDataSetChanged();

			double averageAccuracy = 0;
			double averagePace = 0;
			double averageEstimatedScore = 0;

			if (!mScoreResults.isEmpty()) {
				for (final ScoreResult scoreResult : mScoreResults) {
					averageAccuracy += scoreResult.accuracy;
					averagePace += scoreResult.pace;
					averageEstimatedScore += scoreResult.estimationScore;
				}
				final int n = mScoreResults.size();
				averageAccuracy = NumberHelper.roundTwoDecimals(averageAccuracy / n);
				averagePace = NumberHelper.roundTwoDecimals(averagePace / n);
				averageEstimatedScore = NumberHelper.roundTwoDecimals(averageEstimatedScore / n);
			}

			if (averageAccuracy > 0) {
				mTxtAverageAccuracy.setText(String.valueOf(averageAccuracy) + "%");
			} else {
				mTxtAverageAccuracy.setText("");
			}

			if (averagePace > 0) {
				mTxtAveragePace.setText(String.valueOf(averagePace));
			} else {
				mTxtAveragePace.setText("");
			}

			if (averageEstimatedScore > 0) {
				mTxtAverageEstimatedScore.setText(String.valueOf(averageEstimatedScore));
			} else {
				mTxtAverageEstimatedScore.setText("");
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			synchronized (mScoreResults) {
				try {
					mScoreResults = ScoreResultService.getScoreResults(mContext,
							String.valueOf(ScoreTimerApplication.sSummaryTestOption));
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

	}

}
