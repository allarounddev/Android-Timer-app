package com.forixusa.android.twitter;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.forixusa.android.twitter.OAuthReceiveTokenTask.OAuthReceiveDelegate;
import com.forixusa.android.twitter.OAuthRequestTokenTask.OnRequestTokenFailedListener;

/**
 * 
 * for authenticate twitter and supply api for user
 * 
 * use : create activity A that will use TwitterUtil
 * 
 * In AndroidManifest.xml must specify <uses-permission
 * android:name="android.permission.INTERNET" /> <uses-permission
 * android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 
 * In AndroidManifest.xml in activity tag of A, must specify intent-catogery
 * <intent-filter> <action android:name="android.intent.action.VIEW" />
 * <category android:name="android.intent.category.DEFAULT" /> <category
 * android:name="android.intent.category.BROWSABLE" /> <data
 * android:scheme="myapp" android:host="mainactivity" /> </intent-filter> scheme
 * and host may be defend on user, but remember these value for late use and
 * specify in tag of A : android:launchMode="singleTask"
 * 
 * In createView of A, must do task: +create TwitterUtil with context and
 * delegate when authenticate complete +specify consumerkey and scret consumer
 * key by setSecretKeyForConsumerKey(consumerkey, secretConsumerKey) +specify
 * scheme and host by setSchemeAndHost(sheme, host). scheme and host must map
 * with scheme and host of tag of activity +call authenticate with
 * doAuthenticateRequest() +in onNewIntent function, get uri of data, and pass
 * this data to doAuthenticatReceiveWithURI function(note that, this function
 * will call by browser when authenticate request complete)
 * 
 * In delegate of TwitterUtil, there is one function for hook when request token
 * and token secret complete. +authenticateComplete(TwitterStore data) is called
 * when request token and token secret complete. In this function we can call
 * any action from TwitterUtil that send tweet, get account setting, vvv....
 * 
 */
public class TwitterUtil implements OAuthReceiveDelegate {

	// constant for twitter
	public class TwitterConstant {
		public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
		public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
		public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

		// constant
		public static final String CONSUMER_KEY = "consumer_key";
		public static final String CONSUMER_SECRET = "consumer_secret";

		public static final String OAUTH_TOKEN = "oauth_token";
		public static final String OAUTH_VERIFY = "oauth_verify";
		public static final String TOKEN_STR = "token";
		public static final String TOKEN_SECRET_STR = "token_secret";
	}

	public static class TwitterStore {
		// consumer key and consumer secret
		public String mConsumerKey = "";
		public String mConsumerSecretKey = "";

		// scheme and host
		public String mScheme = "";
		public String mHost = "";

		public String mToken = "";
		public String mTokenSecret = "";

		public String oauth_token = "";
		public String oauth_verify = "";

		public Bundle doStore() {
			final Bundle bun = new Bundle();

			bun.putString(TwitterConstant.CONSUMER_KEY, this.mConsumerKey);
			bun.putString(TwitterConstant.CONSUMER_SECRET, this.mConsumerSecretKey);

			bun.putString(TwitterConstant.OAUTH_TOKEN, this.oauth_token);
			bun.putString(TwitterConstant.OAUTH_VERIFY, this.oauth_verify);

			bun.putString(TwitterConstant.TOKEN_STR, this.mToken);
			bun.putString(TwitterConstant.TOKEN_SECRET_STR, this.mTokenSecret);

			return bun;
		}

		public void copyFrom(TwitterStore source) {
			this.mConsumerKey = source.mConsumerKey;
			this.mConsumerSecretKey = source.mConsumerSecretKey;

			this.mScheme = source.mScheme;
			this.mHost = source.mHost;

			this.mToken = source.mToken;
			this.mTokenSecret = source.mTokenSecret;

			this.oauth_token = source.oauth_token;
			this.oauth_verify = source.oauth_verify;
		}
	}

	public interface TwitterDelegate {
		public void authenticateComplete(TwitterStore data);

		public void authenticateFault();
	}

	protected CommonsHttpOAuthConsumer httpOauthConsumer;
	protected CommonsHttpOAuthProvider httpOauthprovider;

	private final TwitterStore twitterDataObject;

	private final Context m_context;

	private final TwitterDelegate delegate;

	/**
	 * 
	 */
	public TwitterUtil(Context context, TwitterDelegate delegate) {
		// TODO Auto-generated constructor stub
		twitterDataObject = new TwitterStore();
		m_context = context;
		this.delegate = delegate;
	}

