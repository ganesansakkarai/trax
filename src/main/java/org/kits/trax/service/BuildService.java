package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Build;

public interface BuildService {

	public List<String> listApplications();

	public List<Build> listBuilds(String name);
	
	public List<Build> listTrend(String name);

	public Build findBuild(Long id);

	public Build saveModule(String name, Build build);
	
	public Build saveBuild(Build build);

	public void deleteBuild(Long id);
}
