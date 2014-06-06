package org.kits.trax.rest;

import java.util.ArrayList;
import java.util.List;

public class ResultDetail {

	private String log;
	private List<Input> inputs;
	private List<Output> outputs;
	
	public ResultDetail() {
	    inputs = new ArrayList<>();
	    outputs = new ArrayList<>();
    }

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public List<Input> getInputs() {
		return inputs;
	}

	public void setInputs(List<Input> inputs) {
		this.inputs = inputs;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}
}
