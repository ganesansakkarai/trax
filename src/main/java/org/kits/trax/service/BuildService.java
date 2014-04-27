package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Build;
import org.kits.trax.domain.TestType;

public interface BuildService {

	public List<String> listApplications();

	public List<Build> listBuilds(String name);
	
	public List<Build> listModules(Long id, TestType testType);

	public Build findBuild(Long id);

	public Build saveModule(Long id, Build build);
	
	public Build saveBuild(Build build);

	public void deleteBuild(Long id);
}
