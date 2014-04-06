package org.kits.trax.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.kits.trax.domain.Application;
import org.kits.trax.util.DataUtil;
import org.kits.trax.util.DateUtil;
import org.kits.trax.util.HttpUtil;
import org.kits.trax.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CoverageControllerIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoverageControllerIT.class);
	private static final String url = "http://localhost:8080/";

	@DataProvider(name = "data")
	public static Object[][] data() throws IllegalStateException, IOException {

		Application application = DataUtil.build();
		String jsonData = JsonUtil.toJson(application);
		HttpResponse response = HttpUtil.post(url + "coverage/create", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		LOGGER.info("Response: " + jsonData);
		application = JsonUtil.fromJson(Application.class, jsonData);
		return new Object[][] { { application } };
	}

	@Test(dataProvider="data")
	public void findByTimeStamp(Application expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "coverage/" + expected.getTestType().name() + "/" + DateUtil.toString(expected.getTimeStamp()));
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
		System.out.println(jsonData);
	}

	@Test(dataProvider="data")
	public void list(Application expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "coverages/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		@SuppressWarnings("unchecked")
		Map<String, Object> data = JsonUtil.fromJson(HashMap.class, jsonData);
		Assert.assertNotNull(data.get("sEcho"));
		Assert.assertTrue(Integer.parseInt(data.get("iTotalRecords").toString()) > 0);
		Assert.assertTrue(Integer.parseInt(data.get("iTotalDisplayRecords").toString()) > 0);
		Assert.assertNotNull(data.get("aoData"));
	}
	
	@Test(dataProvider="data")
	public void listTimeStamps(Application expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "builds/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<String> data = JsonUtil.fromJsonArray(String.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}
	
	@Test(dataProvider="data")
	public void listByTimeStamp(Application expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "/coverage/Unit/" + DateUtil.toString(expected.getTimeStamp()));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		@SuppressWarnings("unchecked")
		Map<String, Object> data = JsonUtil.fromJson(HashMap.class, jsonData);
		Assert.assertNotNull(data.get("sEcho"));
		Assert.assertTrue(Integer.parseInt(data.get("iTotalRecords").toString()) > 0);
		Assert.assertTrue(Integer.parseInt(data.get("iTotalDisplayRecords").toString()) > 0);
		Assert.assertNotNull(data.get("aoData"));
	}
	
	@Test(dataProvider="data")
	public void delete(Application expected) {

		HttpResponse response = HttpUtil.post(url + "/coverage/Unit/delete/" + DateUtil.toString(expected.getTimeStamp()));
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
	}
}
