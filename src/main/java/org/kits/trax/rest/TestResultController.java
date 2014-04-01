package org.kits.trax.rest;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kits.trax.domain.TestResult;
import org.kits.trax.service.TestResultService;
import org.kits.trax.util.DateUtil;
import org.kits.trax.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResultController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestResultController.class);
	@Autowired
	private TestResultService testResultService;

	@RequestMapping(value = "/testResults/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> bulkCreate(@RequestBody String json) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			List<TestResult> testResults = JsonUtil.fromJsonArray(TestResult.class, json);
			testResults = testResultService.saveTestResults(testResults);
			String jsonData = JsonUtil.toJson(testResults);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error adding testResults", e);
			response = new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}

		return response;
	}

	@RequestMapping(value = "/testResult/{id}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> find(@PathVariable("id") Long id) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		TestResult testResult = testResultService.findTestResult(id);
		if (testResult != null) {
			String jsonData = JsonUtil.toJson(testResult);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		} else {
			LOGGER.error("TestResult not found for id " + id);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/testResults/", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> list() {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<TestResult> result = testResultService.findAllTestResults();
		Map info = new HashMap();
		info.put("Result", "OK");
		info.put("Records", result);
		info.put("TotalRecordCount", result.size());
		String jsonData = JsonUtil.toJson(info);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/testResults/{timeStamp}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listByTimeStamp(@PathVariable("timeStamp") String timeStamp) throws ParseException {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		LOGGER.info("Listing testResults for " + timeStamp);
		List<TestResult> result = testResultService.findTestResults(DateUtil.toDate(timeStamp));
		Map info = new HashMap();
		info.put("Result", "OK");
		info.put("Records", result);
		info.put("TotalRecordCount", result.size());
		String jsonData = JsonUtil.toJson(info);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/testResults/delete/{timeStamp}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> delete(@PathVariable("timeStamp") String timeStamp) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			LOGGER.info("Deleting testResults for " + timeStamp);
			List<TestResult> result = testResultService.findTestResults(DateUtil.toDate(timeStamp));
			testResultService.deleteTestResults(result);
			response = new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error Deleting testResult :", e);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}
	
	@RequestMapping(value = "/testResults/delete", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> bulkDelete() {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			LOGGER.info("Deleting all testResults");
			List<TestResult> result = testResultService.findAllTestResults();
			testResultService.deleteTestResults(result);
			response = new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error Deleting testResult :", e);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}
}
