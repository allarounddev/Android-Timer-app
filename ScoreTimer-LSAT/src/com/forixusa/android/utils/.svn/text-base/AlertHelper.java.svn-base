package com.forixusa.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertHelper {

	public static void showMessageAlert(Context context, int stringResourceId, String buttonText) {
		final AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
		alertbox.setTitle("Message");
		alertbox.setMessage(context.getResources().getString(stringResourceId));

		alertbox.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		alertbox.show();
	}

	public static void showMessageAlert(Context context, String message) {
		final AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
		alertbox.setTitle("Message");
		alertbox.setMessage(message);

		alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		alertbox.show();
	}
}
