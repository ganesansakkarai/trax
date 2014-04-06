package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.TestType;

public interface CoverageService {

	public Long countAllApplications();

	public Application findApplication(Long id);

	public List<Application> findAllApplications();
	
	public List<Long> findAllTimeStamps(String name, TestType testType);
	
	public List<String> findApplicationNames(TestType testType);
	
	public Application findApplication(Long timeStamp, TestType testType);

	public List<Application> findApplications(int page, int maxResults);

	public Application updateApplication(Application application);

	public Application saveApplication(Application application);

	public void deleteApplication(Long timeStamp, TestType testType);
}
