package com.forixusa.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageLoader {
	// public static final int defaultImage = R.drawable.unavailable;
	private static final int REQUIRED_SIZE = 70;

	// the simplest in-memory cache implementation. This should be replaced with
	// something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
	private final HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();

	private File cacheDir;

	private static ImageLoader instance = null;

	private static ProgressBar mPrgBarLoading = null;

	public static ImageLoader getInstance(Context context, ProgressBar prgBarLoading) {
		if (instance == null) {
			instance = new ImageLoader(context);
		}

		mPrgBarLoading = prgBarLoading;

		return instance;
	}

	private ImageLoader(Context context) {
		// Make the background thead low priority. This way it will not affect
		// the UI performance
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

		// Find the dir to save cached images
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(Environment.getExternalStorageDirectory(), "KichAxe");
		} else {
			cacheDir = context.getCacheDir();
		}

		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	public void displayImage(String url, ImageView imageView) {
		if (cache.get(url) != null) {
			mPrgBarLoading.setVisibility(View.GONE);
			imageView.setImageBitmap(cache.get(url));
		} else {
			queuePhoto(url, imageView);
			// imageView.setImageResource(defaultImage);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		// This ImageView may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		photosQueue.clean(imageView);
		final PhotoToLoad p = new PhotoToLoad(url, imageView);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.add(p);
			photosQueue.photosToLoad.notifyAll();
		}

		// start thread if it's not started yet
		if (photoLoaderThread.getState() == Thread.State.NEW) {
			photoLoaderThread.start();
		}
	}

	private Bitmap getBitmap(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		final String filename = String.valueOf(url.hashCode());
		final File file = new File(cacheDir, filename);

		// from SD cache
		final Bitmap b = decodeFile(file);
		if (b != null) {
			return b;
		}

		// from web
		try {
			InputStream inStream = null;
			HttpURLConnection _conn = null;
			Bitmap bitmap = null;

			try {
				final String usernamePassword = "demo:demo";
				final byte[] authentication = Base64.encodeBytesToBytes(usernamePassword.getBytes());

				_conn = (HttpURLConnection) new URL(url).openConnection();
				_conn.setRequestProperty("Authorization", "Basic " + new String(authentication));
				_conn.setDoInput(true);
				_conn.connect();
				inStream = _conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(inStream);
				inStream.close();
				_conn.disconnect();
				inStream = null;
				_conn = null;
			} catch (final Exception ex) {
				// nothing
			}
			if (inStream != null) {
				try {
					inStream.close();
				} catch (final Exception ex) {
				}
			}
			if (_conn != null) {
				_conn.disconnect();
			}

			inStream = null;
			_conn = null;

			return bitmap;
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			final byte[] bytes = new byte[buffer_size];
			for (;;) {
				final int count = is.read(bytes, 0, buffer_size);
				if (count == -1) {
					break;
				}
				os.write(bytes, 0, count);
			}
		} catch (final Exception ex) {
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			final BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
					break;
				}
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			final BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (final FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		photoLoaderThread.interrupt();
		cache.clear();
	}

	// stores list of photos to download
	class PhotosQueue {
		private final List<PhotoToLoad> photosToLoad = new ArrayList<PhotoToLoad>();

		// removes all instances of this ImageView
		public void clean(ImageView image) {
			synchronized (photosToLoad) {
				for (int j = 0; j < photosToLoad.size();) {
					if (photosToLoad.get(j).imageView == image) {
						photosToLoad.remove(j);
					} else {
						++j;
					}
				}
			}
		}
	}

	class PhotosLoader extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0) {
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					} else {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.remove(0);
						}

						final Bitmap bitmap = getBitmap(photoToLoad.url);
						if (bitmap != null) {
							cache.put(photoToLoad.url, bitmap);
						}
						final Object tag = photoToLoad.imageView.getTag();
						if (tag != null && ((String) tag).equals(photoToLoad.url)) {
							final BitmapDisplayer bd = new BitmapDisplayer(bitmap, photoToLoad.imageView);
							final Activity a = (Activity) photoToLoad.imageView.getContext();
							a.runOnUiThread(bd);
						}
					}
					if (Thread.interrupted()) {
						break;
					}
				}
			} catch (final InterruptedException e) {
				// allow thread to exit
			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;

		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		@Override
		public void run() {
			if (bitmap != null) {
				mPrgBarLoading.setVisibility(View.GONE);
				imageView.setImageBitmap(bitmap);
			} else {
				// imageView.setImageResource(defaultImage);
			}
		}
	}

	public void clearCache() {
		// clear memory cache
		cache.clear();

		// clear SD cache
		final File[] files = cacheDir.listFiles();
		for (final File f : files) {
			f.delete();
		}
	}

}
