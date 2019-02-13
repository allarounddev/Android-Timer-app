package com.forixusa.scoretimersat.android.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.forixusa.scoretimersat.android.tabs.TabAboutUs;
import com.forixusa.scoretimersat.android.tabs.TabHome;
import com.forixusa.scoretimersat.android.tabs.TabInstruction;
import com.forixusa.scoretimersat.android.tabs.TabSummary;

public class ScoreTimerActivity extends TabActivity {
	private static final String TAG = ScoreTimerActivity.class.getSimpleName();

	private static final String TAB_HOME = "Home";
	private static final String TAB_INSTRUCTIONS = "Instructions";
	private static final String TAB_SUMMARY = "Summary";
	private static final String TAB_ABOUT_US = "AboutUs";

	private TabHost mTabHost;

	private static ScoreTimerActivity instance = null;

	public static ScoreTimerActivity getInstance() {
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
		instance = this;
		setContentView(R.layout.tab);

		mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec(TAB_HOME)
				.setIndicator(getLayoutInflater().inflate(R.layout.tab_home, null))
				.setContent(new Intent(this, TabHome.class)));

		mTabHost.addTab(mTabHost.newTabSpec(TAB_INSTRUCTIONS)
				.setIndicator(getLayoutInflater().inflate(R.layout.tab_instruction, null))
				.setContent(new Intent(this, TabInstruction.class)));

		mTabHost.addTab(mTabHost.newTabSpec(TAB_SUMMARY)
				.setIndicator(getLayoutInflater().inflate(R.layout.tab_summary, null))
				.setContent(new Intent(this, TabSummary.class)));

		mTabHost.addTab(mTabHost.newTabSpec(TAB_ABOUT_US)
				.setIndicator(getLayoutInflater().inflate(R.layout.tab_about_us, null))
				.setContent(new Intent(this, TabAboutUs.class)));
	}

	public void displayTabSummary() {
		mTabHost.setCurrentTabByTag(TAB_SUMMARY);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ScoreTimerApplication.sIsFirstTime = true;

	}

	@Override
	public void onStop() {
		super.onStop();
		// ScoreTimerApplication.sIsFirstTime = true;
	}
}