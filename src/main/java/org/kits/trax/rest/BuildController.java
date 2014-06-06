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
	DecimalFormat nonDecimalFormat = new DecimalFormat("0");

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

	@RequestMapping(value = "/build/{name}/module", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> saveModule(@PathVariable String name, @RequestBody String json) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		try {
			Build build = JsonUtil.fromJson(Build.class, json);
			build = buildService.saveModule(name, build);
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

	@RequestMapping(value = "/build/{id}/result", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> result(@PathVariable("id") Long id) throws ParseException {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		LOGGER.info("Listing modules for build " + id);
		Build build = buildService.findBuild(id);
		List<Result> results = new ArrayList<>();
		getResults(0l, null, build, results);
		String jsonData = JsonUtil.toJsonArray(results);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}

	private void getCoverages(Long indent, Long parent, Build build, List<Coverage> coverages) {

		for (TestCoverage testCoverage : build.getTestCoverages()) {
			Coverage moduleCoverage = new Coverage();
			moduleCoverage.setId(new Long(coverages.size()));
			moduleCoverage.setIndent(indent);
			if (parent != null) {
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

			if (build.getModules() != null) {
				for (Build module : build.getModules()) {
					getCoverages(indent + 1, moduleCoverage.getId(), module, coverages);
				}
			}
		}
	}

	private void getResults(Long indent, Long parent, Build build, List<Result> results) {

		for (TestResult testResult : build.getTestResults()) {
			Result moduleResult = new Result();
			moduleResult.setId(new Long(results.size()));
			moduleResult.setIndent(indent);
			if (parent != null) {
				moduleResult.setParent(parent);
			}
			moduleResult.setName(build.getName());
			moduleResult.setType(testResult.getTestType());
			moduleResult.setSuccess(Double.parseDouble(format.format(testResult.getSuccess())));
			moduleResult.setTotal(testResult.getPass() + testResult.getFail() + testResult.getSkip());
			moduleResult.setPass(testResult.getPass());
			moduleResult.setSkip(testResult.getSkip());
			moduleResult.setFail(testResult.getFail());
			moduleResult.setDuration(Long.parseLong(nonDecimalFormat.format(testResult.getDuration())));
			results.add(moduleResult);

			for (TestSuite testSuite : testResult.getTestSuites()) {

				Result suiteResult = new Result();
				suiteResult.setId(new Long(results.size()));
				suiteResult.setIndent(moduleResult.getIndent() + 1);
				suiteResult.setParent(moduleResult.getId());
				suiteResult.setName(testSuite.getName());
				suiteResult.setType(testResult.getTestType());
				suiteResult.setSuccess(Double.parseDouble(format.format(testSuite.getSuccess())));
				suiteResult.setTotal(testSuite.getPass() + testSuite.getSkip() + testSuite.getFail());
				suiteResult.setPass(testSuite.getPass());
				suiteResult.setSkip(testSuite.getSkip());
				suiteResult.setFail(testSuite.getFail());
				suiteResult.setDuration(Long.parseLong(nonDecimalFormat.format(testSuite.getDuration())));
				results.add(suiteResult);

				for (TestCase testCase : testSuite.getTestCases()) {

					Result testCaseResult = new Result();
					testCaseResult.setId(new Long(results.size()));
					testCaseResult.setIndent(suiteResult.getIndent() + 1);
					testCaseResult.setParent(suiteResult.getId());
					testCaseResult.setName(testCase.getName());
					testCaseResult.setType(testResult.getTestType());
					testCaseResult.setDuration(Long.parseLong(nonDecimalFormat.format(testCase.getDuration())));
					testCaseResult.setStatus(testCase.getStatus());
					
					ResultDetail detail = new ResultDetail();
					for(org.kits.trax.domain.Input input : testCase.getInputs()) {
						Input aInput = new Input();
						aInput.setName(input.getName());
						aInput.setValue(input.getValue());
						detail.getInputs().add(aInput);
					}
					
					for(org.kits.trax.domain.Output output : testCase.getOutputs()) {
						Output aOutput = new Output();
						aOutput.setName(output.getName());
						aOutput.setActual(output.getActual());
						aOutput.setExpected(output.getExpected());
						aOutput.setMatch(output.isPass());
						detail.getOutputs().add(aOutput);
					}
					testCaseResult.setDetail(detail);
					results.add(testCaseResult);
				}
			}

			if (build.getModules() != null) {
				for (Build module : build.getModules()) {
					getResults(indent + 1, moduleResult.getId(), module, results);
				}
			}
		}
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/app/{name}/coverage/trend", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> coverageTrend(@PathVariable("name") String name) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Build> builds = buildService.listTrend(name);
		List coverages = new ArrayList<>();
		List unitCoverages = new ArrayList<>();
		List integrationCoverages = new ArrayList<>();
		coverages.add(unitCoverages);
		coverages.add(integrationCoverages);
		for (Build build : builds) {
			for (TestCoverage testCoverage : build.getTestCoverages()) {
				if (testCoverage.getTestType().equalsIgnoreCase("Unit")) {
					unitCoverages.add(testCoverage.getCoverage());
				} else if (testCoverage.getTestType().equalsIgnoreCase("Integration")) {
					integrationCoverages.add(testCoverage.getCoverage());
				}
			}
		}
		String jsonData = JsonUtil.toJsonArray(coverages);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/app/{name}/result/trend", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> resultTrend(@PathVariable("name") String name) {

		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Build> builds = buildService.listTrend(name);
		List results = new ArrayList<>();
		List unitResults = new ArrayList<>();
		List integrationResults = new ArrayList<>();
		results.add(unitResults);
		results.add(integrationResults);
		for (Build build : builds) {
			for (TestResult testResult : build.getTestResults()) {
				if (testResult.getTestType().equalsIgnoreCase("Unit")) {
					unitResults.add(testResult.getSuccess());
				} else if (testResult.getTestType().equalsIgnoreCase("Integration")) {
					integrationResults.add(testResult.getSuccess());
				}
			}
		}
		String jsonData = JsonUtil.toJsonArray(results);
		response = new ResponseEntity<String>(jsonData, headers, HttpStatus.OK);
		return response;
	}
}
