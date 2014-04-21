package org.kits.trax.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kits.trax.domain.Application;
import org.kits.trax.domain.Build;
import org.kits.trax.domain.Module;
import org.kits.trax.domain.TestType;
import org.kits.trax.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=org.kits.trax.Application.class)
public class ApplicationServiceTest {

	@Autowired
	private ApplicationService applicationService;
	
	@Test
	public void saveBuild() {
		Build build = DataUtil.build();
		build = applicationService.saveBuild("test", build);
		Assert.assertTrue(build.getId() != 0);
	}
	
	@Test
	public void saveModule() {
		Build build = DataUtil.build();
		build = applicationService.saveBuild("test", build);
		
		Module module = DataUtil.buildModule();
		module = applicationService.saveModule(build.getId(), module);
		build = applicationService.findBuild(build.getId());
		Assert.assertTrue(build.getModules().size() == 1);
	}
	
	@Test
	public void listApplications() {
		Build build = DataUtil.build();
		build = applicationService.saveBuild("test", build);
		List<Application> applications = applicationService.listApplications();
		Assert.assertTrue(applications.size() > 0);
	}

	@Test
	public void listBuilds() {		
		Build build = DataUtil.build();
		build = applicationService.saveBuild("test", build);
		List<Build> builds = applicationService.listBuilds(build.getApplication().getId(), TestType.UNIT);
		Assert.assertTrue(builds.size() > 0);
	}
	
	@Test
	public void findApplication() {		
		Build build = DataUtil.build();
		build = applicationService.saveBuild("test", build);
		build = applicationService.findBuild(build.getId());
		Assert.assertNotNull(build);
	}
	
	@Test
	public void deleteApplication() {
		
		Build build = DataUtil.build();
		build = applicationService.saveBuild("test", build);
		applicationService.deleteBuild(build.getId());
		build = applicationService.findBuild(build.getId());
		Assert.assertNull(build);
	}
}
