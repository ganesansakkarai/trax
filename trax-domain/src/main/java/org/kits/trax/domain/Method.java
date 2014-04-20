package org.kits.trax.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Method {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private double line;
	private double missedLine;
	private double branch;
	private double missedBranch;
	private double coverage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String methodName) {
		this.name = methodName;
	}

	public double getLine() {
		return line;
	}

	public void setLine(double coveredLine) {
		this.line = coveredLine;
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

	public void setBranch(double coveredBranch) {
		this.branch = coveredBranch;
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
}
