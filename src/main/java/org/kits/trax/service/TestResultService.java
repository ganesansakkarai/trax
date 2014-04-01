package org.kits.trax.service;

import java.util.Date;
import java.util.List;

import org.kits.trax.domain.TestResult;

public interface TestResultService {

	public long countAllTestResults();

	public void deleteTestResult(TestResult testResult);

	public TestResult findTestResult(Long id);

	public List<TestResult> findAllTestResults();

	public List<TestResult> findTestResults(Date timeStamp);

	public List<TestResult> findTestResults(int page, int maxResults);

	public TestResult saveTestResult(TestResult testResult);

	public TestResult updateTestResult(TestResult testResult);

	public List<TestResult> saveTestResults(List<TestResult> testResults);

	public void deleteTestResults(List<TestResult> testResults);
}
