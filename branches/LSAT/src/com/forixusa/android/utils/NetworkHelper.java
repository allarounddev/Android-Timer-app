package com.forixusa.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

	public static boolean isNetworkAvailable(Context context) {

		boolean value = false;

		final ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			value = true;
		}

		return value;
	}
}
