package org.kits.trax.rest;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kits.trax.domain.Application;
import org.kits.trax.domain.Build;
import org.kits.trax.domain.Module;
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

	private Build build() throws Exception {

		Build build = DataUtil.build();
		String jsonData = JsonUtil.toJson(build);
		HttpResponse response = HttpUtil.post(url + "app/sample/build", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		LOGGER.info("Response: " + jsonData);
		build = JsonUtil.fromJson(Build.class, jsonData);
		return build;
	}

	@Test
	public void createBuild() throws Exception {

		Build build = DataUtil.build();
		String jsonData = JsonUtil.toJson(build);
		HttpResponse response = HttpUtil.post(url + "app/sample/build", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		build = JsonUtil.fromJson(Build.class, jsonData);
		Assert.assertNotNull(build.getId());
		Assert.assertEquals(build.getApplication().getName(), "sample");
	}

	@Test
	public void createModule() throws Exception {

		Build build = build();

		Module module = DataUtil.buildModule();
		String jsonData = JsonUtil.toJson(module);
		HttpResponse response = HttpUtil.post(url + "build/" + build.getId() + "/module/", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		module = JsonUtil.fromJson(Module.class, jsonData);
		Assert.assertNotNull(module.getId());
		Assert.assertEquals(build.getId(), module.getBuild().getId());
	}

	@Test
	public void listApplications() throws Exception {

		Build build = build();
		HttpResponse response = HttpUtil.post(url + "apps/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<Application> data = JsonUtil.fromJsonArray(Application.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}

	@Test
	public void listBuilds() throws Exception {

		Build build = build();
		HttpResponse response = HttpUtil.post(url + "builds/" + build.getApplication().getId() + "/0/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<String> data = JsonUtil.fromJsonArray(String.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}

	@Test
	public void findBuild() throws Exception {

		Build build = build();
		HttpResponse response = HttpUtil.post(url + "build/" + build.getId() + "/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}

	@Test
	public void delete() throws Exception {

		Build build = build();
		HttpResponse response = HttpUtil.post(url + "/build/delete/" + build.getId() + "/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
	}
}
