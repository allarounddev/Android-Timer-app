package com.forixusa.android.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.forixusa.android.utils.AlertHelper;
import com.forixusa.scoretimermcat.android.ScoreTimerApplication;
import com.forixusa.scoretimermcat.android.activities.ShareActivity;
import com.forixusa.scoretimermcat.android.models.ScoreResult;
import com.forixusa.scoretimermcat.android.services.ScoreResultService;
import com.forixusa.scoretimermcat.android.tabs.ActivityGroupBase;

public class ContentView extends FrameLayout {
	private static final String TAG = ContentView.class.getSimpleName();

	protected ActivityGroupBase mActivity;
	protected Context mContext;
	private ArrayList<ScoreResult> mScoreResultList;

	public ContentView(Context context) {
		super(context);

		mContext = context;
		mActivity = (ActivityGroupBase) mContext;
		mScoreResultList = new ArrayList<ScoreResult>();
	}

	public void onResumeUI() {
	}

	public void gotoShare() {
		ScoreResult scoreResult = mScoreResultList.get(mScoreResultList.size() - 1);
		String strTestOption = "";
		if (scoreResult.testOption.equals(String.valueOf(ScoreTimerApplication.VERBAL_REASONING))) {
			strTestOption = "Verbal";
		} else if (scoreResult.testOption.equals(String.valueOf(ScoreTimerApplication.PHYSICAL_SCIENCES))) {
			strTestOption = "Physical Science";
		} else if (scoreResult.testOption.equals(String.valueOf(ScoreTimerApplication.BIOLOGICAL_SCIENCES))) {
			strTestOption = "Biological Science";
		}

		String shareContent = String.format("I Just got a %s on my latest MCAT practice test in %s",
				scoreResult.estimationScore, strTestOption);
		final Bundle bundle = new Bundle();
		bundle.putString("shareContent", shareContent);

		Intent intentOfferDetail = new Intent(mActivity, ShareActivity.class);
		intentOfferDetail.putExtras(bundle);
		mActivity.startActivity(intentOfferDetail);
	}

	public final OnClickListener mBtnShareListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i(TAG, "onClick " + " btnShare");
			new GetData().execute();
		}
	};

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
			if (mScoreResultList.isEmpty()) {
				AlertHelper.showMessageAlert(mContext, "Please save a result to share.");
			} else {
				gotoShare();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			synchronized (mScoreResultList) {
				try {
					mScoreResultList = ScoreResultService.getAllScoreResults(mContext);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}
}
