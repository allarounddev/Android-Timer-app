package com.forixusa.scoretimersat.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.forixusa.android.utils.GlobalConstants;
import com.forixusa.scoretimersat.android.activities.R;
import com.forixusa.scoretimersat.android.models.ScoreResult;

public class SummaryAdapter extends BaseAdapter {
	private ArrayList<ScoreResult> mArray = new ArrayList<ScoreResult>();
	private final LayoutInflater inflater;
	private final Context mContext;

	public SummaryAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mArray.size();
	}

	@Override
	public Object getItem(int position) {
		return mArray.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void setArrayItem(ArrayList<ScoreResult> array) {
		mArray = array;
	}

	class ViewHolder {
		TextView txtAccuracy;
		TextView txtPace;
		TextView txtEstimatedScore;
		TextView txtDate;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			final LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.lv_item_score_result, null);

			holder.txtAccuracy = (TextView) convertView.findViewById(R.id.txtAccuracy);
			holder.txtPace = (TextView) convertView.findViewById(R.id.txtPace);
			holder.txtEstimatedScore = (TextView) convertView.findViewById(R.id.txtEstimatedScore);
			holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ScoreResult scoreResult = mArray.get(position);
		holder.txtAccuracy.setText(String.valueOf(scoreResult.accuracy) + "%");
		holder.txtPace.setText(String.valueOf(scoreResult.pace));
		holder.txtEstimatedScore.setText(String.valueOf(scoreResult.estimationScore));
		holder.txtDate.setText(GlobalConstants.DISPLAY_DATE_PARSER.format(scoreResult.date));

		return convertView;
	}
}
