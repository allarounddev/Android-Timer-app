package com.forixusa.scoretimer.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.forixusa.scoretimer.android.view.TimerView;

public class SpinnerAdapter extends ArrayAdapter<String> {
	private static final String TAG = TimerView.class.getSimpleName();

	private ArrayList<String> mDatas;

	public SpinnerAdapter(Context context, ArrayList<String> list) {
		super(context, android.R.layout.simple_spinner_item);
		Log.d(TAG, "SpinnerAdapter");
		mDatas = new ArrayList<String>();
		mDatas = list;

		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	public void setData(ArrayList<String> list) {
		mDatas = list;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public String getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
