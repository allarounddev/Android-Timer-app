package com.forixusa.scoretimergre.android.view;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimergre.android.ScoreTimerApplication;
import com.forixusa.scoretimergre.android.activities.R;
import com.forixusa.scoretimergre.android.graph.LineChartView;
import com.forixusa.scoretimergre.android.models.ScoreResult;
import com.forixusa.scoretimergre.android.services.ScoreResultService;

public class GraphView extends ContentView {
	private static final String TAG = GraphView.class.getSimpleName();

	private final LinearLayout mLLGraph;
	private RadioGroup mRadioGroupResult;
	private RadioGroup mRadioGroupShow;
	private final LineChartView mGraphView;
	private ArrayList<ScoreResult> mScoreResults;

	private int mShowTest;
	private boolean mIsAccuracy;
	private boolean mIsPace;
	private boolean mIsEstimatedScore;

	public GraphView(Context context) {
		super(context);

		Log.i(TAG, "GraphView");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.graph_activity, this, true);

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);

		mLLGraph = (LinearLayout) findViewById(R.id.lLGraph);
		mScoreResults = new ArrayList<ScoreResult>();

		loadUIControls();

		mRadioGroupResult.check(R.id.rdbAccuracy);
		mRadioGroupShow.check(R.id.rdbLast10Tests);

		mGraphView = new LineChartView(mContext);
		mGraphView.setData(new ArrayList<Float>());
		mLLGraph.addView(mGraphView, new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));

		listenerUIControls();
		mShowTest = 10;
		mIsAccuracy = true;
		new GetData().execute();
	}

	private void loadUIControls() {
		Log.i(TAG, "loadUIControls");

		mRadioGroupResult = (RadioGroup) findViewById(R.id.rdgResult);
		mRadioGroupShow = (RadioGroup) findViewById(R.id.rdgShow);

	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");

		mRadioGroupResult.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Log.v("Selected", "New radio item selected: " + checkedId);
				final ArrayList<Float> scores = new ArrayList<Float>();
				if (checkedId == R.id.rdbAccuracy) {
					Log.v("Selected", "R.id.rdbAccuracy");
					mIsAccuracy = true;
					mIsPace = false;
					mIsEstimatedScore = false;
					// if (!mScoreResults.isEmpty()) {
					// for (final ScoreResult scoreResult : mScoreResults) {
					// scores.add((float) scoreResult.accuracy);
					// }
					// }
				} else if (checkedId == R.id.rdbPace) {
					Log.v("Selected", "R.id.rdbPace");
					mIsAccuracy = false;
					mIsPace = true;
					mIsEstimatedScore = false;
					// if (!mScoreResults.isEmpty()) {
					// for (final ScoreResult scoreResult : mScoreResults) {
					// scores.add((float) scoreResult.pace);
					// }
					// }
				} else if (checkedId == R.id.rdbEstimatedScore) {
					Log.v("Selected", "R.id.rdbEstimatedScore");
					mIsAccuracy = false;
					mIsPace = false;
					mIsEstimatedScore = true;
					// if (!mScoreResults.isEmpty()) {
					// for (final ScoreResult scoreResult : mScoreResults) {
					// scores.add((float) scoreResult.estimationScore);
					// }
					// }
				}
				// mGraphView.setData(scores);
				// mGraphView.invalidate();

				drawGraph();
			}
		});

		mRadioGroupShow.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Log.v("Selected", "New radio item selected: " + checkedId);
				if (checkedId == R.id.rdbLast10Tests) {
					mShowTest = 10;
				} else if (checkedId == R.id.rdbLast20Tests) {
					mShowTest = 20;
				} else if (checkedId == R.id.rdbAll) {
					mShowTest = -1;
				}

				// mGraphView.setData(scores);
				// mGraphView.invalidate();
				drawGraph();
			}
		});
	}

	@Override
	public void onResumeUI() {
		super.onResumeUI();
		Log.i(TAG, "onResumeUI");

		mRadioGroupResult.check(R.id.rdbAccuracy);
		mRadioGroupShow.check(R.id.rdbLast10Tests);
		mShowTest = 10;
		mIsAccuracy = true;
		new GetData().execute();
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

			final ArrayList<Float> scores = new ArrayList<Float>();
			if (!mScoreResults.isEmpty()) {
				if (mShowTest == -1) {
					for (final ScoreResult scoreResult : mScoreResults) {
						scores.add((float) scoreResult.accuracy);
					}
				} else {
					int count = 1;
					for (final ScoreResult scoreResult : mScoreResults) {
						scores.add((float) scoreResult.accuracy);
						if (count == mShowTest)
							break;

						count++;
					}
				}
			}
			mGraphView.setData(scores);
			mGraphView.invalidate();
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

	private void drawGraph() {
		if (mScoreResults.isEmpty())
			return;

		final ArrayList<Float> scores = new ArrayList<Float>();

		if (mShowTest == -1) {
			if (mIsAccuracy) {
				for (final ScoreResult scoreResult : mScoreResults) {
					scores.add((float) scoreResult.accuracy);
				}
			} else if (mIsPace) {
				for (final ScoreResult scoreResult : mScoreResults) {
					scores.add((float) scoreResult.pace);
				}
			} else if (mIsEstimatedScore) {
				for (final ScoreResult scoreResult : mScoreResults) {
					scores.add((float) scoreResult.estimationScore);
				}
			}
		} else {
			if (mIsAccuracy) {
				int countAccuracy = 1;
				for (final ScoreResult scoreResult : mScoreResults) {
					scores.add((float) scoreResult.accuracy);
					if (countAccuracy == mShowTest)
						break;
					countAccuracy++;
				}
			} else if (mIsPace) {
				int countPace = 1;
				for (final ScoreResult scoreResult : mScoreResults) {
					scores.add((float) scoreResult.pace);
					if (countPace == mShowTest)
						break;
					countPace++;
				}
			} else if (mIsEstimatedScore) {
				int countEstimatedScore = 1;
				for (final ScoreResult scoreResult : mScoreResults) {
					scores.add((float) scoreResult.estimationScore);
					if (countEstimatedScore == mShowTest)
						break;
					countEstimatedScore++;
				}
			}
		}

		mGraphView.setData(scores);
		mGraphView.invalidate();
	}
}
