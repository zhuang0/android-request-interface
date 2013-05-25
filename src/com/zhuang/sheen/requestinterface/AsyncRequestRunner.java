package com.zhuang.sheen.requestinterface;

import android.app.Activity;
import android.content.Intent;

class AsyncRequestRunner extends Activity {

	private static final String ACTION_NAME = "com.zhuang.sheen.action.BROADCAST";

	/**
	 * 
	 * @param url
	 *            服务器地�?
	 * @param params
	 *            存放参数的容�?
	 * @param httpMethod
	 *            "GET"or“POST�?
	 * @param listener
	 *            回调对象
	 */
	private String response;
	public void request(final String url, final RequestParameters params, final String httpMethod,
			final RequestListener listener) {

		new Thread() {
			@Override
			public void run() {
				try {
					response = HttpManager.openUrl(url, httpMethod, params);
					listener.onComplete(response);

				} catch (RequestException e) {
					listener.onError(e);

				}

			}
		}.start();

	}

}
