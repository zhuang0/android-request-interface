package com.zhuang.sheen.activity;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhuang.sheen.requestinterface.AsyncImageLoader;
import com.zhuang.sheen.requestinterface.R;
import com.zhuang.sheen.requestinterface.RequestException;
import com.zhuang.sheen.requestinterface.RequestInterface;
import com.zhuang.sheen.requestinterface.RequestListener;

public class MainActivity extends Activity {

	String nameString;
	String url;
	String imageUrl;
	Handler handler;
	TextView textView;
	ImageView imageView;
	RequestInterface requestInterface;
	AsyncImageLoader asyncImageLoader;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new Handler();
		textView = (TextView) findViewById(R.id.textView1);
		imageView = (ImageView) findViewById(R.id.imageView1);
		Button button = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		requestInterface = new RequestInterface();
		asyncImageLoader = new AsyncImageLoader(getApplicationContext());

		url = "http://www.webservicex.net/globalweather.asmx/GetWeather";
		requestInterface.addParameter("CityName", "beijing");
		requestInterface.addParameter("CountryName", "china");
		imageUrl = "http://www.sucaitianxia.com/png/UploadFiles_6130/200803/20080321003432189.png";

		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() // Not
																				// necessary
																				// in
																				// common
				.build();
		imageLoader.init(config);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				requestInterface.request(url, new RequestListener() {

					@Override
					public void onIOException(IOException e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError(RequestException e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onComplete(String response) {
						// TODO Auto-generated method stub
						nameString = response;
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								textView.setText(nameString);
							}
						});
						// 若返回json：
						/*
						 * try { JSONObject object = new JSONObject(response);
						 * String value = object.getString("key"); } catch
						 * (JSONException e) { // TODO Auto-generated catch
						 * block e.printStackTrace(); }
						 */

					}
				});

			}
		});

		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				imageLoader.displayImage(imageUrl, imageView, options);
				/*
				 * asyncImageLoader.asyncLoadBitmap(imageUrl, imageView,
				 * imageUrl, new ImageCallback() {
				 * 
				 * @Override public void imageLoaded(Bitmap bitmap, ImageView
				 * imageView, String tag) { // TODO Auto-generated method stub
				 * asyncImageLoader.showImg(imageView, bitmap,
				 * getApplicationContext()); } });
				 */
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
