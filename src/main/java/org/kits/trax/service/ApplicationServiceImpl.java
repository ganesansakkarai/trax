package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.Build;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.Module;
import org.kits.trax.domain.TestType;
import org.kits.trax.repository.ApplicationRepository;
import org.kits.trax.repository.BuildRepository;
import org.kits.trax.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private ApplicationRepository applicationRepository;
	@Autowired
	private BuildRepository buildRepository;
	@Autowired
	private ModuleRepository moduleRepository;

	public Build saveBuild(String name, Build build) {
		
		Application application = null;
		if(build.getId() == null) {
			application = applicationRepository.findByName(name);
			if(application == null) {
				application = new Application();
				application.setName(name);
				application = applicationRepository.save(application);				
			}
			
			build.setApplication(application);
		}

		for (Module module : build.getModules()) {
			build.setLine(build.getLine() + module.getLine());
			build.setMissedLine(build.getMissedLine() + module.getMissedLine());
			build.setBranch(build.getBranch() + module.getBranch());
			build.setMissedBranch(build.getMissedBranch() + module.getMissedBranch());
		}

		build.setCoverage(((build.getLine() - build.getMissedLine()) / build.getLine()) * 100);
		return buildRepository.save(build);
	}

	public List<Application> listApplications() {

		return (List<Application>) applicationRepository.findAll();
	}

	public Application findApplication(String name) {

		return applicationRepository.findByName(name);
	}

	public List<Build> listBuilds(Long id, TestType testType) {

		return buildRepository.list(id, testType);
	}

	public Build findBuild(Long id) {

		return buildRepository.find(id);
	}

	public void deleteBuild(Long id) {

		Build build = buildRepository.findOne(id);
		buildRepository.delete(build);
	}

	public Module saveModule(Long id, Module module) {

		Build build = buildRepository.findOne(id);
		module.setBuild(build);

		for (Clazz clazz : module.getClazzes()) {
			for (Method method : clazz.getMethods()) {
				clazz.setLine(clazz.getLine() + method.getLine());
				clazz.setMissedLine(clazz.getMissedLine() + method.getMissedLine());
				clazz.setBranch(clazz.getBranch() + method.getBranch());
				clazz.setMissedBranch(clazz.getMissedBranch() + method.getMissedBranch());
				method.setCoverage(((method.getLine() - method.getMissedLine()) / method.getLine()) * 100);
			}

			module.setLine(module.getLine() + clazz.getLine());
			module.setMissedLine(module.getMissedLine() + clazz.getMissedLine());
			module.setBranch(module.getBranch() + clazz.getBranch());
			module.setMissedBranch(module.getMissedBranch() + clazz.getMissedBranch());
			clazz.setCoverage(((clazz.getLine() - clazz.getMissedLine()) / clazz.getLine()) * 100);
		}

		module.setCoverage(((module.getLine() - module.getMissedLine()) / module.getLine()) * 100);
		module = moduleRepository.save(module);
		return module;
	}
}
