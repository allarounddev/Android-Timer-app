package com.forixusa.scoretimer.android.activities;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.forixusa.android.utils.FileHelper;
import com.forixusa.scoretimer.android.ScoreTimerApplication;

public class InstructionActivity extends ScoreTimerBaseActivity {
	private static final String TAG = ScoreTimerActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.instruction_activity);

		final TextView lblContent = (TextView) findViewById(R.id.lblContent);
		lblContent.setText(Html.fromHtml(FileHelper.readFromFile(this, R.raw.instructions)));

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);

	}
}
