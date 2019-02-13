/**
 * 
 */
package com.forixusa.android.twitter;

import com.forixusa.android.twitter.TwitterUtil.TwitterConstant;
import com.forixusa.android.twitter.TwitterUtil.TwitterStore;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.os.AsyncTask;

public class OAuthReceiveTokenTask extends AsyncTask<String, Void, Void> {

	public interface OAuthReceiveDelegate {
		public void oauthReceiveComplete(TwitterStore dataStore);
		public void oauthReceiveFault();
	}
	
	private CommonsHttpOAuthConsumer httpOauthConsumer;
    private CommonsHttpOAuthProvider httpOauthprovider;
	
	private TwitterStore dataStore;
	private OAuthReceiveDelegate delegate;
	/**
	 * 
	 */
	public OAuthReceiveTokenTask(TwitterStore dataStore, OAuthReceiveDelegate delegate) {
		this.dataStore = dataStore;
		this.delegate = delegate;
		
		httpOauthConsumer = new CommonsHttpOAuthConsumer(dataStore.mConsumerKey, dataStore.mConsumerSecretKey);
		httpOauthprovider = new CommonsHttpOAuthProvider(TwitterConstant.REQUEST_URL, TwitterConstant.ACCESS_URL, TwitterConstant.AUTHORIZE_URL);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(String... params) {
		String oauth_verify = params[1];
		String oauth_token = params[0];
		dataStore.oauth_verify = oauth_verify;
		dataStore.oauth_token = oauth_token;
		httpOauthConsumer.setTokenWithSecret(dataStore.oauth_token, "");
		
		try {
			httpOauthprovider.retrieveAccessToken(httpOauthConsumer, dataStore.oauth_verify);
			System.out.println("=== token = " + httpOauthConsumer.getToken() + " /  scre = " + httpOauthConsumer.getTokenSecret());
			
			dataStore.mToken = httpOauthConsumer.getToken();
			dataStore.mTokenSecret = httpOauthConsumer.getTokenSecret();
			
		} catch(Exception e) {
			System.out.println("error when request");
			e.printStackTrace();
			
			this.delegate.oauthReceiveFault();
		}
		
		System.out.println("get token is ok");
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		//post on main thread
		super.onPostExecute(result);
		
		if(delegate!=null) {
			delegate.oauthReceiveComplete(this.dataStore);
		}
	}

}
