package org.kits.trax.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kits.trax.domain.TestResult;
import org.kits.trax.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestResultServiceImpl implements TestResultService {

	@Autowired
	private TestResultRepository testResultRepository;

	public long countAllTestResults() {

		return testResultRepository.count();
	}

	public void deleteTestResult(TestResult testResult) {

		testResultRepository.delete(testResult);
	}

	public void deleteTestResults(List<TestResult> testResults) {

		for (TestResult testResult : testResults) {
			testResultRepository.delete(testResult);
		}
	}

	public TestResult findTestResult(Long id) {

		return testResultRepository.findOne(id);
	}

	public List<TestResult> findAllTestResults() {

		return (List<TestResult>) testResultRepository.findAll();
	}

	public List<TestResult> findTestResults(Date timeStamp) {

		return testResultRepository.findByTimeStamp(timeStamp);
	}

	public List<TestResult> findTestResults(int page, int maxResults) {

		return testResultRepository.findAll(new PageRequest(page, maxResults)).getContent();
	}

	public TestResult saveTestResult(TestResult testResult) {

		return testResultRepository.save(testResult);
	}

	public List<TestResult> saveTestResults(List<TestResult> testResults) {

		List<TestResult> saved = new ArrayList<TestResult>();
		for (TestResult testResult : testResults) {
			saved.add(testResultRepository.save(testResult));
		}

		return saved;
	}

	public TestResult updateTestResult(TestResult testResult) {

		return testResultRepository.save(testResult);
	}
}
