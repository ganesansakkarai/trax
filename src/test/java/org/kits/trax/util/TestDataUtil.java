package org.kits.trax.util;

import java.util.Date;

import org.kits.trax.domain.Build;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.TestCase;
import org.kits.trax.domain.TestCoverage;
import org.kits.trax.domain.TestResult;
import org.kits.trax.domain.TestSuite;

public class TestDataUtil {
	
	public static Build getRootBuild() {

		Build build = new Build();
		build.setName("root");
		build.setTimeStamp(new Date());		
		return build;
	}
	
	public static Build getModuleBuild() {

		Build build = new Build();
		build.setName("sample");
		build.setTimeStamp(new Date());
		build.getTestCoverages().add(getTestCoverage());
		build.getTestResults().add(getTestResult());
		return build;
	}
	
	public static TestCoverage getTestCoverage() {
		
		TestCoverage coverage = new TestCoverage();
		coverage.setTestType("UNIT");
		
		Clazz clazz = new Clazz();
		clazz.setName("Sample");
		coverage.getClazzes().add(clazz);

		for (int i = 0; i < 10; ++i) {
			Method method = new Method();
			method.setName("method" + i);
			method.setLine(10);
			method.setMissedLine(9);
			method.setBranch(5);
			method.setMissedBranch(4);
			clazz.getMethods().add(method);
		}
		
		return coverage;
	}
	
	public static TestResult getTestResult() {
		
		TestResult testResult = new TestResult();
		testResult.setTestType("UNIT");
		
		TestSuite testSuite = new TestSuite();
		testSuite.setName("Sample");
		testResult.getTestSuites().add(testSuite);
		
		for (int i = 0; i < 10; ++i) {
			TestCase testCase = new TestCase();
			testCase.setName("test" + i);
			testCase.setDuration(10l);
			testCase.setLog("testing");
			testCase.setStatus("PASS");
			testSuite.getTestCases().add(testCase);
		}
		
		return testResult;
	}
}
