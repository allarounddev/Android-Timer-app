package com.forixusa.scoretimer.android.activities;

import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import com.forixusa.android.utils.FileHelper;

public class AboutUsActivity extends ScoreTimerBaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about_us_activity);

		final TextView lblContent = (TextView) findViewById(R.id.lblContent);
		lblContent.setText(Html.fromHtml(FileHelper.readFromFile(this, R.raw.about_us)));

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);
	}
}
