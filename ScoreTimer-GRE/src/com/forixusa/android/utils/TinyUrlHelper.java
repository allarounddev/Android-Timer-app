package com.forixusa.android.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.forixusa.android.http.CustomHttpClient;

public class TinyUrlHelper {

	public static String getTinyUrl(String fullUrl) throws HttpException, IOException, URISyntaxException {
		final ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>(1);
		queryParams.add(new BasicNameValuePair("url", fullUrl));
		return CustomHttpClient.executeHttpGet("http://tinyurl.com/api-create.php", new ArrayList<NameValuePair>(),
				queryParams);
	}
}
