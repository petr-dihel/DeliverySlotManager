package com.dih008.dihel.form;

import java.util.List;
import java.util.Map;

public class SelectInput {
	
	public String name;
	
	public Map<Integer, String> options;
	
	public List<Integer> selected;
	
	public boolean isMultiSelect;
	
	public SelectInput(String name, Map<Integer, String> options, boolean isMultiSelect, List<Integer> selected) {
		this.name = name;
		this.options = options;
		this.selected = selected;
		this.isMultiSelect = isMultiSelect;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, String> getOptions() {
		return options;
	}

	public void setOptions(Map<Integer, String> options) {
		this.options = options;
	}

	public List<Integer> getSelected() {
		return selected;
	}

	public void setSelected(List<Integer> selected) {
		this.selected = selected;
	}
	
}
