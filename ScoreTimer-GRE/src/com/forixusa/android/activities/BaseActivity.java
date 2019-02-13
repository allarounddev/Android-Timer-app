package com.forixusa.android.activities;

import android.app.Activity;
import android.app.ProgressDialog;

import com.forixusa.android.utils.AlertHelper;
import com.forixusa.scoretimergre.android.activities.R;

public class BaseActivity extends Activity {

	protected static final int LOADING_DATA = 0;
	protected boolean mConnectionHasProblem = false;
	protected boolean mShowLoadingDialog = false;

	public void cancelDialog(int code) {
		try {
			if (code == BaseActivity.LOADING_DATA) {
				mShowLoadingDialog = false;
			}
			removeDialog(code);
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	protected ProgressDialog createDialog() {
		final ProgressDialog loading = new ProgressDialog(this);
		loading.setCancelable(true);
		final String loadingStr = getText(R.string.loading).toString();
		loading.setMessage(loadingStr);
		mShowLoadingDialog = true;

		return loading;
	}

	protected void showNetworkProblem() {
		if (!isFinishing()) {
			AlertHelper.showMessageAlert(BaseActivity.this, R.string.network_error, "OK");
		}
		mConnectionHasProblem = false;
	}

}
