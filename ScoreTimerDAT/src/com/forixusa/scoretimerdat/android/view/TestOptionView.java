package com.forixusa.scoretimerdat.android.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.forixusa.android.view.ContentView;
import com.forixusa.scoretimerdat.android.ScoreTimerApplication;
import com.forixusa.scoretimerdat.android.activities.R;

public class TestOptionView extends ContentView implements OnItemClickListener {

	private final ListView mList;
	private final TestOptionAdapter mTestOptionAdapter;
	private final ArrayList<String> mTestOptions = new ArrayList<String>();
	private final int mTestOption;

	// public static boolean sIsFromResultView;

	public TestOptionView(Context context, int testOption) {
		super(context);

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.test_option_activity, this, true);

		mTestOptions.add("Science");
		mTestOptions.add("QR");
		mTestOptions.add("RC");
		mTestOptions.add("PAT");

		mList = (ListView) findViewById(R.id.list);
		mList.setOnItemClickListener(TestOptionView.this);
		mTestOptionAdapter = new TestOptionAdapter(mContext, mTestOptions);
		mList.setAdapter(mTestOptionAdapter);
		mTestOption = testOption;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (ScoreTimerApplication.sIsFromTimer) {
			ScoreTimerApplication.sIsFromTimer = false;
			ScoreTimerApplication.sTimerTestOption = position;
		} else {
			ScoreTimerApplication.sSummaryTestOption = position;
		}
		// sIsFromResultView = true;
		mActivity.onBackPressed();
	}

	private class TestOptionAdapter extends ArrayAdapter<String> {
		private final LayoutInflater inflater;

		public TestOptionAdapter(final Context context, final int textViewResourceId, final List<String> objects) {
			super(context, textViewResourceId, objects);
			inflater = LayoutInflater.from(context);
		}

		public TestOptionAdapter(final Context context, final List<String> objects) {
			this(context, 0, objects);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.lv_item_test_option, null);
				holder.lblName = (TextView) convertView.findViewById(R.id.lblName);
				holder.imgSelected = (ImageView) convertView.findViewById(R.id.imgSelected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.lblName.setText(getItem(position));
			if (position == mTestOption) {
				holder.imgSelected.setVisibility(View.VISIBLE);
			}
			return convertView;
		}
	}

	class ViewHolder {
		TextView lblName;
		ImageView imgSelected;
	}

}
