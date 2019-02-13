package com.forixusa.android.facebookconnection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.forixusa.android.utils.GlobalConstants;

public class FacebookLogoutHelper {
	public static final String TAG = FacebookLogoutHelper.class.getSimpleName();

	public static void logout(final Context context) {
		final Facebook mFacebook = new Facebook(GlobalConstants.FACEBOOK_APP_ID);
		final AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, context);

		mAsyncRunner.logout(context, new RequestListener() {

			@Override
			public void onComplete(String response, Object state) {
				Log.i(TAG, "logout success");
				SessionEvents.clearListeners();
				SessionStore.clear(context);
			}

			@Override
			public void onIOException(IOException e, Object state) {
				logoutFacebookWhenException(context, mFacebook);
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
				logoutFacebookWhenException(context, mFacebook);
			}

			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
				logoutFacebookWhenException(context, mFacebook);
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				logoutFacebookWhenException(context, mFacebook);
			}
		});
	}

	public static void logout(final Context context, final FacebookLogoutCompleteListener listener) {
		final Facebook mFacebook = new Facebook(GlobalConstants.FACEBOOK_APP_ID);
		final AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, context);

		mAsyncRunner.logout(context, new RequestListener() {

			@Override
			public void onComplete(String response, Object state) {
				Log.i(TAG, "logout success");
				SessionEvents.clearListeners();
				SessionStore.clear(context);

				listener.onLogoutComplete();
			}

			@Override
			public void onIOException(IOException e, Object state) {
				logoutFacebookWhenException(context, mFacebook);
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {
				logoutFacebookWhenException(context, mFacebook);
			}

			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {
				logoutFacebookWhenException(context, mFacebook);
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				logoutFacebookWhenException(context, mFacebook);
			}
		});
	}

	private static void logoutFacebookWhenException(final Context context, final Facebook mFacebook) {
		Log.i(TAG, "logout when exception");
		Util.clearCookies(context);
		mFacebook.setAccessToken(null);
		mFacebook.setAccessExpires(0);
		SessionStore.clear(context);
	}
}
