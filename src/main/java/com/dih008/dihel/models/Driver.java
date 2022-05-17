package com.dih008.dihel.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import com.dih008.dihel.annotations.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Driver extends Person {

	@NotEmpty(message = "Cannot be empty")
	@Type(type = "password")
	@JsonIgnore
	private String password;

	@JsonIgnore
	@OneToMany
	private List<Delivery> deliveries;
	
	public List<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

	public Driver() {}
	
	public Driver(int id, String firstName, String lastName, String email, String password) {
		super(id, firstName, lastName, email);
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
