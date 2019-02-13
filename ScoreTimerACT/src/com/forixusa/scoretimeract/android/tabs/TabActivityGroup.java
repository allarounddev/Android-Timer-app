package com.forixusa.scoretimeract.android.tabs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.forixusa.android.animations.AnimationHelper;

public class TabActivityGroup extends ActivityGroup {

	private static final String TAG = TabActivityGroup.class.getSimpleName();

	protected ArrayList<String> mActivityID;
	protected ArrayList<View> mHistory;
	protected ArrayList<Intent> mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		mActivityID = new ArrayList<String>();
		if (mHistory == null) {
			mHistory = new ArrayList<View>();
		}
		mIntent = new ArrayList<Intent>();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();

		if (!mActivityID.isEmpty()) {
			Log.i(TAG, "!mActivityID.isEmpty()");
			final LocalActivityManager manager = getLocalActivityManager();
			final Activity activity = manager.getActivity(mActivityID.get(mActivityID.size() - 1));
			// manager.dispatchResume();
			activity.onContentChanged();
			// mHistory.get(mHistory.size() - 1).invalidate();
		}
	}

	@Override
	public void finishFromChild(Activity child) {
		Log.i(TAG, "finishFromChild");

		final int length = mHistory.size();
		Log.d(TAG, "length:" + length);
		if (length > 1) {
			final LocalActivityManager manager = getLocalActivityManager();
			manager.destroyActivity(mActivityID.get(length - 1) + "", true);
			mHistory.get(mHistory.size() - 1).startAnimation(AnimationHelper.outToRightAnimation());
			mHistory.get(mHistory.size() - 2).startAnimation(AnimationHelper.inFromLeftAnimation());
			mHistory.remove(length - 1);
			setContentView(mHistory.get(mHistory.size() - 1));

			mActivityID.remove(length - 1);
			mIntent.remove(length - 1);

			if (!mActivityID.isEmpty()) {
				Log.d(TAG, "!mActivityID.isEmpty()");
				final int newlength = mActivityID.size();
				mIntent.get(newlength - 1).addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				// mIntent.get(newlength -
				// 1).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				// Log.d(TAG, "mActivityID.get(newlength - 1):" +
				// mActivityID.get(newlength - 1));
				// final LocalActivityManager manager1 =
				// getLocalActivityManager();
				// final Activity activity =
				// manager.getActivity(mActivityID.get(newlength - 1));
				// activity.onContentChanged();
			}

		} else {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.i(TAG, "onKeyUp");
		if (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	public void startChildActivity(String id, Intent intent) {
		if (mActivityID.contains(id)) {
			final int index = mActivityID.indexOf(id);
			final LocalActivityManager manager = getLocalActivityManager();
			manager.destroyActivity(mActivityID.get(index) + "", true);
			mHistory.remove(index);
			mActivityID.remove(index);
			mIntent.remove(index);
		}
		final Window window = getLocalActivityManager().startActivity(id + "",
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		if (window != null) {
			Log.i(TAG, "startChildActivity: " + id);
			final View view = window.getDecorView();

			if (mHistory.size() > 0) {
				mHistory.get(mHistory.size() - 1).startAnimation(AnimationHelper.outToLeftAnimation());
				view.startAnimation(AnimationHelper.inFromRightAnimation());
			}
			setContentView(view);
			view.setFocusable(true);
			view.requestFocus();
			mHistory.add(view);
			mActivityID.add(id);
			mIntent.add(intent);
		}
	}

	@Override
	public void onBackPressed() {
		Log.i(TAG, "onBackPressed");
		final int length = mHistory.size();
		if (length > 1) {
			final LocalActivityManager manager = getLocalActivityManager();

			manager.destroyActivity(mActivityID.get(length - 1) + "", true);
			mHistory.get(mHistory.size() - 1).startAnimation(AnimationHelper.outToRightAnimation());
			mHistory.get(mHistory.size() - 2).startAnimation(AnimationHelper.inFromLeftAnimation());
			mHistory.remove(length - 1);
			mActivityID.remove(length - 1);
			mIntent.remove(length - 1);
			setContentView(mHistory.get(mHistory.size() - 1));

		} else {
			finish();
		}
	}

}
