package org.kits.trax.service;

import java.util.Date;
import java.util.List;

import org.kits.trax.Application;
import org.kits.trax.domain.TestType;
import org.kits.trax.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

@SpringApplicationConfiguration(classes=Application.class)
public class CoverageServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private CoverageService coverageService;
	
	@Test
	public void listTimeStamps() {
		org.kits.trax.domain.Application application = DataUtil.build();
		coverageService.saveApplication(application);
		List<Long> timeStamps = coverageService.findAllTimeStamps("Sample", TestType.Unit);
		Assert.assertTrue(timeStamps.size() > 0);
	}
	
	@Test
	public void findApplication() {
		org.kits.trax.domain.Application application = DataUtil.build();
		coverageService.saveApplication(application);
		org.kits.trax.domain.Application app = coverageService.findApplication(application.getTimeStamp(), application.getTestType());
		Assert.assertNotNull(app);
		Assert.assertEquals(app.getTimeStamp(), application.getTimeStamp());
	}
}
