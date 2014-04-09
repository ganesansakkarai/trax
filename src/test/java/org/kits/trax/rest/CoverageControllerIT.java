package org.kits.trax.rest;

import java.io.IOException;
import java.util.List;

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
	
	@Test
	public void create() throws IllegalStateException, IOException {

		Application application = DataUtil.build();
		String jsonData = JsonUtil.toJson(application);
		HttpResponse response = HttpUtil.post(url + "coverage/create", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		LOGGER.info("Response: " + jsonData);
		application = JsonUtil.fromJson(Application.class, jsonData);
		Assert.assertNotNull(application);
	}
	
	@Test(dataProvider="data")
	public void listApplications(Application expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "applications/" + expected.getTestType().name() + "/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<String> data = JsonUtil.fromJsonArray(String.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}
	
	@Test(dataProvider="data")
	public void listBuilds(Application expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "builds/" + expected.getName() + "/" + expected.getTestType().name() + "/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<String> data = JsonUtil.fromJsonArray(String.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}	

	@Test(dataProvider="data")
	public void coverageSummary(Application expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "coverage/summary/" + expected.getName() + "/" + expected.getTestType().name() + "/" + DateUtil.toString(expected.getTimeStamp()));
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}

	@Test(dataProvider="data")
	public void findApplication(Application expected) throws IllegalStateException, IOException {

		HttpResponse response = HttpUtil.post(url + "coverage/" + expected.getName() + "/" + expected.getTestType().name() + "/" + DateUtil.toString(expected.getTimeStamp()));
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}
	
	@Test(dataProvider="data")
	public void delete(Application expected) {

		HttpResponse response = HttpUtil.post(url + "/coverage/delete/" + expected.getName() + "/" + expected.getTestType().name() + "/" + DateUtil.toString(expected.getTimeStamp()) + "/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
	}
}
