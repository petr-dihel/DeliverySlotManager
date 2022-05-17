package com.dih008.dihel.controllers;

public class FlashMessage {
	
	private String message;
	private String type; // success, primary, danger, warining./..
	
	public FlashMessage(String message, String type) {
		this.message = message;
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}
