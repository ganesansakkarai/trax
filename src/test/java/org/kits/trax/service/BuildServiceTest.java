package org.kits.trax.service;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kits.trax.domain.Build;
import org.kits.trax.util.DataUtil;
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
		Build build = DataUtil.build();
		build = buildService.saveBuild(build);
		Assert.assertTrue(build.getId() != 0);
	}

	@Test
	public void saveModule() {
		Build root = new Build();
		root.setName("root");
		root.setTimeStamp(new Date());
		buildService.saveBuild(root);

		Build build = DataUtil.build();
		build = buildService.saveModule(root.getId(), build);
		Assert.assertTrue(build.getParent() != null);
	}

	@Test
	public void listApplications() {
		Build build = DataUtil.build();
		build = buildService.saveBuild(build);
		List<String> applications = buildService.listApplications();
		Assert.assertTrue(applications.size() > 0);
	}

	@Test
	public void listBuilds() {
		Build build = DataUtil.build();
		build = buildService.saveBuild(build);
		List<Build> builds = buildService.listBuilds(build.getName());
		Assert.assertTrue(builds.size() > 0);
	}

	@Test
	public void listModules() {
		Build build = DataUtil.build();
		build = buildService.saveBuild(build);
		List<Build> builds = buildService.listModules(build.getId());
		Assert.assertNotNull(builds);
	}

	@Test
	public void delete() {

		Build build = DataUtil.build();
		build = buildService.saveBuild(build);
		buildService.deleteBuild(build.getId());
		build = buildService.findBuild(build.getId());
		Assert.assertNull(build);
	}
}
