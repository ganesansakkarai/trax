package org.kits.trax.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.kits.trax.domain.Coverage;
import org.kits.trax.domain.TestType;
import org.kits.trax.util.DateUtil;
import org.kits.trax.util.HttpUtil;
import org.kits.trax.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CoverageControllerIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoverageControllerIT.class);
	private static final String url = "http://localhost:8080/";
	
	@AfterMethod
	public void clean() {
		HttpUtil.post(url + "/coverages/delete/");
	}

	@DataProvider(name = "bulkCreateData")
	public static Object[][] bulkCreateData() {

		List<Coverage> coverages = buildList();
		return new Object[][] { { coverages } };
	}
	
	@DataProvider(name = "findData")
	public static Object[][] findData() throws IllegalStateException, IOException {

		List<Coverage> coverages = buildList();
		String jsonData = JsonUtil.toJsonArray(coverages);
		HttpResponse response = HttpUtil.post(url + "/coverages/create", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		LOGGER.info("Response: " + jsonData);
		coverages = JsonUtil.fromJsonArray(Coverage.class, jsonData);
		return new Object[][] { { coverages.get(0) } };
	}
	
	@DataProvider(name = "listData")
	public static Object[][] listData() throws IllegalStateException, IOException {

		List<Coverage> coverages = buildList();
		String jsonData = JsonUtil.toJsonArray(coverages);
		HttpResponse response = HttpUtil.post(url + "/coverages/create", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		LOGGER.info("Response: " + jsonData);
		coverages = JsonUtil.fromJsonArray(Coverage.class, jsonData);
		return new Object[][] { { coverages } };
	}

	@Test(dataProvider = "bulkCreateData")
	public void bulkCreate(List<Coverage> coverages)
			throws IllegalStateException, IOException {

		String jsonData = JsonUtil.toJsonArray(coverages);
		HttpResponse response = HttpUtil.post(url + "/coverages/create", jsonData);
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		LOGGER.info("Response: " + jsonData);
		coverages = JsonUtil.fromJsonArray(Coverage.class, jsonData);
	}

	@Test(dataProvider="findData")
	public void find(Coverage expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "/coverage/" + expected.getId());
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Coverage actual = JsonUtil.fromJson(Coverage.class, jsonData);
		Assert.assertNotNull(actual);
		Assert.assertEquals(actual.getName(), expected.getName());
	}

	@Test(dataProvider="listData")
	public void list(List<Coverage> expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "/coverages/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		@SuppressWarnings("unchecked")
		Map<String, Object> data = JsonUtil.fromJson(HashMap.class, jsonData);
		Assert.assertEquals(Integer.parseInt(data.get("TotalRecordCount").toString()), expected.size());
		Assert.assertEquals(data.get("Result"), "OK");
		Assert.assertNotNull(data.get("Records"));
	}
	
	@Test(dataProvider="listData")
	public void listByTimeStamp(List<Coverage> expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "/coverages/" + DateUtil.toString(expected.get(0).getTimeStamp()));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		@SuppressWarnings("unchecked")
		Map<String, Object> data = JsonUtil.fromJson(HashMap.class, jsonData);
		Assert.assertEquals(Integer.parseInt(data.get("TotalRecordCount").toString()), expected.size());
		Assert.assertEquals(data.get("Result"), "OK");
		Assert.assertNotNull(data.get("Records"));
	}
	
	@Test(dataProvider="listData")
	public void bulkDelete(List<Coverage> expected) {

		HttpResponse response = HttpUtil.post(url + "/coverages/delete/" + DateUtil.toString(expected.get(0).getTimeStamp()));
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
	}

	private static List<Coverage> buildList() {

		List<Coverage> coverages = new ArrayList<Coverage>();
		for (int i = 0; i < 10; ++i) {
			Coverage coverage = new Coverage();
			coverage.setName("hello.Hello" + System.currentTimeMillis());
			coverage.setType(TestType.Unit);
			coverage.setTimeStamp(new Date());
			coverage.setLines(Math.round(100 * Math.random()));
			coverage.setMissedLines(Math.round(100 * Math.random()));
			coverage.setBranch(Math.round(100 * Math.random()));
			coverage.setMissedBranch(Math.round(100 * Math.random()));
			coverages.add(coverage);
		}

		return coverages;
	}
}
