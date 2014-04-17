package org.kits.trax.rest;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kits.trax.domain.Application;
import org.kits.trax.util.DataUtil;
import org.kits.trax.util.DateUtil;
import org.kits.trax.util.HttpUtil;
import org.kits.trax.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = org.kits.trax.Application.class)
@WebAppConfiguration
@IntegrationTest
@DirtiesContext
public class ApplicationControllerIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationControllerIT.class);
	private static final String url = "http://localhost:8080/";

	
	private Application buildApplication() throws Exception {

		Application application = DataUtil.build();
		String jsonData = JsonUtil.toJson(application);
		HttpResponse response = HttpUtil.post(url + "coverage/create", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		LOGGER.info("Response: " + jsonData);
		application = JsonUtil.fromJson(Application.class, jsonData);
		return application;
	}

	@Test
	public void create() throws Exception {

		Application application = DataUtil.build();
		String jsonData = JsonUtil.toJson(application);
		HttpResponse response = HttpUtil.post(url + "coverage/create", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
	}
	
	@Test
	public void listApplications() throws Exception {

		Application expected = buildApplication();
		HttpResponse response = HttpUtil.post(url + "applications/" + expected.getTestType().name() + "/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<String> data = JsonUtil.fromJsonArray(String.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}

	@Test
	public void listBuilds() throws Exception {

		Application expected = buildApplication();
		HttpResponse response = HttpUtil.post(url + "builds/" + expected.getName() + "/"
		        + expected.getTestType().name() + "/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<String> data = JsonUtil.fromJsonArray(String.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}

	@Test
	public void coverageSummary() throws Exception {

		Application expected = buildApplication();
		HttpResponse response = HttpUtil.post(url + "coverage/summary/" + expected.getName() + "/"
		        + expected.getTestType().name() + "/" + DateUtil.toString(expected.getTimeStamp()));
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}

	@Test
	public void findApplication() throws Exception {

		Application expected = buildApplication();
		HttpResponse response = HttpUtil.post(url + "coverage/" + expected.getName() + "/"
		        + expected.getTestType().name() + "/" + DateUtil.toString(expected.getTimeStamp()));
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}

	@Test
	public void delete() throws Exception {
 
		Application expected = buildApplication();
		HttpResponse response = HttpUtil.post(url + "/coverage/delete/" + expected.getName() + "/"
		        + expected.getTestType().name() + "/" + DateUtil.toString(expected.getTimeStamp()) + "/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
	}
}
