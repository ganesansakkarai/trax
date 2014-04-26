package org.kits.trax.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Build {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date timeStamp;
	private String name;
	@ManyToOne(fetch = FetchType.EAGER)
	private Build parent;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "BUILD_TEST_RESULT", joinColumns = @JoinColumn(name = "BUILD_ID"), inverseJoinColumns = @JoinColumn(name = "TEST_RESULT_ID"))
	private List<TestResult> testResults;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "BUILD_TEST_COVERAGE", joinColumns = @JoinColumn(name = "BUILD_ID"), inverseJoinColumns = @JoinColumn(name = "TEST_COVERAGE_ID"))
	private List<TestCoverage> testCoverages;
	
	public Build() {		
		testResults = new ArrayList<>();
		testCoverages = new ArrayList<>();
	}
	
	public Build(Long id, String name, Date timeStamp) {
		this.id = id;
		this.name = name;
		this.timeStamp = timeStamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Build getParent() {
		return parent;
	}

	public void setParent(Build parent) {
		this.parent = parent;
	}

	public List<TestResult> getTestResults() {
		return testResults;
	}

	public void setTestResults(List<TestResult> testResults) {
		this.testResults = testResults;
	}

	public List<TestCoverage> getTestCoverages() {
		return testCoverages;
	}

	public void setTestCoverages(List<TestCoverage> testCoverages) {
		this.testCoverages = testCoverages;
	}
	
	public String toString() {
		return String.valueOf(id);
	}
}
