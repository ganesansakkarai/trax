package org.kits.trax.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kits.trax.domain.Build;
import org.kits.trax.util.HttpUtil;
import org.kits.trax.util.JsonUtil;
import org.kits.trax.util.TestDataUtil;
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

	private static final String url = "http://localhost:8080/";

	private Build saveRootModuleBuild() throws Exception {

		Build root = TestDataUtil.getRootBuild();
		String jsonData = JsonUtil.toJson(root);
		HttpResponse response = HttpUtil.post(url + "build", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		root = JsonUtil.fromJson(Build.class, jsonData);
		return root;
	}
	
	private Build saveSingleModuleBuild() throws Exception {
		
		Build module = TestDataUtil.getModuleBuild();
		String jsonData = JsonUtil.toJson(module);
		HttpResponse response = HttpUtil.post(url + "build", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		module = JsonUtil.fromJson(Build.class, jsonData);
		return module;
	}
	
	private Build saveChildModuleBuild() throws Exception {
		
		Build root = saveRootModuleBuild();
		Build module = TestDataUtil.getModuleBuild();
		String jsonData = JsonUtil.toJson(module);
		HttpResponse response = HttpUtil.post(url + "build/" + root.getId() + "/module", jsonData);
		jsonData = IOUtils.toString(response.getEntity().getContent());
		module = JsonUtil.fromJson(Build.class, jsonData);
		return module;
	}

	@Test
	public void saveBuild() throws Exception {

		Build build = saveRootModuleBuild();
		Assert.assertNotNull(build.getId());
		Assert.assertNull(build.getParent());
		Assert.assertTrue(build.getTestCoverages().size() == 0);
		Assert.assertTrue(build.getTestResults().size() == 0);
	}

	@Test
	public void saveModule() throws Exception {

		Build module = saveChildModuleBuild();
		Assert.assertNotNull(module.getId());
		Assert.assertNotNull(module.getParent());
		Assert.assertTrue(module.getTestCoverages().size() > 0);
		Assert.assertTrue(module.getTestResults().size() > 0);
	}

	@Test
	public void listApplications() throws Exception {

		saveChildModuleBuild();
		HttpResponse response = HttpUtil.post(url + "apps");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<String> data = JsonUtil.fromJsonArray(String.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}

	@Test
	public void listBuilds() throws Exception {

		Build build = saveChildModuleBuild();
		HttpResponse response = HttpUtil.post(url + "app/" + build.getParent().getName() + "/builds");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		List<Build> data = JsonUtil.fromJsonArray(Build.class, jsonData);
		Assert.assertTrue(data.size() > 0);
	}

	@Test
	public void listModules() throws Exception {

		Build build = saveChildModuleBuild();
		HttpResponse response = HttpUtil.post(url + "build/" + build.getParent().getId() + "/modules");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Map data = JsonUtil.fromJson(HashMap.class, jsonData);
		Assert.assertTrue(data.size() == 4);
	}
	
	@Test
	public void coverageSummaryWithSingleModule() throws Exception {

		Build build = saveSingleModuleBuild();		
		HttpResponse response = HttpUtil.post(url + "build/" + build.getId() + "/coverage");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}
	
	@Test
	public void resultSummaryWithSingleModule() throws Exception {

		Build build = saveSingleModuleBuild();		
		HttpResponse response = HttpUtil.post(url + "build/" + build.getId() + "/result");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}
	
	@Test
	public void coverageSummaryWithMultipleModule() throws Exception {

		Build build = saveChildModuleBuild();		
		HttpResponse response = HttpUtil.post(url + "build/" + build.getParent().getId() + "/coverage");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}
	
	@Test
	public void resultSummaryWithMultipleModule() throws Exception {

		Build build = saveChildModuleBuild();		
		HttpResponse response = HttpUtil.post(url + "build/" + build.getParent().getId() + "/result");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
		String jsonData = IOUtils.toString(response.getEntity().getContent());
		Assert.assertNotNull(jsonData);
	}

	@Test
	public void delete() throws Exception {

		Build build = saveChildModuleBuild();
		HttpResponse response = HttpUtil.post(url + "build/" + build.getId() + "/delete");
		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
	}
}
