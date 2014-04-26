package org.kits.trax.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kits.trax.domain.Build;
import org.kits.trax.util.TestDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = org.kits.trax.Application.class)
public class BuildServiceTest {

	@Autowired
	private BuildService buildService;

	@Test
	public void saveBuild() {
		Build root = TestDataUtil.getRootBuild();
		root = buildService.saveBuild(root);
		Assert.assertTrue(root.getId() != 0);
	}

	@Test
	public void saveModule() {
		Build root = TestDataUtil.getRootBuild();
		root = buildService.saveBuild(root);
		Build build = TestDataUtil.getModuleBuild();
		build = buildService.saveModule(root.getId(), build);
		Assert.assertTrue(build.getParent().getId() != null);
	}

	@Test
	public void listApplications() {
		Build root = TestDataUtil.getRootBuild();
		root = buildService.saveBuild(root);
		List<String> applications = buildService.listApplications();
		Assert.assertTrue(applications.size() > 0);
	}

	@Test
	public void listBuilds() {
		Build root = TestDataUtil.getRootBuild();
		root = buildService.saveBuild(root);
		Build build = TestDataUtil.getModuleBuild();
		build = buildService.saveModule(root.getId(), build);
		List<Build> builds = buildService.listBuilds(root.getName());
		Assert.assertTrue(builds.size() > 0);
	}

	@Test
	public void listModules() {
		Build root = TestDataUtil.getRootBuild();
		root = buildService.saveBuild(root);
		Build build = TestDataUtil.getModuleBuild();
		build = buildService.saveModule(root.getId(), build);
		List<Build> builds = buildService.listModules(root.getId());
		Assert.assertNotNull(builds);
	}

	@Test
	public void delete() {

		Build root = TestDataUtil.getRootBuild();
		root = buildService.saveBuild(root);
		Build build = TestDataUtil.getModuleBuild();
		build = buildService.saveModule(root.getId(), build);
		buildService.deleteBuild(root.getId());
		root = buildService.findBuild(root.getId());
		Assert.assertNull(root);
	}
}
