package org.kits.trax.rest;

public class Coverage {

	private Long id;
	private Long indent;
	private Long parent;
	private String name;
	private String type;
	private Double coverage;
	private Double line;
	private Double missedLine;
	private Double branch;
	private Double missedBranch;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIndent() {
		return indent;
	}

	public void setIndent(Long indent) {
		this.indent = indent;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
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
