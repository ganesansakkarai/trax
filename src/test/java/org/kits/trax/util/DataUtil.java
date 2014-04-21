package org.kits.trax.util;

import java.util.Date;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.Build;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.Module;
import org.kits.trax.domain.TestCase;
import org.kits.trax.domain.TestStatus;
import org.kits.trax.domain.TestSuite;
import org.kits.trax.domain.TestType;

public class DataUtil {
	

	public static Build build() {

		Application application = new Application();
		application.setName("sample");
		Build build = new Build();
		build.setTimeStamp(new Date());
		build.setTestType(TestType.UNIT);
		
		Module module = new Module();
		module.setName("sample.module");
		build.getModules().add(module);

		Clazz clazz = new Clazz();
		clazz.setName("Sample");
		module.getClazzes().add(clazz);

		for (int i = 0; i < 10; ++i) {
			Method method = new Method();
			method.setName("method" + i);
			method.setLine(10);
			method.setMissedLine(9);
			method.setBranch(5);
			method.setMissedBranch(4);
			clazz.getMethods().add(method);
		}
		
		
		module.setDuration(5l);
		module.setEndTime(new Date());
		module.setStartTime(new Date());
		module.setFail(10l);
		module.setName("Sample");
		module.setPass(10l);
		module.setSkip(5l);
		module.setSuccess(10l);
		
		TestSuite testSuite = new TestSuite();
		testSuite.setDuration(5l);
		testSuite.setEndTime(new Date());
		testSuite.setStartTime(new Date());
		testSuite.setFail(10l);
		testSuite.setName("Sample");
		testSuite.setPass(10l);
		testSuite.setSkip(5l);
		testSuite.setSuccess(10l);
		module.getTestSuites().add(testSuite);
		
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
	
	public static Module buildModule() {
		
		Module module = new Module();
		module.setName("sample.module");

		Clazz clazz = new Clazz();
		clazz.setName("Sample");
		module.getClazzes().add(clazz);

		for (int i = 0; i < 10; ++i) {
			Method method = new Method();
			method.setName("method" + i);
			method.setLine(10);
			method.setMissedLine(9);
			method.setBranch(5);
			method.setMissedBranch(4);
			clazz.getMethods().add(method);
		}
		
		module.setDuration(5l);
		module.setEndTime(new Date());
		module.setStartTime(new Date());
		module.setFail(10l);
		module.setName("Sample");
		module.setPass(10l);
		module.setSkip(5l);
		module.setSuccess(10l);
		
		TestSuite testSuite = new TestSuite();
		testSuite.setDuration(5l);
		testSuite.setEndTime(new Date());
		testSuite.setStartTime(new Date());
		testSuite.setFail(10l);
		testSuite.setName("Sample");
		testSuite.setPass(10l);
		testSuite.setSkip(5l);
		testSuite.setSuccess(10l);
		module.getTestSuites().add(testSuite);
		
		for (int i = 0; i < 10; ++i) {
			TestCase testCase = new TestCase();
			testCase.setName("test" + i);
			testCase.setDuration(10l);
			testCase.setLog("testing");
			testCase.setStatus(TestStatus.PASS);
			testSuite.getTestCases().add(testCase);
		}
		
		return module;
	}
}
