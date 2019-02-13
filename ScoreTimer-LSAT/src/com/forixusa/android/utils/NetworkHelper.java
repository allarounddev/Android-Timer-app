package com.forixusa.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class NetworkHelper {

	 public static boolean isNetworkAvailable(Context context) {
		 
         boolean value = false;
         
//         WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//         try {
//        	 if (mWifiManager.isWifiEnabled() == false){
//            	 mWifiManager.setWifiEnabled(true);
//             }
//         } catch (Exception e) {
//        	 e.printStackTrace();
//         }
         
       //  ConnectivityManager mConnectivity;
         TelephonyManager mTelephony = (TelephonyManager) context
         					.getSystemService(Context.TELEPHONY_SERVICE);;
         
         ConnectivityManager manager = (ConnectivityManager) context
                          .getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo info = manager.getActiveNetworkInfo();
         if (info == null)
        	 return false;
         
      // Only update if WiFi or 3G is connected and not roaming
//         int netType = info.getType();
//         int netSubtype = info.getSubtype();
//         if (netType == ConnectivityManager.TYPE_WIFI) {
//        	 value = info.isConnected();
//         } else if (netType == ConnectivityManager.TYPE_MOBILE
//             && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
//             && !mTelephony.isNetworkRoaming()) {
//        	 value =  info.isConnected();
//         } else {
//        	 value =  false;
//         }

         
         if (info != null && info.isConnected()) {
        	 value = true;
         }
         
         return value;
	 }
	 
}
