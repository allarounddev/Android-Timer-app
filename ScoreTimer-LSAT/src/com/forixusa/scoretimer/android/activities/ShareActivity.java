package com.forixusa.scoretimer.android.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.forixusa.android.activities.BaseApplication;
import com.forixusa.android.facebookconnection.SessionEvents;
import com.forixusa.android.facebookconnection.SessionEvents.AuthListener;
import com.forixusa.android.facebookconnection.SessionEvents.LogoutListener;
import com.forixusa.android.facebookconnection.SessionStore;
import com.forixusa.android.twitter.TwitterActivity;
import com.forixusa.android.utils.AlertHelper;
import com.forixusa.android.utils.GlobalConstants;
import com.forixusa.android.utils.NetworkHelper;

public class ShareActivity extends Activity implements OnClickListener {
	private static final String TAG = ShareActivity.class.getSimpleName();

	private static final int CODE_SHARE_BY_EMAIL = 1;

	private Button mBtnShareEmail;
	private Button mBtnShareFacebook;
	private Button mBtnShareTwitter;
	private boolean canShareOnTwitterNow = false;
	private boolean isButtonTwitterClicked = false;
	private ProgressDialog mProgressDialog;
	private Facebook mFacebook;

	private String shareContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.share_activity);
		loadUIControls();
		listenerUIControls();

		final Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			shareContent = bundle.getString("shareContent");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isButtonTwitterClicked) {
			postMsgToTwitter();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

	private void loadUIControls() {
		Log.i(TAG, "loadUIControls");

		mBtnShareEmail = (Button) findViewById(R.id.btnShareEmail);
		mBtnShareFacebook = (Button) findViewById(R.id.btnShareFacebook);
		mBtnShareTwitter = (Button) findViewById(R.id.btnShareTwitter);
	}

	private void listenerUIControls() {
		Log.i(TAG, "listenerUIControls");
		mBtnShareEmail.setOnClickListener(this);
		mBtnShareFacebook.setOnClickListener(this);
		mBtnShareTwitter.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnShareEmail:
			final Intent sendMailIntent = new Intent(Intent.ACTION_SEND);
			sendMailIntent.setType("message/rfc822");
			sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, "Score Timer");
			sendMailIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
			startActivityForResult(sendMailIntent, CODE_SHARE_BY_EMAIL);

			break;
		case R.id.btnShareFacebook:

			if (!NetworkHelper.isNetworkAvailable(this)) {
				AlertHelper.showMessageAlert(this, "Network not avilable. Please try again later.");
				return;
			}

			if (mFacebook == null || !mFacebook.isSessionValid()) {
				initFacebook();
				mFacebook.authorize(ShareActivity.this, new String[] { "publish_stream" }, new LoginDialogListener());
			} else {
				postOnWall(shareContent);
			}

			break;
		case R.id.btnShareTwitter:
			Log.i(TAG, "btnShareTwitter click");

			if (!NetworkHelper.isNetworkAvailable(this)) {
				AlertHelper.showMessageAlert(this, "Network not avilable. Please try again later.");
				return;
			}

			isButtonTwitterClicked = true;
			shareViaTwitter();
			break;
		}
	}

	private void postMsgToTwitter() {
		Log.i(TAG, "postMsgToTwitter");
		mProgressDialog = ProgressDialog
				.show(ShareActivity.this, "", getResources().getString(R.string.loading), false);

		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					Log.i(TAG, "start upload tweet");
					BaseApplication.sTwitterUtil.sendTweet(BaseApplication.sTwitterStore, shareContent);
					mHandler.sendEmptyMessage(1);
				} catch (final Exception e) {
					Log.e(TAG, "error when upload tweet:");
					e.printStackTrace();

					// parse error detail from something like this
					// 403:The request is understood, but it has been refused.
					// An accompanying error message will explain why. This code
					// is used when requests are being denied due to update
					// limits
					// (http://support.twitter.com/forums/10711/entries/15364).
					// error - Status is a duplicate.
					// request - /1/statuses/update.json
					final String errorMessage = e.getMessage();
					Log.v(TAG, "errorMessage: " + errorMessage);
					final String[] mgs = errorMessage.split("\n");
					String errorDetail = null;
					if (mgs.length >= 2) {
						try {
							errorDetail = mgs[1].split("-")[1].trim();
						} catch (final Exception ex) {
							ex.printStackTrace();
							errorDetail = ex.getMessage();
						}
					} else {
						errorDetail = errorMessage;
					}

					final Message message = mHandler.obtainMessage();
					message.obj = errorDetail;
					message.what = 0;
					mHandler.sendMessage(message);
				}
			}
		};
		t.start();
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			isButtonTwitterClicked = false;
			switch (msg.what) {
			case 0:
				// AlertHelper.showMessageAlert(ShareActivity.this,
				// "Twiter post failure");
				AlertHelper.showMessageAlert(ShareActivity.this, msg.obj.toString());
				break;
			case 1:
				AlertHelper.showMessageAlert(ShareActivity.this, "Twitter post successful");
				break;
			}
		};
	};

	private void shareViaTwitter() {
		Log.i(TAG, "shareViaTwitter");

		new TwitterAuthenticationTask(new TwitterAuthenticationListener() {
			@Override
			public void onSuccess() {
				// showShareOnTwitterDialog();
				postMsgToTwitter();
			}

			@Override
			public void onFailed() {
				final Intent intent = new Intent(ShareActivity.this, TwitterActivity.class);
				ShareActivity.this.startActivity(intent);
			}
		}).execute();
	}

	private class TwitterAuthenticationTask extends AsyncTask<Void, Void, Void> {
		private final TwitterAuthenticationListener listener;

		public TwitterAuthenticationTask(TwitterAuthenticationListener listener) {
			this.listener = listener;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// showDialog(MyActivityGroup.LOADING_DATA);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// cancelDialog(MyActivityGroup.LOADING_DATA);

			if (canShareOnTwitterNow) {
				listener.onSuccess();
			} else {
				listener.onFailed();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			canShareOnTwitterNow = BaseApplication.sTwitterStore != null
					&& !BaseApplication.sTwitterStore.mToken.equals("")
					&& BaseApplication.sTwitterUtil.isAuthenticated(BaseApplication.sTwitterStore);

			return null;
		}
	}

	private interface TwitterAuthenticationListener {
		public void onSuccess();

		public void onFailed();
	}

	// ================= FACEBOOK ===========================

	private void initFacebook() {
		Log.d(TAG, "initFacebook");

		mFacebook = new Facebook(GlobalConstants.FACEBOOK_APP_ID);
		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new FacebookAuthListener());

		SessionEvents.addLogoutListener(new LogoutListener() {
			@Override
			public void onLogoutBegin() {
			}

			@Override
			public void onLogoutFinish() {
			}
		});
	}

	private final class LoginDialogListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
		}

		@Override
		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class FacebookAuthListener implements AuthListener {

		@Override
		public void onAuthSucceed() {
			final String accessToken = mFacebook.getAccessToken();
			if (accessToken == null) {
				Toast.makeText(ShareActivity.this, "Can't reach to Facebook server.", Toast.LENGTH_SHORT).show();
				return;
			}

			try {
				postOnWall(shareContent);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onAuthFail(String error) {
			Toast.makeText(ShareActivity.this, "Can't reach to Facebook server.", Toast.LENGTH_SHORT).show();
		}
	}

	private void postOnWall(String msg) {
		Log.i(TAG, "postOnWall");
		try {

			final Bundle parameters = new Bundle();
			parameters.putString("message", msg);
			mFacebook.request("feed", parameters, "POST");

		} catch (final Exception e) {
			e.printStackTrace();

			ShareActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ShareActivity.this, "Can not connect to Facebook.", Toast.LENGTH_SHORT).show();
				}
			});
		}

		ShareActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(ShareActivity.this, "Post to wall successful.", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
