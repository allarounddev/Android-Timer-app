package com.forixusa.scoretimer.android.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.forixusa.android.utils.FileHelper;
import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimer.android.activities.R;

public class AboutUsView extends ContentView {
	public static final String TAG = AboutUsView.class.getSimpleName();
	private static final int CODE_SEND_EMAIL = 1;

	private final ImageView mImgSendEmail;
	private final ImageView mImgGoToFacebook;

	public AboutUsView(Context context) {
		super(context);

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.about_us_activity, this, true);

		final TextView lblContent = (TextView) findViewById(R.id.lblContent);
		lblContent.setText(Html.fromHtml(FileHelper.readFromFile(mContext, R.raw.about_us)));

		((Button) findViewById(R.id.btnShare)).setOnClickListener(mBtnShareListener);

		mImgSendEmail = (ImageView) findViewById(R.id.imgSendEmail);
		mImgGoToFacebook = (ImageView) findViewById(R.id.imgGoToFacebook);

		mImgSendEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String aEmailList[] = { "info@testscoretimer.com" };

				final Intent sendMailIntent = new Intent(Intent.ACTION_SEND);
				sendMailIntent.setType("message/rfc822");
				sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, "Score Timer");
				sendMailIntent.putExtra(Intent.EXTRA_TEXT, "");
				sendMailIntent.putExtra(Intent.EXTRA_EMAIL, aEmailList);
				mActivity.startActivityForResult(sendMailIntent, CODE_SEND_EMAIL);

			}
		});

		mImgGoToFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intentWeb = new Intent(android.content.Intent.ACTION_VIEW, Uri
						.parse("http://www.facebook.com/pages/Test-Score-Timer-LLC/345963768749080"));
				mActivity.startActivity(intentWeb);

			}
		});

	}

}
