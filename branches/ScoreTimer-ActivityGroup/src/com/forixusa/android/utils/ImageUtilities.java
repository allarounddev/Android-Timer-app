package com.forixusa.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class ImageUtilities {

	public static Bitmap decodeFile(String filePath)
			throws FileNotFoundException {
		final File file = new File(filePath);
		// Decode image size
		final BitmapFactory.Options option1 = new BitmapFactory.Options();
		option1.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(new FileInputStream(file), null, option1);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 100;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = option1.outWidth;
		int height_tmp = option1.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}

			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		final BitmapFactory.Options option2 = new BitmapFactory.Options();
		option2.inSampleSize = scale;
		return BitmapFactory.decodeStream(new FileInputStream(file), null,
				option2);

	}

	public static byte[] getBitmapToBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// Middle parameter is quality, but since PNG is lossless, it doesn't
		// matter
		bitmap.compress(CompressFormat.PNG, 0, outputStream);
		return outputStream.toByteArray();
	}
}
