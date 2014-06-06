package org.kits.trax.rest;

public class Result {

	private Long id;
	private Long indent;
	private Long parent;
	private String name;
	private String type;
	private Long duration;
	private Double success;
	private Double total;
	private Double pass;
	private Double skip;
	private Double fail;
	private String status;
	private ResultDetail detail;

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

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Double getSuccess() {
		return success;
	}

	public void setSuccess(Double success) {
		this.success = success;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getPass() {
		return pass;
	}

	public void setPass(Double pass) {
		this.pass = pass;
	}

	public Double getSkip() {
		return skip;
	}

	public void setSkip(Double skip) {
		this.skip = skip;
	}

	public Double getFail() {
		return fail;
	}

	public void setFail(Double fail) {
		this.fail = fail;
	}

	public ResultDetail getDetail() {
		return detail;
	}

	public void setDetail(ResultDetail detail) {
		this.detail = detail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
