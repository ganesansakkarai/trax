package org.kits.trax.rest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.TestType;
import org.kits.trax.service.CoverageService;
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
public class CoverageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoverageController.class);
	@Autowired
	private CoverageService coverageService;

	@RequestMapping(value = "/coverage/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> create(@RequestBody String json) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			Application coverage = JsonUtil.fromJson(Application.class, json);
			coverage = coverageService.saveApplication(coverage);
			String jsonData = JsonUtil.toJson(coverage);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
			long count = coverageService.countAllApplications();
			System.out.println(count);
		} catch (Exception e) {
			LOGGER.error("Error adding coverages", e);
			response = new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}

		return response;
	}

	@RequestMapping(value = "/coverage/{id}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> find(@PathVariable("id") Long id) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Application result = coverageService.findApplication(id);
		if (result != null) {
			Map info = new HashMap();
			info.put("sEcho", 1);
			info.put("iTotalRecords", result.getPackages().size());
			info.put("iTotalDisplayRecords", result.getPackages().size());
			info.put("aaData", result.getPackages());
			String jsonData = JsonUtil.toJson(info);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		} else {
			LOGGER.error("Coverage not found for id " + id);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/applications/{testType}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listApplications(@PathVariable("testType") String testType) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<String> result = coverageService.findApplicationNames(TestType.valueOf(testType));
		String jsonData = JsonUtil.toJsonArray(result);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/builds/{name}/{testType}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listBuilds(@PathVariable("name") String name, @PathVariable("testType") String testType) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Long> result = coverageService.findAllTimeStamps(name, TestType.valueOf(testType));
		List<String> builds = new ArrayList<String>();
		for (Long timmeStamp : result) {
			builds.add(DateUtil.toString(timmeStamp));
		}
		String jsonData = JsonUtil.toJson(builds);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/coverages/summary/{testType}/{timeStamp}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> coverageSummary(
	        @PathVariable("timeStamp") String timeStamp, @PathVariable("testType") String testType)
	        throws ParseException {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		LOGGER.info("Coverages Summary for " + timeStamp);
		Application result = coverageService.findApplication(DateUtil.toLong(timeStamp), TestType.valueOf(testType));
		Method summary = new Method();
		for(org.kits.trax.domain.Package aPackage: result.getPackages()) {
			for(Clazz clazz : aPackage.getClazzes()) {
				for (Method coverage : clazz.getMethods()) {
					summary.setLine(summary.getLine() + coverage.getLine());
					summary.setMissedLine(summary.getMissedLine() + coverage.getMissedLine());
					summary.setBranch(summary.getBranch() + coverage.getBranch());
					summary.setMissedBranch(summary.getMissedBranch() + coverage.getMissedBranch());
				}
			}
		}
		
		String jsonData = JsonUtil.toJson(summary);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/coverage/{testType}/{timeStamp}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findByTimeStampTestType(@PathVariable("timeStamp") String timeStamp, @PathVariable("testType") String testType)
	        throws ParseException {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		LOGGER.info("Listing coverages for " + DateUtil.toLong(timeStamp));
		Application result = coverageService.findApplication(DateUtil.toLong(timeStamp), TestType.valueOf(testType));
		long count = coverageService.countAllApplications();
		Map info = new HashMap();
		info.put("sEcho", 1);
		info.put("iTotalRecords", 1);
		info.put("iTotalDisplayRecords", 1);
		info.put("aoData", result);
		String jsonData = JsonUtil.toJson(info);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/coverages/delete/{testType}/{timeStamp}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> delete(@PathVariable("timeStamp") String timeStamp, @PathVariable("testType") String testType) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			LOGGER.info("Deleting coverages for " + timeStamp);
			coverageService.deleteApplication(DateUtil.toLong(timeStamp), TestType.valueOf(testType));
			response = new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error Deleting coverage :", e);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}
}
