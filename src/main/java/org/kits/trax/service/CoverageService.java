package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.TestType;

public interface CoverageService {

	public Application saveApplication(Application application);
	
	public List<String> listApplications(TestType testType);
	
	public List<Long> listBuilds(String name, TestType testType);	
	
	public Application findApplication(String name, TestType testType, Long timeStamp);
	
	public void deleteApplication(String name, TestType testType, Long timeStamp);
}
