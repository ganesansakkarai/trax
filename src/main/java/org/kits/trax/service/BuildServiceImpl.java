package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Build;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.TestCase;
import org.kits.trax.domain.TestCoverage;
import org.kits.trax.domain.TestResult;
import org.kits.trax.domain.TestSuite;
import org.kits.trax.domain.TestType;
import org.kits.trax.repository.BuildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BuildServiceImpl implements BuildService {

	@Autowired
	private BuildRepository buildRepository;

	public Build saveBuild(Build build) {

		for (TestCoverage testCoverage : build.getTestCoverages()) {
			process(testCoverage);
		}

		for (TestResult testResult : build.getTestResults()) {
			processTestResult(testResult);
		}

		buildRepository.save(build);
		return build;
	}

	public Build saveModule(Long id, Build build) {

		if (id != null) {
			Build parent = buildRepository.findOne(id);
			build.setParent(parent);
		}

		for (TestCoverage testCoverage : build.getTestCoverages()) {
			process(testCoverage);
		}

		for (TestResult testResult : build.getTestResults()) {
			processTestResult(testResult);
		}

		buildRepository.save(build);
		return build;
	}

	public List<String> listApplications() {

		return (List<String>) buildRepository.listApplications();
	}

	public List<Build> listBuilds(String name) {

		return buildRepository.listBuilds(name);
	}

	public List<Build> listModules(Long id, TestType testType) {

		List<Build> modules = buildRepository.listModules(id);
		if (modules.size() > 1) {
			for (Build module : modules) {
				if (module.getId().equals(id)) {
					modules.remove(module);
				} else {
					for (TestCoverage testCoverage : module.getTestCoverages()) {
						if (testCoverage.getTestType() != testType) {
							module.getTestCoverages().remove(testCoverage);
						}
					}
				}
			}
		}

		return modules;
	}

	public Build findBuild(Long id) {

		return buildRepository.findOne(id);
	}

	public void deleteBuild(Long id) {

		Build build = buildRepository.findOne(id);
		List<Build> modules = buildRepository.listModules(id);

		for (Build module : modules) {
			if (module.getId() != build.getId()) {
				buildRepository.delete(module);
			}
		}

		buildRepository.delete(build);
	}

	private void process(TestCoverage testCoverage) {

		for (Clazz clazz : testCoverage.getClazzes()) {
			for (Method method : clazz.getMethods()) {
				clazz.setLine(clazz.getLine() + method.getLine());
				clazz.setMissedLine(clazz.getMissedLine() + method.getMissedLine());
				clazz.setBranch(clazz.getBranch() + method.getBranch());
				clazz.setMissedBranch(clazz.getMissedBranch() + method.getMissedBranch());
				method.setCoverage(((method.getLine() - method.getMissedLine()) / method.getLine()) * 100);
			}

			testCoverage.setLine(testCoverage.getLine() + clazz.getLine());
			testCoverage.setMissedLine(testCoverage.getMissedLine() + clazz.getMissedLine());
			testCoverage.setBranch(testCoverage.getBranch() + clazz.getBranch());
			testCoverage.setMissedBranch(testCoverage.getMissedBranch() + clazz.getMissedBranch());
			clazz.setCoverage(((clazz.getLine() - clazz.getMissedLine()) / clazz.getLine()) * 100);
		}

		testCoverage
		        .setCoverage(((testCoverage.getLine() - testCoverage.getMissedLine()) / testCoverage.getLine()) * 100);
	}

	private void processTestResult(TestResult testResult) {

		for (TestSuite testSuite : testResult.getTestSuites()) {
			for (TestCase testCase : testSuite.getTestCases()) {
				switch (testCase.getStatus()) {

				case PASS:
					testSuite.setPass(testSuite.getPass() + 1);
					break;

				case FAIL:
					testSuite.setFail(testSuite.getFail() + 1);
					break;

				case SKIP:
					testSuite.setSkip(testSuite.getSkip() + 1);
					break;
				}

				testSuite.setDuration(testSuite.getDuration() + testCase.getDuration());
			}

			testResult.setPass(testResult.getPass() + testSuite.getPass());
			testResult.setFail(testResult.getFail() + testSuite.getFail());
			testResult.setSkip(testResult.getSkip() + testSuite.getSkip());
			testResult.setDuration(testResult.getDuration() + testSuite.getDuration());
			testSuite.setSuccess(testSuite.getPass()
			        / (testSuite.getPass() + testSuite.getFail() + testSuite.getSkip()) * 100);
		}

		testResult.setSuccess(testResult.getPass()
		        / (testResult.getPass() + testResult.getFail() + testResult.getSkip()) * 100);
	}
}
