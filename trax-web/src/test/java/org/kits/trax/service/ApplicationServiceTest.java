package org.kits.trax.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kits.trax.domain.Application;
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
	public void saveApplication() {
		Application application = DataUtil.build();
		application = applicationService.saveApplication(application);
		Assert.assertTrue(application.getId() != 0);
	}
	
	@Test
	public void listApplications() {
		Application application = DataUtil.build();
		applicationService.saveApplication(application);
		List<String> applications = applicationService.listApplications(TestType.UNIT);
		Assert.assertTrue(applications.size() > 0);
	}

	@Test
	public void listBuilds() {
		
		Application application = DataUtil.build();
		applicationService.saveApplication(application);
		List<Long> builds = applicationService.listBuilds(application.getName(), TestType.UNIT);
		Assert.assertTrue(builds.size() > 0);
	}
	
	@Test
	public void findApplication() {
		
		Application application = DataUtil.build();
		applicationService.saveApplication(application);
		application = applicationService.findApplication(application.getName(), TestType.UNIT, application.getTimeStamp());
		Assert.assertNotNull(application);
	}
	
	@Test
	public void deleteApplication() {
		
		Application application = DataUtil.build();
		applicationService.saveApplication(application);
		applicationService.deleteApplication(application.getName(), TestType.UNIT, application.getTimeStamp());
		application = applicationService.findApplication(application.getName(), TestType.UNIT, application.getTimeStamp());
		Assert.assertNull(application);
	}
}