	// init
	private void init() {
		httpOauthConsumer = new CommonsHttpOAuthConsumer(this.twitterDataObject.mConsumerKey,
				this.twitterDataObject.mConsumerSecretKey);
		httpOauthprovider = new CommonsHttpOAuthProvider(TwitterConstant.REQUEST_URL, TwitterConstant.ACCESS_URL,
				TwitterConstant.AUTHORIZE_URL);
	}

	// set data
	public void setSecretKeyForConsumerKey(String consumerKey, String secretKey) {
		this.twitterDataObject.mConsumerKey = consumerKey;
		this.twitterDataObject.mConsumerSecretKey = secretKey;
	}

	public void setSchemeAndHost(String sheme, String host) {
		this.twitterDataObject.mScheme = sheme;
		this.twitterDataObject.mHost = host;
	}

	// end set data

	// oauthenticate
	public void doAuthenticateRequest() {

		if (this.twitterDataObject == null || this.twitterDataObject.mScheme == ""
				|| this.twitterDataObject.mHost == "" || this.twitterDataObject.mConsumerKey == ""
				|| this.twitterDataObject.mConsumerSecretKey == "") {
			System.out
					.println("please provide enough infomation >>> schemeStr / hostStr / consumerKey / consumerSecretKey");
			return;
		}

		init();

		new OAuthRequestTokenTask(m_context, twitterDataObject, new OnRequestTokenFailedListener() {

			@Override
			public void onFail() {
				// close Loading dialog when request token is failed
				final TwitterActivity twitterActivity = (TwitterActivity) m_context;
				twitterActivity.finish();
			}
		}).execute();
	}

	public void doAuthenticatReceive(String oauth_token, String oauth_verify) {
		if (this.twitterDataObject == null || this.twitterDataObject.mScheme == ""
				|| this.twitterDataObject.mHost == "" || this.twitterDataObject.mConsumerKey == ""
				|| this.twitterDataObject.mConsumerSecretKey == "") {
			System.out
					.println("please provide enough infomation >>> schemeStr / hostStr / consumerKey / consumerSecretKey");
			return;
		}

		new OAuthReceiveTokenTask(twitterDataObject, this).execute(oauth_token, oauth_verify);
	}

	public void doAuthenticatReceiveWithURI(Uri uri) {
		if (this.twitterDataObject == null || this.twitterDataObject.mScheme.equals("")
				|| this.twitterDataObject.mConsumerKey.equals("")
				|| this.twitterDataObject.mConsumerSecretKey.equals("")) {
			System.out
					.println("please provide enough infomation >>> schemeStr / hostStr / consumerKey / consumerSecretKey");
			return;
		}

		if (uri != null && uri.getScheme().equals(this.twitterDataObject.mScheme)) {
			final String oauth_verify = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
			final String oauth_token = uri.getQueryParameter(OAuth.OAUTH_TOKEN);

			doAuthenticatReceive(oauth_token, oauth_verify);
		}
	}

	// end oauthenticate

	// api to twitter
	public boolean isAuthenticated(final TwitterStore obj) {
		final String token = obj.mToken;
		final String secret = obj.mTokenSecret;

		System.out.println(" token " + token + " / token screte " + secret);

		final twitter4j.auth.AccessToken a = new twitter4j.auth.AccessToken(token, secret);
		final Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(obj.mConsumerKey, obj.mConsumerSecretKey);
		twitter.setOAuthAccessToken(a);

		try {
			twitter.getAccountSettings();
			if (twitter.verifyCredentials() != null) {
				// Authentication.sTwitterUsername =
				// twitter.verifyCredentials().getScreenName();
			}

			return true;
		} catch (final TwitterException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sendTweet(TwitterStore obj, String msg) throws Exception {
		final String token = obj.mToken;
		final String secret = obj.mTokenSecret;

		final twitter4j.auth.AccessToken a = new twitter4j.auth.AccessToken(token, secret);
		final Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(obj.mConsumerKey, obj.mConsumerSecretKey);
		twitter.setOAuthAccessToken(a);

		twitter.updateStatus(msg);
	}

	/*
	 * delegate for oauth receive
	 * 
	 * @see com.android.library.app.twitter.oauth.OAuthReceiveTokenTask.
	 * OAuthReceiveDelegate
	 * #oauthReceiveComplete(com.android.library.app.twitter.
	 * TwitterUtil.TwitterStore)
	 */

	@Override
	public void oauthReceiveComplete(TwitterStore dataStore) {
		// TODO Auto-generated method stub
		if (delegate != null) {
			delegate.authenticateComplete(dataStore);
		}
	}

	@Override
	public void oauthReceiveFault() {
		// TODO Auto-generated method stub
		if (delegate != null) {
			delegate.authenticateFault();
		}
	}

}
