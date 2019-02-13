package com.forixusa.android.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.forixusa.android.activities.BaseApplication;

public class ButtonBoldTemplate extends Button {

	public ButtonBoldTemplate(Context context) {
		super(context);
		init();
	}

	public ButtonBoldTemplate(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setTypeface(BaseApplication.helveticaTypeface, Typeface.BOLD);
	}
}
