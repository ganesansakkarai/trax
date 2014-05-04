package org.kits.trax.rest;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kits.trax.domain.Build;
import org.kits.trax.domain.TestCoverage;
import org.kits.trax.domain.TestResult;
import org.kits.trax.domain.TestType;
import org.kits.trax.service.BuildService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuildController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BuildController.class);
	@Autowired
	private BuildService buildService;

	@RequestMapping(value = "/build", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> saveBuild(@RequestBody String json) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			Build build = JsonUtil.fromJson(Build.class, json);
			build = buildService.saveBuild(build);
			String jsonData = JsonUtil.toJson(build);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error adding applications", e);
			response = new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}

		return response;
	}

	@RequestMapping(value = "/build/{id}/module", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> saveModule(@PathVariable Long id, @RequestBody String json) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			Build build = JsonUtil.fromJson(Build.class, json);
			build = buildService.saveModule(id, build);
			String jsonData = JsonUtil.toJson(build);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error adding applications", e);
			response = new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}

		return response;
	}

	@RequestMapping(value = "/apps", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> listApplications() {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<String> result = buildService.listApplications();
		String jsonData = JsonUtil.toJsonArray(result);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/app/{name}/builds", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> listBuilds(@PathVariable("name") String name) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Build> result = buildService.listBuilds(name);
		String jsonData = JsonUtil.toJsonArray(result);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/build/{id}/modules/{type}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> listModules(@PathVariable("id") Long id, @PathVariable("type") String type)
	        throws ParseException {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		LOGGER.info("Listing modules for build " + id + ", " + type);
		TestType testType = TestType.valueOf(type);
		List<Build> result = buildService.listModules(id, testType);
		Map info = new HashMap();
		info.put("sEcho", 1);
		info.put("iTotalRecords", 1);
		info.put("iTotalDisplayRecords", 1);
		info.put("aaData", result);
		String jsonData = JsonUtil.toJson(info);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/build/{id}/delete", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> deleteBuild(@PathVariable("id") Long id) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			LOGGER.info("Deleting Build for " + id);
			buildService.deleteBuild(id);
			response = new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error Deleting application :", e);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}
	
	@RequestMapping(value = "/build/{id}/coverage/{type}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> coverageSummary(@PathVariable("id") Long id, @PathVariable("type") String type) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		TestType testType = TestType.valueOf(type);
		List<Build> modules = buildService.listModules(id, testType);
		
		TestCoverage overall = new TestCoverage();
		overall.setTestType(testType);
		for (Build module : modules) {
			for (TestCoverage coverage : module.getTestCoverages()) {
				if (coverage.getTestType() == testType) {
					overall.setCoverage(overall.getCoverage() + coverage.getCoverage());
					overall.setLine(overall.getLine() + coverage.getLine());
					overall.setMissedLine(overall.getMissedLine() + coverage.getMissedLine());
					overall.setBranch(overall.getBranch() + coverage.getBranch());
					overall.setMissedBranch(overall.getMissedBranch() + coverage.getMissedBranch());
				}
			}
		}

		String jsonData = JsonUtil.toJson(overall);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/build/{id}/result/{type}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> resultSummary(@PathVariable("id") Long id, @PathVariable("type") String type) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		TestType testType = TestType.valueOf(type);
		List<Build> modules = buildService.listModules(id, testType);
		
		TestResult overall = new TestResult();
		overall.setTestType(testType);
		for (Build module : modules) {
			for (TestResult result : module.getTestResults()) {
				if (result.getTestType() == testType) {
					overall.setDuration(overall.getDuration() + result.getDuration());
					overall.setPass(overall.getPass() + result.getPass());
					overall.setFail(overall.getFail() + result.getFail());
					overall.setSkip(overall.getSkip() + result.getSkip());
					overall.setSuccess(overall.getSuccess() + result.getSuccess());
				}
			}
		}
		
		String jsonData = JsonUtil.toJson(overall);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}
}
