package org.kits.trax.rest;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kits.trax.domain.Coverage;
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

	@RequestMapping(value = "/coverages/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> bulkCreate(@RequestBody String json) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			List<Coverage> coverages = JsonUtil.fromJsonArray(Coverage.class, json);
			coverages = coverageService.saveCoverages(coverages);
			String jsonData = JsonUtil.toJson(coverages);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
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

		Coverage coverage = coverageService.findCoverage(id);
		if (coverage != null) {
			String jsonData = JsonUtil.toJson(coverage);
			response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		} else {
			LOGGER.error("Coverage not found for id " + id);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/coverages/", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> list() {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Coverage> result = coverageService.findAllCoverages();
		Map info = new HashMap();
		info.put("sEcho", 1);
		info.put("iTotalRecords", result.size());
		info.put("iTotalDisplayRecords", result.size());
		info.put("aaData", result);
		String jsonData = JsonUtil.toJson(info);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/coverages/{timeStamp}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listByTimeStamp(@PathVariable("timeStamp") String timeStamp) throws ParseException {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		LOGGER.info("Listing coverages for " + timeStamp);
		List<Coverage> result = coverageService.findCoverages(DateUtil.toDate(timeStamp));
		Map info = new HashMap();
		info.put("Result", "OK");
		info.put("Records", result);
		info.put("TotalRecordCount", result.size());
		String jsonData = JsonUtil.toJson(info);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/coverages/delete/{timeStamp}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> delete(@PathVariable("timeStamp") String timeStamp) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			LOGGER.info("Deleting coverages for " + timeStamp);
			List<Coverage> result = coverageService.findCoverages(DateUtil.toDate(timeStamp));
			coverageService.deleteCoverages(result);
			response = new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error Deleting coverage :", e);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}
	
	@RequestMapping(value = "/coverages/delete", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> bulkDelete() {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			LOGGER.info("Deleting all coverages");
			List<Coverage> result = coverageService.findAllCoverages();
			coverageService.deleteCoverages(result);
			response = new ResponseEntity<String>(headers, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error Deleting coverage :", e);
			response = new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return response;
	}
}
