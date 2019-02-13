package com.forixusa.android.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.forixusa.android.utils.Base64;

public final class CustomHttpClient {
	/** The time it takes for our client to timeout */
	public static final int HTTP_TIMEOUT = 15 * 1000; // milliseconds

	/** Single instance of our HttpClient */
	private static HttpClient mHttpClient;
	public static final String TAG = CustomHttpClient.class.getSimpleName();

	/**
	 * Performs an HTTP GET request to the specified url.
	 * 
	 * @param url
	 *            The web address to post the request to
	 * @return The result of the request
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws Exception
	 */
	public static String executeHttpGet(String url, ArrayList<NameValuePair> headers,
			ArrayList<NameValuePair> queryParams) throws URISyntaxException, ClientProtocolException, IOException {

		BufferedReader in = null;
		try {
			// add parameters
			final StringBuffer buffer = new StringBuffer();
			if (queryParams != null && !queryParams.isEmpty()) {
				buffer.append("?");
				for (final NameValuePair p : queryParams) {
					final String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");

					if (buffer.toString().length() > 1) {
						buffer.append("&");
					}

					buffer.append(paramString);
				}

				url += buffer.toString();
			}

			final HttpClient client = getHttpClient();
			final HttpGet request = new HttpGet();
			request.setURI(new URI(url));

			// add User-Agents header
			if (headers == null) {
				headers = new ArrayList<NameValuePair>();
			}
			headers.add(new BasicNameValuePair("Content-Type", "application/xml"));

			// add headers
			for (final NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
				Log.d(h.getName(), h.getValue());
			}

			Log.d("URL", url);

			final HttpResponse response = client.execute(request);
			for (final Header h : response.getAllHeaders()) {
				Log.d(h.getName(), h.getValue());
			}

			final StringBuffer sb = new StringBuffer("");
			if (response.getEntity() != null) {
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				in.close();
			} else {
				return null;
			}
			final String result = sb.toString();
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String executeHttpPost(String url, ArrayList<NameValuePair> headers,
			ArrayList<NameValuePair> bodyParams) throws ClientProtocolException, IOException {
		BufferedReader in = null;
		try {
			final HttpClient client = getHttpClient();
			final HttpPost request = new HttpPost(url);

			if (headers == null) {
				headers = new ArrayList<NameValuePair>();
			}

			// add headers
			final String usernamePassword = "demo:demo";
			final byte[] authentication = Base64.encodeBytesToBytes(usernamePassword.getBytes());
			request.addHeader("Authorization", "Basic " + new String(authentication));
			headers.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));

			for (final NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			request.setEntity(new UrlEncodedFormEntity(bodyParams));

			final HttpContext context = new BasicHttpContext();
			final HttpResponse response = client.execute(request, context);
			for (final Header h : response.getAllHeaders()) {
				Log.d(h.getName(), h.getValue());
			}

			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			final StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();

			final String result = sb.toString();
			Log.d(TAG, "Content:" + result);
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String executeHttpPost(String url, ArrayList<NameValuePair> headers,
			ArrayList<NameValuePair> bodyParams, Bitmap photo) throws ClientProtocolException, IOException {
		BufferedReader in = null;
		try {
			final HttpClient client = getHttpClient();
			final HttpPost request = new HttpPost(url);

			if (headers == null) {
				headers = new ArrayList<NameValuePair>();
			}

			// add headers
			final String usernamePassword = "demo:demo";
			final byte[] authentication = Base64.encodeBytesToBytes(usernamePassword.getBytes());
			request.addHeader("Authorization", "Basic " + new String(authentication));
			for (final NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			final MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (final NameValuePair h : bodyParams) {
				entity.addPart(h.getName(), new StringBody(h.getValue()));
			}

			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			photo.compress(CompressFormat.JPEG, 100, bos);
			final byte[] data = bos.toByteArray();
			entity.addPart("photo", new ByteArrayBody(data, "image/jpeg", "myPhoto.jpg"));

			request.setEntity(entity);

			final HttpContext context = new BasicHttpContext();
			final HttpResponse response = client.execute(request, context);
			for (final Header h : response.getAllHeaders()) {
				Log.d(h.getName(), h.getValue());
			}

			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			final StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();

			final String result = sb.toString();
			Log.d("ESY", "Content:" + result);
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Performs an HTTP Post request to the specified url with the specified
	 * parameters.
	 * 
	 * @param url
	 *            The web address to post the request to
	 * @param postParameters
	 *            The parameters to send via the request
	 * @return The result of the request
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws Exception
	 */
	public static String executeHttpPost(String url, ArrayList<NameValuePair> headers, String jsonBody)
			throws ClientProtocolException, IOException {
		BufferedReader in = null;
		try {
			final HttpClient client = getHttpClient();
			final HttpPost request = new HttpPost(url);

			final StringEntity se = new StringEntity(jsonBody);
			request.setEntity(se);

			// add User-Agents header
			if (headers == null) {
				headers = new ArrayList<NameValuePair>();
			}

			// add headers
			for (final NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
				Log.d(h.getName(), h.getValue());
			}

			Log.d("PIRQ-PostBody", jsonBody.toString());

			final HttpResponse response = client.execute(request);
			for (final Header h : response.getAllHeaders()) {
				Log.d(h.getName(), h.getValue());
			}

			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			final StringBuffer sb = new StringBuffer("");
			if (response.getEntity() != null) {
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				in.close();
			} else {
				return null;
			}

			final String result = sb.toString();
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get our single instance of our HttpClient object.
	 * 
	 * @return an HttpClient object with connection parameters set
	 */
	private static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
			final ClientConnectionManager mgr = mHttpClient.getConnectionManager();
			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
			final ConnPerRoute connPerRoute = new ConnPerRouteBean(6);
			ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
			ConnManagerParams.setMaxTotalConnections(params, 20);

			mHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()),
					params);
		}

		return mHttpClient;
	}

}
