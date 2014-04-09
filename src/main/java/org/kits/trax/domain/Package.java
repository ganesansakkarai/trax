package org.kits.trax.domain;

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
public class Package {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "PKG_CLASS", joinColumns = @JoinColumn(name = "PKG_ID"), inverseJoinColumns = @JoinColumn(name = "CLASS_ID"))
	private List<Clazz> clazzes;
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
}
