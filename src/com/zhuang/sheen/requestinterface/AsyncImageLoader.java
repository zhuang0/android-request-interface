package com.zhuang.sheen.requestinterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class AsyncImageLoader {

	// SoftReference是软引用，是为了更好的为了系统回收变量
	private HashMap<String, SoftReference<Bitmap>> imageCache;
	private Context context;

	// private ExecutorService executorService =
	// Executors.newFixedThreadPool(25); // 固定线程数

	public AsyncImageLoader(Context context) {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
		this.context = context;
	}

	public Bitmap asyncLoadBitmap(final String imageUrl, final ImageView imageView, final String tag,
			final ImageCallback imageCallback) {
		// resetPurgeTimer();
		if (imageCache.containsKey(imageUrl)) {
			// 从缓存中获取
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			Bitmap bitmap = softReference.get();
			if (bitmap != null) {
				return bitmap;
			}
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageView, tag);
			}
		};
		// 建立新一个新的线程下载图片
		new Thread() {

			@Override
			public void run() {
				Bitmap bitmap;
				try {
					bitmap = loadBitmapFromUrl(imageUrl);
					imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
					Message message = handler.obtainMessage(0, bitmap);
					handler.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					sendMsg(0);
					e.printStackTrace();
				}

			}
		}.start();
		return null;
	}

	public Bitmap loadBitmapFromUrl(String url) throws IOException {

		Bitmap bitmap = null;
		for (int reTry = 0; reTry < 2; reTry++) {

			try {
				URL myURL;
				myURL = new URL(url);
				URLConnection conn = myURL.openConnection();
				HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;
				httpUrlConnection.setDoInput(true);
				httpUrlConnection.setConnectTimeout(5000);
				httpUrlConnection.setRequestMethod("GET");
				httpUrlConnection.connect();

				if (httpUrlConnection.getResponseCode() == -1) {

					httpUrlConnection.disconnect();
					if (reTry == 0) {
						continue;
					}

				} else {

					InputStream is = httpUrlConnection.getInputStream();

					if (is == null) {

						throw new RuntimeException("stream is null");
					}

					bitmap = BitmapFactory.decodeStream(is);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sendMsg(0);
			}

		}
		sendMsg(1);
		return bitmap;

	}

	public void savePNG(final Bitmap bitmap, final String path) {

		File file = new File(path);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 加载本地图片
	 * 
	 * @param path
	 * @return
	 */
	public Bitmap getLoacalBitmap(String path) {
		try {
			FileInputStream fis = new FileInputStream(path);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 显示大图的方法
	public void showImg(ImageView view, Bitmap img, Context context) {

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int w = img.getWidth();
		int h = img.getHeight();

		if (w > width) {
			int hh = width * h / w;
			LayoutParams para = view.getLayoutParams();
			para.width = width;
			para.height = hh;
			view.setLayoutParams(para);
		}
		view.setImageBitmap(img);

	}

	// 回调接口
	public interface ImageCallback {
		public void imageLoaded(Bitmap bitmap, ImageView imageView, String tag);
	}

	private Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
					case 0 :
						msgHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(context, "下载出错", Toast.LENGTH_SHORT).show();
							}
						});

						break;
					case 1 :
						msgHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
							}
						});

						break;

				}
			}
			super.handleMessage(msg);
		}
	};

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		msgHandler.dispatchMessage(msg);

	}
}
