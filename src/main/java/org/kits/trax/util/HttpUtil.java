package org.kits.trax.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "resource", "deprecation" })
public final class HttpUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpUtil.class);

	public static HttpResponse get(String url) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("GET " + url);
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader(new BasicHeader("Accept", "application/json"));
		HttpResponse response = null;
		try {
			response = httpClient.execute(getRequest);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		} finally {
			getRequest.releaseConnection();
		}

		return response;
	}

	public static HttpResponse post(String url) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("POST " + url);
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);
		postRequest.setHeader(new BasicHeader("Accept", "application/json"));
		HttpResponse response = null;

		try {
			response = httpClient.execute(postRequest);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			postRequest.releaseConnection();
		}

		return response;
	}

	public static HttpResponse post(String url, String jsonData) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("POST " + url);
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);
		postRequest.setHeader(new BasicHeader("Accept", "application/json"));
		HttpResponse response = null;

		try {
			postRequest.setEntity(new StringEntity(jsonData));
			response = httpClient.execute(postRequest);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			postRequest.releaseConnection();
		}

		return response;
	}

	public static HttpResponse put(String url, String jsonData) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("PUT " + url);
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpPut putRequest = new HttpPut(url);
		putRequest.setHeader(new BasicHeader("Accept", "application/json"));
		HttpResponse response = null;

		try {
			putRequest.setEntity(new StringEntity(jsonData));
			response = httpClient.execute(putRequest);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			putRequest.releaseConnection();
		}

		return response;
	}

	public static HttpResponse delete(String url) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("DELETE " + url);
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete putRequest = new HttpDelete(url);
		putRequest.setHeader(new BasicHeader("Accept", "application/json"));
		HttpResponse response = null;

		try {
			response = httpClient.execute(putRequest);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			putRequest.releaseConnection();
		}

		return response;
	}
}
