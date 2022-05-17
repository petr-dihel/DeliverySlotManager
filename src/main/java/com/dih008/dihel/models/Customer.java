package com.dih008.dihel.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import com.dih008.dihel.annotations.Listable;
import com.dih008.dihel.annotations.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Customer extends Person {

	@Type(type = "text")
	@NotEmpty(message = "Cannot be empty")
	@Listable
	private String phoneNumber;

	@JsonIgnore
	@OneToMany
	private List<Delivery> deliveries;
	
	public Customer() {
		super();
	}
	
	public Customer(int id, String firstName, String lastName, String email, String phoneNumber) {
		super(id, firstName, lastName, email);
		this.setPhoneNumber(phoneNumber);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}
	
}
