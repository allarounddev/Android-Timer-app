/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.forixusa.android.facebookconnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.facebook.android.Facebook;

public class SessionStore {
	public static final String TAG = SessionStore.class.getSimpleName();
    
    private static final String TOKEN = "access_token";
    private static final String EXPIRES = "expires_in";
    private static final String KEY = "facebook-session";
    
    public static boolean save(Facebook session, Context context) {
    	Log.i(TAG, "SessionStore.save()");
    	
        Editor editor =
            context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, session.getAccessToken());
        editor.putLong(EXPIRES, session.getAccessExpires());
        return editor.commit();
    }

    public static boolean restore(Facebook session, Context context) {
    	Log.i(TAG, "SessionStore.restore()");
    	
        SharedPreferences savedSession =
            context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        session.setAccessToken(savedSession.getString(TOKEN, null));
        session.setAccessExpires(savedSession.getLong(EXPIRES, 0));
        return session.isSessionValid();
    }

    public static void clear(Context context) {
    	Log.i(TAG, "SessionStore.clear()");
    	
        Editor editor = 
            context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        
    }
    
    public static class FacebookShare {
    	private static final String TOKEN = "fb_share_access_token";
        private static final String EXPIRES = "fb_share_expires_in";
    	
    	public static boolean save(Facebook session, Context context) {
        	Log.i(TAG, "SessionStore.FacebookShare.save()");
        	
            Editor editor =
                context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
            editor.putString(TOKEN, session.getAccessToken());
            editor.putLong(EXPIRES, session.getAccessExpires());
            return editor.commit();
        }

        public static boolean restore(Facebook session, Context context) {
        	Log.i(TAG, "SessionStore.FacebookShare.restore()");
        	
            SharedPreferences savedSession =
                context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
            session.setAccessToken(savedSession.getString(TOKEN, null));
            session.setAccessExpires(savedSession.getLong(EXPIRES, 0));
            return session.isSessionValid();
        }

        public static void clear(Context context) {
        	Log.i(TAG, "SessionStore.FacebookShare.clear()");
        	
            Editor editor = 
                context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            
        }
    }
}
