package com.forixusa.scoretimer.android.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.forixusa.android.facebookconnection.SessionEvents;
import com.forixusa.android.facebookconnection.SessionStore;
import com.forixusa.android.facebookconnection.SessionEvents.AuthListener;
import com.forixusa.android.facebookconnection.SessionEvents.LogoutListener;
import com.forixusa.android.twitter.TwitterActivity;
import com.forixusa.android.utils.AlertHelper;
import com.forixusa.android.utils.GlobalConstants;
import com.forixusa.scoretimer.android.ScoreTimerApplication;

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

public class ShareActivity extends Activity implements OnClickListener {
	private static final String TAG = ShareActivity.class.getSimpleName();

	private static final int CODE_SHARE_BY_EMAIL = 1;

	private Button mBtnShareEmail;
	private Button mBtnShareFacebook;
	private Button mBtnShareTwitter;
	private boolean canShareOnTwitterNow = false;
	private ProgressDialog mProgressDialog;
	
	private Facebook mFacebook;
//	private AsyncFacebookRunner mAsyncRunner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.share_activity);

		loadUIControls();
		listenerUIControls();
	}
	
	@Override
	public void onResume() {
		super.onResume();	
		
		if (ScoreTimerApplication.sTwitterStatus == 1) {
			new TwitterAuthenticationTask(new TwitterAuthenticationListener() {				
				@Override
				public void onSuccess() {
				//	showShareOnTwitterDialog();
					postMsgToTwitter();
				}
				
				@Override
				public void onFailed() {
				}
			}).execute(); 
			
			ScoreTimerApplication.sTwitterStatus  = -1;
		} else if (ScoreTimerApplication.sTwitterStatus  == 2) {
			AlertHelper.showMessageAlert(getParent(), "Twitter authentication failure");
			ScoreTimerApplication.sTwitterStatus  = -1;
		} 
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
			sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
			sendMailIntent.putExtra(Intent.EXTRA_TEXT, "");
			startActivityForResult(sendMailIntent, CODE_SHARE_BY_EMAIL);

			break;
		case R.id.btnShareFacebook:
			initFacebook();
        	mFacebook.authorize(getParent(), new String[] {"publish_stream"}, 
        			new LoginDialogListener());
			break;
		case R.id.btnShareTwitter:
			Log.i(TAG, "btnShareTwitter click");
			shareViaTwitter();
			break;
		}
	}

	private void postMsgToTwitter() {
		Log.i(TAG, "postMsgToTwitter");
		mProgressDialog = ProgressDialog.show(getParent(), "",
				getResources().getString(R.string.loading), false);

		Thread t = new Thread() {
			public void run() {
				try {
					Log.i(TAG, "start upload tweet");
					ScoreTimerApplication.sTwitterUtil.sendTweet(
							ScoreTimerApplication.sTwitterStore, "Hello abc world");
					mHandler.sendEmptyMessage(1);
				} catch (Exception e) {
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
					String errorMessage = e.getMessage();
					Log.v(TAG, "errorMessage: " + errorMessage);
					String[] mgs = errorMessage.split("\n");
					String errorDetail = null;
					if (mgs.length >= 2) {
						try {
							errorDetail = mgs[1].split("-")[1].trim();
						} catch (Exception ex) {
							ex.printStackTrace();
							errorDetail = ex.getMessage();
						}
					} else {
						errorDetail = errorMessage;
					}

					Message message = mHandler.obtainMessage();
					message.obj = errorDetail;
					message.what = 0;
					mHandler.sendMessage(message);
				}
			}
		};
		t.start();
	}
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}

			switch (msg.what) {
			case 0:
				AlertHelper.showMessageAlert(getParent(), "Twiter post failure");
				break;
			case 1:
				AlertHelper.showMessageAlert(getParent(),"Twiter post successfully");
				break;
			}
		};
	};
	
	private void shareViaTwitter() {
		Log.i(TAG, "shareViaTwitter");
		
		new TwitterAuthenticationTask(new TwitterAuthenticationListener() {
			@Override
			public void onSuccess() {
				//showShareOnTwitterDialog();
				postMsgToTwitter();
			}

			@Override
			public void onFailed() {
				 final Intent intent = new Intent(getParent(),TwitterActivity.class);
				 getParent().startActivity(intent);
			}
		}).execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CODE_SHARE_BY_EMAIL) {

		}
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
			 canShareOnTwitterNow = ScoreTimerApplication.sTwitterStore != null
			 && !ScoreTimerApplication.sTwitterStore.mToken.equals("")
			 &&
			 ScoreTimerApplication.sTwitterUtil.isAuthenticated(ScoreTimerApplication.sTwitterStore);

			return null;
		}
	}

	private interface TwitterAuthenticationListener {
		public void onSuccess();

		public void onFailed();
	}

	
	// ================= FACEBOOK ===========================
	
	private void initFacebook() {
		SessionEvents.clearListeners();
		
		mFacebook = new Facebook(GlobalConstants.FACEBOOK_APP_ID);
      // 	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
       	
       	SessionStore.FacebookShare.restore(mFacebook, this);
        SessionEvents.addAuthListener(new FacebookAuthListener());
        SessionEvents.addLogoutListener(new LogoutListener() {
            public void onLogoutBegin() {
            }

            public void onLogoutFinish() {
            }
        });
	}
	
	private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
        }

        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }
	
	private class FacebookAuthListener implements AuthListener {

        public void onAuthSucceed() {
        	String accessToken = mFacebook.getAccessToken();
        	if(accessToken == null) {
        		Toast.makeText(getParent(),"Can't reach to Facebook server.", 
        				Toast.LENGTH_SHORT).show();
        		return ;
        	}
        	
        	try {
        		postOnWall("Hello World");
				SessionStore.FacebookShare.save(mFacebook, getParent());
        	} catch (Exception e) {
				e.printStackTrace();
			}
        }

        public void onAuthFail(String error) {
        	Toast.makeText(getParent(),"Can't reach to Facebook server.", 
    				Toast.LENGTH_SHORT).show();
        }
	}
	
	private void postOnWall(String msg) {
		try {
			String response = mFacebook.request("me");
			Bundle parameters = new Bundle();
			parameters.putString("message", msg);
			response = mFacebook.request("me/feed", parameters, "POST");
			Log.d(TAG, "got response: " + response);
			if (response == null || response.equals("") || response.equals("false")) {
				Log.v("Error", "Blank response");
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					Toast.makeText(getParent(),"Can't reach to Facebook server.", 
		    				Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(getParent(),"Post to wall successfully.", 
	    				Toast.LENGTH_SHORT).show();
			}
		});
    }
}
