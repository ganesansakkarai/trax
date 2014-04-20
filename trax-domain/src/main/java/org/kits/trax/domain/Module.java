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
import javax.persistence.OneToMany;

@Entity
public class Module {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "MOD_CLASS", joinColumns = @JoinColumn(name = "MOD_ID"), inverseJoinColumns = @JoinColumn(name = "CLASS_ID"))
	private List<Clazz> clazzes;
	private double line;
	private double missedLine;
	private double branch;
	private double missedBranch;
	private double coverage;

	private double pass;
	private double fail;
	private double skip;
	private double success;
	private Date startTime;
	private Date endTime;
	private long duration;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "MOD_SUITE", joinColumns = @JoinColumn(name = "MOD_ID"), inverseJoinColumns = @JoinColumn(name = "SUITE_ID"))
	private List<TestSuite> testSuites;

	public Module() {
		clazzes = new ArrayList<>();
		testSuites = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Clazz> getClazzes() {
		return clazzes;
	}

	public void setClazzes(List<Clazz> clazzes) {
		this.clazzes = clazzes;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public List<TestSuite> getTestSuites() {
		return testSuites;
	}

	public void setTestSuites(List<TestSuite> testSuites) {
		this.testSuites = testSuites;
	}
}
