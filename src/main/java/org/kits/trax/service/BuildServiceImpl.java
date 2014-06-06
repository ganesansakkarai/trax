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
import org.springframework.data.domain.PageRequest;
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

	public Build saveModule(String name, Build build) {

		Build parent = buildRepository.findOne(buildRepository.findLatest(name));

		for (TestCoverage testCoverage : build.getTestCoverages()) {
			process(testCoverage);
			TestCoverage aCoverage = null;
			for (TestCoverage coverage : parent.getTestCoverages()) {
				if(coverage.getTestType().equals(testCoverage.getTestType())) {
					aCoverage = coverage;
				}
			}
			
			if(aCoverage == null) {
				aCoverage = new TestCoverage();
				parent.getTestCoverages().add(aCoverage);
			}
			
			aCoverage.setLine(testCoverage.getLine() + aCoverage.getLine());
			aCoverage.setMissedLine(testCoverage.getMissedLine() + aCoverage.getMissedLine());
			aCoverage.setCoverage((aCoverage.getLine() - aCoverage.getMissedLine()) / aCoverage.getLine() * 100);
			aCoverage.setBranch(testCoverage.getBranch() + aCoverage.getBranch());
			aCoverage.setMissedBranch(testCoverage.getMissedBranch() + aCoverage.getMissedBranch());
			aCoverage.setTestType(testCoverage.getTestType());
		}

		for (TestResult testResult : build.getTestResults()) {
			processTestResult(testResult);
			TestResult aResult = null;
			for (TestResult result : parent.getTestResults()) {
				if(result.getTestType().equals(testResult.getTestType())) {
					aResult = result;
				}
			}
			
			if(aResult == null) {
				aResult = new TestResult();
				parent.getTestResults().add(aResult);
			}
			
			aResult.setDuration(testResult.getDuration() + aResult.getDuration());
			aResult.setTestType(testResult.getTestType());
			aResult.setPass(testResult.getPass() + aResult.getPass());
			aResult.setSkip(testResult.getSkip() + aResult.getSkip());
			aResult.setFail(testResult.getFail() + aResult.getFail());
			aResult.setSuccess(aResult.getPass() / (aResult.getPass() + aResult.getSkip() + aResult.getFail()) * 100);
		}

		build.setParent(parent);
		buildRepository.save(build);
		parent.getModules().add(build);
		buildRepository.save(parent);		
		return build;
	}

	public List<String> listApplications() {

		return buildRepository.listApplications();
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

	@Override
    public List<Build> listTrend(String name) {
	    
		List<Build> builds = buildRepository.listTrend(name, new PageRequest(0, 5));
	    return builds;
    }
}
