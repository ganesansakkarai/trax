package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.Build;
import org.kits.trax.domain.Module;
import org.kits.trax.domain.TestType;

public interface ApplicationService {

	public Build saveBuild(String name, Build build);
	
	public Module saveModule(Long id, Module module);
	
	public List<Application> listApplications();
	
	public List<Build> listBuilds(Long id, TestType testType);
	
	public Application findApplication(String name);
	
	public Build findBuild(Long id);
	
	public void deleteBuild(Long id);
}
