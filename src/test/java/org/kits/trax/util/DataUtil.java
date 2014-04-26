package org.kits.trax.util;

import java.util.Date;

import org.kits.trax.domain.Build;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.TestCase;
import org.kits.trax.domain.TestCoverage;
import org.kits.trax.domain.TestResult;
import org.kits.trax.domain.TestStatus;
import org.kits.trax.domain.TestSuite;

public class DataUtil {	

	public static Build build() {

		Build build = new Build();
		build.setName("sample");
		build.setTimeStamp(new Date());
		TestCoverage coverage = new TestCoverage();
		build.getTestCoverages().add(coverage);
		
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
		
		TestResult testResult = new TestResult();
		build.getTestResults().add(testResult);
		
		TestSuite testSuite = new TestSuite();
		testSuite.setName("Sample");
		testResult.getTestSuites().add(testSuite);
		
		for (int i = 0; i < 10; ++i) {
			TestCase testCase = new TestCase();
			testCase.setName("test" + i);
			testCase.setDuration(10l);
			testCase.setLog("testing");
			testCase.setStatus(TestStatus.PASS);
			testSuite.getTestCases().add(testCase);
		}
		
		return build;
	}
}
