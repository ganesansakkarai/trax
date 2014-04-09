package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.TestType;
import org.kits.trax.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringApplicationConfiguration(classes=org.kits.trax.Application.class)
public class CoverageServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private CoverageService coverageService;
	
	@Test
	public void saveApplication() {
		Application application = DataUtil.build();
		application = coverageService.saveApplication(application);
		Assert.assertTrue(application.getId() != 0);
	}
	
	@Test
	public void listApplications() {
		Application application = DataUtil.build();
		coverageService.saveApplication(application);
		List<String> applications = coverageService.listApplications(TestType.Unit);
		Assert.assertTrue(applications.size() > 0);
	}

	@Test
	public void listBuilds() {
		
		Application application = DataUtil.build();
		coverageService.saveApplication(application);
		List<Long> builds = coverageService.listBuilds(application.getName(), TestType.Unit);
		Assert.assertTrue(builds.size() > 0);
	}
	
	@Test
	public void findApplication() {
		
		Application application = DataUtil.build();
		coverageService.saveApplication(application);
		application = coverageService.findApplication(application.getName(), TestType.Unit, application.getTimeStamp());
		Assert.assertNotNull(application);
	}
	
	@Test
	public void deleteApplication() {
		
		Application application = DataUtil.build();
		coverageService.saveApplication(application);
		coverageService.deleteApplication(application.getName(), TestType.Unit, application.getTimeStamp());
		application = coverageService.findApplication(application.getName(), TestType.Unit, application.getTimeStamp());
		Assert.assertNull(application);
	}
}
