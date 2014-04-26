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
public class TestCoverage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private TestType testType;
	private double line;
	private double missedLine;
	private double branch;
	private double missedBranch;
	private double coverage;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Clazz> clazzes;
	
	public TestCoverage() {		
		clazzes = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TestType getTestType() {
		return testType;
	}

	public void setTestType(TestType testType) {
		this.testType = testType;
	}

	public double getLine() {
		return line;
	}

	public void setLine(double line) {
		this.line = line;
	}

	public double getMissedLine() {
		return missedLine;
	}

	public void setMissedLine(double missedLine) {
		this.missedLine = missedLine;
	}

	public double getBranch() {
		return branch;
	}

	public void setBranch(double branch) {
		this.branch = branch;
	}

	public double getMissedBranch() {
		return missedBranch;
	}

	public void setMissedBranch(double missedBranch) {
		this.missedBranch = missedBranch;
	}

	public double getCoverage() {
		return coverage;
	}

	public void setCoverage(double coverage) {
		this.coverage = coverage;
	}

	public List<Clazz> getClazzes() {
		return clazzes;
	}

	public void setClazzes(List<Clazz> clazzes) {
		this.clazzes = clazzes;
	}
}
