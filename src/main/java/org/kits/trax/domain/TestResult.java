package org.kits.trax.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private TestType testType;
	private double pass;
	private double fail;
	private double skip;
	private double success;
	private double duration;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<TestSuite> testSuites;

	public TestResult() {
		testSuites = new ArrayList<>();
	}
	
	public TestType getTestType() {
		return testType;
	}

	public void setTestType(TestType testType) {
		this.testType = testType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPass() {
		return pass;
	}

	public void setPass(double pass) {
		this.pass = pass;
	}

	public double getFail() {
		return fail;
	}

	public void setFail(double fail) {
		this.fail = fail;
	}

	public double getSkip() {
		return skip;
	}

	public void setSkip(double skip) {
		this.skip = skip;
	}

	public double getSuccess() {
		return success;
	}

	public void setSuccess(double success) {
		this.success = success;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public List<TestSuite> getTestSuites() {
		return testSuites;
	}

	public void setTestSuites(List<TestSuite> testSuites) {
		this.testSuites = testSuites;
	}
}
