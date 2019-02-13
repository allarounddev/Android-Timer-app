package com.forixusa.scoretimerdat.android.tabs;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimerdat.android.activities.R;
import com.forixusa.scoretimerdat.android.activities.ScoreTimerBaseActivity;
import com.forixusa.scoretimerdat.android.view.HomeView;

public class ActivityGroupBase extends ScoreTimerBaseActivity {
	private static final String TAG = HomeView.class.getSimpleName();

	public static final int LOADING_DATA = 0;
	public static final int ERROR_OPEN_CAMERA = 1;
	public static final int ERROR_SCAN_FROM_FRAME = 2;

	private ArrayList<View> mHistoryViews;
	private ViewGroup mContentParent;
	public boolean mShowLoadingDialog = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (mHistoryViews == null) {
			mHistoryViews = new ArrayList<View>();
			mContentParent = (ViewGroup) getWindow().getDecorView();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == LOADING_DATA && !mShowLoadingDialog) {
			final ProgressDialog progressDialog = new ProgressDialog(getParent());
			progressDialog.setCancelable(true);
			final String loadingStr = getText(R.string.loading).toString();
			progressDialog.setMessage(loadingStr);
			mShowLoadingDialog = true;
			return progressDialog;
		}

		return null;
	}

	@Override
	public void cancelDialog(final int code) {
		try {
			if (code == LOADING_DATA) {
				mShowLoadingDialog = false;
			}

			removeDialog(code);
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setContentView(View view) {
		Log.i(TAG, "setContentView");
		mHistoryViews.add(mContentParent.getChildAt(0));
		mContentParent.removeAllViews();
		mContentParent.addView(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		((ContentView) mContentParent.getChildAt(0)).onResumeUI();
	}

	@Override
	public void onBackPressed() {
		Log.i(TAG, "onBackPressed");

		if (mHistoryViews.size() <= 1) {
			super.onBackPressed();
		} else {
			final int index = mHistoryViews.size() - 1;
			mContentParent.removeAllViews();
			((ContentView) mHistoryViews.get(index)).onResumeUI();
			mContentParent.addView(mHistoryViews.get(index));
			mHistoryViews.remove(index);
		}
	}
}