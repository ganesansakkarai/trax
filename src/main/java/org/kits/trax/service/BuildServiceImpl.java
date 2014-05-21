package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Build;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.TestCase;
import org.kits.trax.domain.TestCoverage;
import org.kits.trax.domain.TestResult;
import org.kits.trax.domain.TestSuite;
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

		Build parent = null;
		if (id != null) {
			parent = buildRepository.findOne(id);
		}

		for (TestCoverage testCoverage : build.getTestCoverages()) {
			process(testCoverage);
			boolean found = false;
			if (id != null) {
				for (TestCoverage coverage : parent.getTestCoverages()) {
					if(coverage.getTestType().equals(testCoverage.getTestType())) {
						found = true;
						coverage.setLine(testCoverage.getLine() + coverage.getLine());
						coverage.setMissedLine(testCoverage.getMissedLine() + coverage.getMissedLine());
						coverage.setCoverage((coverage.getLine() - coverage.getMissedLine()) / coverage.getLine() * 100);
						coverage.setBranch(testCoverage.getBranch() + coverage.getBranch());
						coverage.setMissedBranch(testCoverage.getMissedBranch() + coverage.getMissedBranch());
					}
				}
				
				if(!found) {
					parent.getTestCoverages().add(testCoverage);
				}
			}
		}

		for (TestResult testResult : build.getTestResults()) {
			processTestResult(testResult);
			boolean found = false;
			if (id != null) {
				for (TestResult result : parent.getTestResults()) {
					if(result.getTestType().equals(testResult.getTestType())) {
						found = true;
						result.setDuration(testResult.getDuration() + result.getDuration());
						result.setPass(testResult.getPass() + result.getPass());
						result.setSkip(testResult.getSkip() + result.getSkip());
						result.setFail(testResult.getFail() + result.getFail());
						result.setSuccess(result.getPass() / (result.getPass() + result.getSkip() + result.getFail()) * 100);
					}
				}
				
				if(!found) {
					parent.getTestResults().add(testResult);
				}
			}
		}

		buildRepository.save(parent);
		build.setParent(parent);
		buildRepository.save(build);
		return build;
	}

	public List<String> listApplications() {

		return (List<String>) buildRepository.listApplications();
	}

	public List<Build> listBuilds(String name) {

		return buildRepository.listBuilds(name);
	}

	public Build findBuild(Long id) {

		return buildRepository.findOne(id);
	}

	public void deleteBuild(Long id) {

		Build build = buildRepository.findOne(id);
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

				case "PASS":
					testSuite.setPass(testSuite.getPass() + 1);
					break;

				case "FAIL":
					testSuite.setFail(testSuite.getFail() + 1);
					break;

				case "SKIP":
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
