package com.forixusa.scoretimer.android.activities;

import java.util.ArrayList;
import com.forixusa.android.utils.NumberHelper;
import com.forixusa.scoretimer.android.adapter.SummaryAdapter;
import com.forixusa.scoretimer.android.models.ScoreResult;
import com.forixusa.scoretimer.android.services.ScoreResultService;
import com.forixusa.scoretimer.android.tabs.TabActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SummaryActivity extends ScoreTimerBaseActivity implements OnClickListener{
	private static final String TAG = SummaryActivity.class.getSimpleName();

	private ListView mListScoreResult;
	public SummaryAdapter mSummaryAdapter;
	
	private Button mBtnReset;
	private Button mBtnGraph;
	
	private TextView mTxtAverageAccuracy;
	private TextView mTxtAveragePace;
	private TextView mTxtAverageEstimatedScore;
	private ArrayList<ScoreResult> mScoreResults;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.summary_activity);
		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);
	
		loadUIControls();
		listenerUIControls();
		
		mScoreResults = new ArrayList<ScoreResult>();
		mSummaryAdapter = new SummaryAdapter(getParent());
		mSummaryAdapter.setArrayItem(mScoreResults);
		mListScoreResult.setAdapter(mSummaryAdapter);
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
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
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnReset.setOnClickListener(this);
		mBtnGraph.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnReset:
			resetConfirmMessage(getParent(), "Are you sure you want to delete ALL of your saves?");
			break;
		case R.id.btnGraph:
			final Intent intentGraph = new Intent(getParent(), GraphActivity.class);
			final TabActivityGroup parentActivity = (TabActivityGroup) getParent();
			parentActivity.startChildActivity("GraphActivity", intentGraph);
//			Intent intent = null;
//			intent = (new AverageTemperatureChart()).execute(getParent());
//			final TabActivityGroup parentActivity = (TabActivityGroup) getParent();
//			parentActivity.startChildActivity("GraphActivity", intent);
//			 
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
				ScoreResultService.deleteAlls(getParent());
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
            
            if(!mScoreResults.isEmpty()) {
	            for(ScoreResult scoreResult : mScoreResults) {
	            	averageAccuracy += scoreResult.accuracy;
	            	averagePace += scoreResult.pace;
	            	averageEstimatedScore = scoreResult.estimationScore;
	            }
	            final int n = mScoreResults.size();
	            averageAccuracy = NumberHelper.roundTwoDecimals(averageAccuracy/n);
	            averagePace = NumberHelper.roundTwoDecimals(averagePace/n);
	            averageEstimatedScore = NumberHelper.roundTwoDecimals(averageEstimatedScore/n);
            }
            
           mTxtAverageAccuracy.setText(String.valueOf(averageAccuracy));
           mTxtAveragePace.setText(String.valueOf(averagePace));
           mTxtAverageEstimatedScore.setText(String.valueOf(averageEstimatedScore));
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (mScoreResults) {
                try {
                	mScoreResults = ScoreResultService.getScoreResults(getParent());
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

}
