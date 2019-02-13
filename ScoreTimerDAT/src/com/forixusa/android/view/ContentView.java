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
import com.forixusa.scoretimerdat.android.ScoreTimerApplication;
import com.forixusa.scoretimerdat.android.activities.ShareActivity;
import com.forixusa.scoretimerdat.android.models.ScoreResult;
import com.forixusa.scoretimerdat.android.services.ScoreResultService;
import com.forixusa.scoretimerdat.android.tabs.ActivityGroupBase;

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
		if (scoreResult.testOption.equals(String.valueOf(ScoreTimerApplication.DAT_SCIENCE))) {
			strTestOption = "DAT Science";
		} else if (scoreResult.testOption.equals(String.valueOf(ScoreTimerApplication.DAT_QR))) {
			strTestOption = "DAT QR";
		} else if (scoreResult.testOption.equals(String.valueOf(ScoreTimerApplication.DAT_RC))) {
			strTestOption = "DAT RC";
		} else if (scoreResult.testOption.equals(String.valueOf(ScoreTimerApplication.DAT_PAT))) {
			strTestOption = "DAT PAT";
		}

		String shareContent = String.format("I Just got a %s on my latest DAT practice test in %s",
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
