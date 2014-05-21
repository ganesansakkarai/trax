package org.kits.trax.rest;

public class Coverage {

	private Integer id;
	private Integer indent;
	private Integer parent;
	private String name;
	private String type;
	private Double coverage;
	private Double line;
	private Double missedLine;
	private Double branch;
	private Double missedBranch;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIndent() {
		return indent;
	}

	public void setIndent(Integer indent) {
		this.indent = indent;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getCoverage() {
		return coverage;
	}

	public void setCoverage(Double coverage) {
		this.coverage = coverage;
	}

	public Double getLine() {
		return line;
	}

	public void setLine(Double line) {
		this.line = line;
	}

	public Double getMissedLine() {
		return missedLine;
	}

	public void setMissedLine(Double missedLine) {
		this.missedLine = missedLine;
	}

	public Double getBranch() {
		return branch;
	}

	public void setBranch(Double branch) {
		this.branch = branch;
	}

	public Double getMissedBranch() {
		return missedBranch;
	}

	public void setMissedBranch(Double missedBranch) {
		this.missedBranch = missedBranch;
	}
}
