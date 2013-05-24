package com.zhuang.sheen.requestinterface;

public class RequestInterface {

	/**
	 * get请求方式
	 */
	public static final String HTTPMETHOD_GET = "GET";

	private RequestParameters params = new RequestParameters();

	public void addParameter(String key, String parameter) {
		this.params.add(key, parameter);
	}

	public void request(final String url, final RequestListener listener) {
		AsyncRequestRunner asyncRequestRunner = new AsyncRequestRunner();
		asyncRequestRunner.request(url, this.params, HTTPMETHOD_GET, listener);
	}

}
