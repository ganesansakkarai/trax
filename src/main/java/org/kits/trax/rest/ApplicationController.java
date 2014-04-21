package org.kits.trax.rest;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.Build;
import org.kits.trax.domain.Module;
import org.kits.trax.domain.TestType;
import org.kits.trax.service.ApplicationService;
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
public class ApplicationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
	@Autowired
	private ApplicationService applicationService;

	@RequestMapping(value = "/app/{name}/build", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createBuild(@PathVariable("name") String name, @RequestBody String json) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			Build build = JsonUtil.fromJson(Build.class, json);
			build = applicationService.saveBuild(name, build);
			String jsonData = JsonUtil.toJson(build);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error adding applications", e);
			response = new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}

		return response;
	}

	@RequestMapping(value = "/build/{id}/module", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createModule(@PathVariable Long id, @RequestBody String json) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			Module module = JsonUtil.fromJson(Module.class, json);
			module = applicationService.saveModule(id, module);
			String jsonData = JsonUtil.toJson(module);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error adding applications", e);
			response = new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}

		return response;
	}

	@RequestMapping(value = "/apps/", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> listApplications() {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Application> result = applicationService.listApplications();
		String jsonData = JsonUtil.toJsonArray(result);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/builds/{id}/{testType}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> listBuilds(@PathVariable("id") Long id, @PathVariable("testType") int testType) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Build> result = applicationService.listBuilds(id, TestType.values()[testType]);
		String jsonData = JsonUtil.toJson(result);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/build/{id}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> findBuild(@PathVariable("id") Long id) throws ParseException {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		LOGGER.info("Listing applications for " + id);
		Build result = applicationService.findBuild(id);
		Map info = new HashMap();
		info.put("sEcho", 1);
		info.put("iTotalRecords", 1);
		info.put("iTotalDisplayRecords", 1);
		info.put("aoData", result);
		String jsonData = JsonUtil.toJson(info);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/build/delete/{id}/", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> deleteBuild(@PathVariable("id") Long id) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			LOGGER.info("Deleting Build for " + id);
			applicationService.deleteBuild(id);
			response = new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error Deleting application :", e);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}
}
