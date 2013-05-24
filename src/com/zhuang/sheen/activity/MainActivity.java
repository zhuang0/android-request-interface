package com.zhuang.sheen.activity;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.zhuang.sheen.requestinterface.AsyncImageLoader;
import com.zhuang.sheen.requestinterface.AsyncImageLoader.ImageCallback;
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

		// url = "http://www.baidu.com";
		url = "http://www.webservicex.net/globalweather.asmx/GetWeather";
		requestInterface.addParameter("CityName", "beijing");
		requestInterface.addParameter("CountryName", "china");
		imageUrl = "http://photocdn.sohu.com/20130227/Img367284140.jpg";

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
						//若返回json：
						/*try {
							JSONObject object = new JSONObject(response);
							String value = object.getString("key");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/

					}
				});

			}
		});

		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				asyncImageLoader.asyncLoadBitmap(imageUrl, imageView, imageUrl, new ImageCallback() {

					@Override
					public void imageLoaded(Bitmap bitmap, ImageView imageView, String tag) {
						// TODO Auto-generated method stub
						asyncImageLoader.showImg(imageView, bitmap, getApplicationContext());
					}
				});
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
