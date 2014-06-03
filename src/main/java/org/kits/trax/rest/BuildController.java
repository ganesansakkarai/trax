package org.kits.trax.rest;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.kits.trax.domain.Build;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.TestCase;
import org.kits.trax.domain.TestCoverage;
import org.kits.trax.domain.TestResult;
import org.kits.trax.domain.TestSuite;
import org.kits.trax.service.BuildService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuildController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BuildController.class);
	@Autowired
	private BuildService buildService;
	DecimalFormat format = new DecimalFormat("0.00");

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
			LOGGER.error("Error adding build", e);
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

	@RequestMapping(value = "/apps", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> listApplications() {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<String> applications = buildService.listApplications();
		String jsonData = JsonUtil.toJsonArray(applications);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/app/{name}/builds", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> listBuilds(@PathVariable("name") String name) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Build> builds = buildService.listBuilds(name);
		List<AppBuild> appBuilds = new ArrayList<>();
		for (Build build : builds) {
			AppBuild appBuild = new AppBuild();
			appBuild.setId(build.getId());
			appBuild.setDate(DateUtil.toString(build.getTimeStamp()));
			appBuilds.add(appBuild);
		}
		String jsonData = JsonUtil.toJsonArray(appBuilds);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/build/{id}/coverage", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> coverage(@PathVariable("id") Long id) throws ParseException {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		LOGGER.info("Listing modules for build " + id);
		Build build = buildService.findBuild(id);
		List<Coverage> coverages = new ArrayList<>();
		getCoverages(0l, null, build, coverages);
		String jsonData = JsonUtil.toJsonArray(coverages);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/build/{id}/result", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> result(@PathVariable("id") Long id) throws ParseException {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		LOGGER.info("Listing modules for build " + id);
		Build build = buildService.findBuild(id);
		List<Result> results = getResult(build);
		for(Build module : build.getModules()) {
			results.addAll(getResult(module));
		}
		String jsonData = JsonUtil.toJsonArray(results);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}

	private void getCoverages(Long indent, Long parent, Build build, List<Coverage> coverages) {
		
		for (TestCoverage testCoverage : build.getTestCoverages()) {
			Coverage moduleCoverage = new Coverage();
			moduleCoverage.setId(new Long(coverages.size()));
			moduleCoverage.setIndent(indent);
			if(parent != null) {
				moduleCoverage.setParent(parent);
			}
			moduleCoverage.setName(build.getName());
			moduleCoverage.setType(testCoverage.getTestType());
			moduleCoverage.setCoverage(Double.parseDouble(format.format(testCoverage.getCoverage())));
			moduleCoverage.setLine(testCoverage.getLine());
			moduleCoverage.setMissedLine(testCoverage.getMissedLine());
			moduleCoverage.setBranch(testCoverage.getBranch());
			moduleCoverage.setMissedBranch(testCoverage.getMissedBranch());
			coverages.add(moduleCoverage);

			for (Clazz clazz : testCoverage.getClazzes()) {

				Coverage clazzCoverage = new Coverage();
				clazzCoverage.setId(new Long(coverages.size()));
				clazzCoverage.setIndent(moduleCoverage.getIndent() + 1);
				clazzCoverage.setParent(moduleCoverage.getId());
				clazzCoverage.setType(testCoverage.getTestType());
				clazzCoverage.setName(clazz.getName());
				clazzCoverage.setCoverage(Double.parseDouble(format.format(clazz.getCoverage())));
				clazzCoverage.setLine(clazz.getLine());
				clazzCoverage.setMissedLine(clazz.getMissedLine());
				clazzCoverage.setBranch(clazz.getBranch());
				clazzCoverage.setMissedBranch(clazz.getMissedBranch());
				coverages.add(clazzCoverage);

				for (Method method : clazz.getMethods()) {
					Coverage methodCoverage = new Coverage();
					methodCoverage.setId(new Long(coverages.size()));
					methodCoverage.setIndent(clazzCoverage.getIndent() + 1);
					methodCoverage.setParent(clazzCoverage.getId());
					methodCoverage.setName(method.getName());
					methodCoverage.setType(testCoverage.getTestType());
					methodCoverage.setCoverage(Double.parseDouble(format.format(method.getCoverage())));
					methodCoverage.setLine(method.getLine());
					methodCoverage.setMissedLine(method.getMissedLine());
					methodCoverage.setBranch(method.getBranch());
					methodCoverage.setMissedBranch(method.getMissedBranch());
					coverages.add(methodCoverage);
				}
			}
			
			if(build.getModules() != null) {
				for(Build module: build.getModules()) {
					getCoverages(indent + 1, moduleCoverage.getId(), module, coverages);
				}
			}
		}		
	}

	private List<Result> getResult(Build build) {

		List<Result> results = new ArrayList<>();
		for (TestResult testResult : build.getTestResults()) {

			Result moduleResult = new Result();
			moduleResult.setId(results.size() + 1);
			moduleResult.setIndent(0);
			moduleResult.setParent(null);
			moduleResult.setName(build.getName());
			moduleResult.setType(testResult.getTestType());
			moduleResult.setPass(testResult.getPass());
			moduleResult.setFail(testResult.getFail());
			moduleResult.setSkip(testResult.getSkip());
			double total = moduleResult.getPass() + moduleResult.getFail() + moduleResult.getSkip();
			moduleResult.setSuccess(moduleResult.getPass() / total * 100);
			results.add(moduleResult);

			for (TestSuite testSuite : testResult.getTestSuites()) {

				Result suiteResult = new Result();
				suiteResult.setId(results.size() + 1);
				suiteResult.setIndent(0);
				suiteResult.setParent(null);
				suiteResult.setName(testSuite.getName());
				suiteResult.setType(testResult.getTestType());
				suiteResult.setPass(testSuite.getPass());
				suiteResult.setFail(testSuite.getFail());
				suiteResult.setSkip(testSuite.getSkip());
				total = suiteResult.getPass() + suiteResult.getFail() + suiteResult.getSkip();
				suiteResult.setSuccess(suiteResult.getPass() / total * 100);
				results.add(suiteResult);

				for (TestCase testCase : testSuite.getTestCases()) {
					Result methodResult = new Result();
					methodResult.setId(results.size() + 1);
					methodResult.setIndent(0);
					methodResult.setParent(null);
					methodResult.setName(testCase.getName());
					methodResult.setType(testResult.getTestType());
					results.add(methodResult);
				}
			}
		}

		return results;
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

	@RequestMapping(value = "/app/{name}/coverage/trend", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> coverageTrend(@PathVariable("name") String name) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Build> builds = buildService.listCoverages(name);
		List coverages = new ArrayList<>();
		List unitCoverages = new ArrayList<>();
		List integrationCoverages = new ArrayList<>();
		coverages.add(unitCoverages);
		coverages.add(integrationCoverages);
		for(Build build : builds) {
			for(TestCoverage testCoverage : build.getTestCoverages()) {
				if(testCoverage.getTestType().equalsIgnoreCase("Unit")) {
					unitCoverages.add(testCoverage.getCoverage());
				} else if(testCoverage.getTestType().equalsIgnoreCase("Integration")) {
					integrationCoverages.add(testCoverage.getCoverage());
				}
			}
		}
		String jsonData = JsonUtil.toJsonArray(coverages);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/build/{id}/result/summary", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> resultSummary(@PathVariable("id") Long id) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Build build = buildService.findBuild(id);
		String jsonData = JsonUtil.toJsonArray(build.getTestResults());
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}
}
