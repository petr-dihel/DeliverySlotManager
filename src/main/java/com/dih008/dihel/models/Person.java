package com.dih008.dihel.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;

import com.dih008.dihel.annotations.Listable;
import com.dih008.dihel.annotations.Type;

@MappedSuperclass
public class Person {
	
	@Listable
	@Id
	@Type(type = "hidden")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Listable
	@NotEmpty(message = "Cannot be empty")
	@Type(type = "text")
	private String firstName;
	
	@Listable
	@Type(type = "text")
	@NotEmpty(message = "Cannot be empty")
	private String lastName;
	
	@Listable
	@Type(type = "email")
	@NotEmpty(message = "Cannot be empty")
	private String email;

	public Person() {}
			
	public Person(int id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
