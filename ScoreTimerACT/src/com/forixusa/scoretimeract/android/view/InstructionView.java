package com.forixusa.scoretimeract.android.view;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import com.forixusa.android.utils.FileHelper;
import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimeract.android.activities.R;

public class InstructionView extends ContentView {
	public static final String TAG = InstructionView.class.getSimpleName();

	public InstructionView(Context context) {
		super(context);

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.instruction_activity, this, true);

		final TextView lblContent = (TextView) findViewById(R.id.lblContent);
		lblContent.setText(Html.fromHtml(FileHelper.readFromFile(mContext, R.raw.instructions)));

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);
	}
}
