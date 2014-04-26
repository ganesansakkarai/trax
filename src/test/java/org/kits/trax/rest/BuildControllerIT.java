package org.kits.trax.rest;

import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kits.trax.domain.Build;
import org.kits.trax.util.DataUtil;
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
public class BuildControllerIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(BuildControllerIT.class);
	private static final String url = "http://localhost:8080/";

	private Build build() throws Exception {

		Build root = new Build();
		root.setName("root");
		root.setTimeStamp(new Date());
		String jsonData = JsonUtil.toJson(root);
		HttpResponse response = HttpUtil.post(url + "build", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		LOGGER.info("Response: " + jsonData);
		root = JsonUtil.fromJson(Build.class, jsonData);
		return root;
	}

	@Test
	public void saveBuild() throws Exception {

		Build build = DataUtil.build();
		String jsonData = JsonUtil.toJson(build);
		HttpResponse response = HttpUtil.post(url + "build", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		build = JsonUtil.fromJson(Build.class, jsonData);
		Assert.assertNotNull(build.getId());
	}

	@Test
	public void SaveModule() throws Exception {

		Build root = build();
		Build module = DataUtil.build();
		module.setParent(root);
		String jsonData = JsonUtil.toJson(module);
		HttpResponse response = HttpUtil.post(url + "build/" + root.getId() + "/module", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		module = JsonUtil.fromJson(Build.class, jsonData);
		Assert.assertNotNull(module.getId());
	}

	@Test
	public void listApplications() throws Exception {

		Build build = build();
		HttpResponse response = HttpUtil.post(url + "apps/");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<String> data = JsonUtil.fromJsonArray(String.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}

	@Test
	public void listBuilds() throws Exception {

		Build build = build();
		HttpResponse response = HttpUtil.post(url + "app/" + build.getName() + "/builds");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<Build> data = JsonUtil.fromJsonArray(Build.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}

	@Test
	public void listModules() throws Exception {

		Build build = build();
		HttpResponse response = HttpUtil.post(url + "build/" + build.getId() + "/modules");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}

	@Test
	public void delete() throws Exception {

		Build build = build();
		HttpResponse response = HttpUtil.post(url + "build/" + build.getId() + "/delete");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
	}
}
