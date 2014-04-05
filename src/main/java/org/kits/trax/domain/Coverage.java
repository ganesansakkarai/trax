package org.kits.trax.domain;

import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Coverage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	private TestType testType;
	private long line;
	private long missedLine;
	private long branch;
	private long missedBranch;

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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getLine() {
		return line;
	}

	public void setLine(long coveredLine) {
		this.line = coveredLine;
	}

	public long getMissedLine() {
		return missedLine;
	}

	public void setMissedLine(long missedLine) {
		this.missedLine = missedLine;
	}

	public long getBranch() {
		return branch;
	}

	public void setBranch(long coveredBranch) {
		this.branch = coveredBranch;
	}

	public long getMissedBranch() {
		return missedBranch;
	}

	public void setMissedBranch(long missedBranch) {
		this.missedBranch = missedBranch;
	}

	public TestType getTestType() {
		return testType;
	}

	public void setTestType(TestType testType) {
		this.testType = testType;
	}

	public String getCoverage() {

		double coverage = (getLine() - getMissedLine()) / getLine() * 100;
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(coverage);
	}
}
