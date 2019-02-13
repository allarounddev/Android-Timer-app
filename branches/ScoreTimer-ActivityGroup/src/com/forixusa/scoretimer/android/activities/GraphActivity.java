package com.forixusa.scoretimer.android.activities;

import java.util.ArrayList;
import com.forixusa.scoretimer.android.graph.LineChartView;
import com.forixusa.scoretimer.android.models.ScoreResult;
import com.forixusa.scoretimer.android.services.ScoreResultService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;


public class GraphActivity extends ScoreTimerBaseActivity {
	private static final String TAG = GraphActivity.class.getSimpleName();
	
	private LinearLayout mLLGraph;
	private RadioGroup  mRadioGroupResult;
	private RadioGroup  mRadioGroupShow;
	private LineChartView mGraphView;
	private ArrayList<ScoreResult> mScoreResults;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.graph_activity);
		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);
	
		mLLGraph = (LinearLayout)findViewById(R.id.lLGraph);
		mScoreResults = new ArrayList<ScoreResult>();
		
		loadUIControls();
		
		mRadioGroupResult.check(R.id.rdbAccuracy);
		mRadioGroupShow.check(R.id.rdbLast10Tests);
		
		mGraphView = new LineChartView(getParent());
		mGraphView.setData(new ArrayList<Float>());	
		mLLGraph.addView(mGraphView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		listenerUIControls();
	}
	
	private void loadUIControls() {
		Log.i(TAG, "loadUIControls");

		mRadioGroupResult = (RadioGroup) findViewById(R.id.rdgResult);
		mRadioGroupShow = (RadioGroup) findViewById(R.id.rdgShow);
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
				
		mRadioGroupResult.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	Log.v("Selected", "New radio item selected: " + checkedId);
	            ArrayList<Float> scores = new ArrayList<Float>();
	            if(checkedId == R.id.rdbAccuracy) {
	            	Log.v("Selected", "R.id.rdbAccuracy");
	            	 if(!mScoreResults.isEmpty()) {
	     	            for(ScoreResult scoreResult : mScoreResults) {
	     	            	scores.add((float)scoreResult.accuracy);
	     	            }
	                 }
	            } else if(checkedId == R.id.rdbPace) {
	            	Log.v("Selected", "R.id.rdbPace");
	            	 if(!mScoreResults.isEmpty()) {
	     	            for(ScoreResult scoreResult : mScoreResults) {
	     	            	scores.add((float)scoreResult.pace);
	     	            }
	                 }
	            } else if(checkedId == R.id.rdbEstimatedScore) {
	            	Log.v("Selected", "R.id.rdbEstimatedScore");
	            	 if(!mScoreResults.isEmpty()) {
	     	            for(ScoreResult scoreResult : mScoreResults) {
	     	            	scores.add((float)scoreResult.estimationScore);
	     	            }
	                 }
	            }
	            mGraphView.setData(scores);	
	            mGraphView.invalidate();
	        }
		});
		
		mRadioGroupShow.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            Log.v("Selected", "New radio item selected: " + checkedId);
	        }
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
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
            
    		ArrayList<Float> scores = new ArrayList<Float>();
            if(!mScoreResults.isEmpty()) {
	            for(ScoreResult scoreResult : mScoreResults) {
	            	scores.add((float)scoreResult.accuracy);
	            }
            }
            mGraphView.setData(scores);	
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
