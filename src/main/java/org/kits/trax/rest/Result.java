package org.kits.trax.rest;

public class Result {

	private Integer id;
	private Integer indent;
	private Integer parent;
	private String name;
	private String type;
	private double success;
	private double pass;
	private double skip;
	private double fail;

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

	public double getSuccess() {
		return success;
	}

	public void setSuccess(double success) {
		this.success = success;
	}

	public double getPass() {
		return pass;
	}

	public void setPass(double pass) {
		this.pass = pass;
	}

	public double getSkip() {
		return skip;
	}

	public void setSkip(double skip) {
		this.skip = skip;
	}

	public double getFail() {
		return fail;
	}

	public void setFail(double fail) {
		this.fail = fail;
	}
}
