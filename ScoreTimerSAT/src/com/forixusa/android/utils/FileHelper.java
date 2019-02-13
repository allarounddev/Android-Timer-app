package com.forixusa.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import android.content.Context;

public class FileHelper {

	public static String readFromFile(Context context, int resourceId) {
		final InputStream is = context.getResources().openRawResource(resourceId);

		String content = "";
		if (is != null) {
			final Writer writer = new StringWriter();
			final char[] buffer = new char[1024];
			Reader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (final UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			content = writer.toString();
		}
		return content;
	}
}
