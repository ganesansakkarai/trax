package org.kits.trax.domain;

import java.text.DecimalFormat;

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

	public void setName(String methodName) {
		this.name = methodName;
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

	public String getCoverage() {

		double coverage = (getLine() - getMissedLine()) / getLine() * 100;
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(coverage);
	}
}
