package com.dih008.dihel.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.dih008.dihel.annotations.Listable;
import com.dih008.dihel.annotations.Type;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Package {

	@Listable
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "hidden")
	private int id;

	@Listable
	@Type(type = "text")
	private float weigth;

	@Listable
	@Type(type = "text")
	@NotEmpty(message = "Cannot be empty")
	private String number;
	
	@Column(name = "delivery_id", insertable = false, updatable = false)
	private Integer deliveryId;
	
	@JsonIgnore
	@ManyToOne
	private Delivery delivery;

	public Package() {}
	
	public Package(int id, float weigth, String number) {
		this.id = id;
		this.weigth = weigth;
		this.number = number;
	}
	
	public Package(int id, float weigth, String number, Integer deliveryId) {
		this.id = id;
		this.weigth = weigth;
		this.number = number;
		this.deliveryId = deliveryId;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public float getWeigth() {
		return weigth;
	}

	public void setWeigth(float weigth) {
		this.weigth = weigth;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}
	
}
