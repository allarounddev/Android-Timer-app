package com.forixusa.scoretimer.android.view;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import com.forixusa.android.utils.FileHelper;
import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimer.android.activities.R;

public class AboutUsView extends ContentView {
	public static final String TAG = AboutUsView.class.getSimpleName();

	public AboutUsView(Context context) {
		super(context);

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.about_us_activity, this, true);

		final TextView lblContent = (TextView) findViewById(R.id.lblContent);
		lblContent.setText(Html.fromHtml(FileHelper.readFromFile(mContext, R.raw.about_us)));

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);
	}

}
