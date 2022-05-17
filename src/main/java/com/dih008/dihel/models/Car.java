package com.dih008.dihel.models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import com.dih008.dihel.annotations.Listable;
import com.dih008.dihel.annotations.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "hidden")
	@Listable
	private int id;
	
	@Listable
	@NotEmpty(message = "Cannot be empty")
	@Type(type = "text")
	private String type;
	
	@Listable
	@NotEmpty(message = "Cannot be empty")
	@Type(type = "text")
	private String licensePlate;
	
	@JsonIgnore
	@OneToMany
	private List<Delivery> deliveries;
	
	@Transient
	private boolean realtionsLoaded = false;
	
	public Car() {	
	}
	
	public Car(int id, String type, String licensePlate) {	
		this.id = id;
		this.type = type;
		this.licensePlate = licensePlate;
	}

	public List<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setName(String type) {
		this.type = type;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

}
