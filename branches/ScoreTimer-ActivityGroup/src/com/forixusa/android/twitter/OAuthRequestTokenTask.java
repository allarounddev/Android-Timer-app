/**
 * 
 */
package com.forixusa.android.twitter;

import com.forixusa.android.twitter.TwitterUtil.TwitterConstant;
import com.forixusa.android.twitter.TwitterUtil.TwitterStore;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

public class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void> {
	
	private CommonsHttpOAuthConsumer httpOauthConsumer;
    private CommonsHttpOAuthProvider httpOauthprovider;
	private Context context;
	
	
	private String urlBrowser;
	
	private TwitterStore dataStore;
	
	private OnRequestTokenFailedListener listener;
	
	
	/**
	 * 
	 */
	public OAuthRequestTokenTask(Context con, TwitterStore data, OnRequestTokenFailedListener listener) {
		context = con;
		dataStore = data;
		this.listener = listener;
		
		httpOauthConsumer = new CommonsHttpOAuthConsumer(dataStore.mConsumerKey, dataStore.mConsumerSecretKey);
		httpOauthprovider = new CommonsHttpOAuthProvider(TwitterConstant.REQUEST_URL, TwitterConstant.ACCESS_URL, TwitterConstant.AUTHORIZE_URL);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(Void... params) {
		try {
			String callBackStr = dataStore.mScheme + "://" + dataStore.mHost;
			System.out.println("===start requet token --- callback = " + callBackStr);
			System.out.println("consumer key = " + dataStore.mConsumerKey + " / secret key = " + dataStore.mConsumerSecretKey);
			urlBrowser = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, callBackStr);
			System.out.println("request complete ");
			System.out.println("start browser with url " + urlBrowser);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlBrowser)).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
			context.startActivity(intent);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("error when load request or open browser");
			
			cancel(true);
			if (listener != null) {
				listener.onFail();
			}
		}
		
		return null;
	}

	interface OnRequestTokenFailedListener {
		public void onFail();
	}
}
