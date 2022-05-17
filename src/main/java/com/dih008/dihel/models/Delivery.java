package com.dih008.dihel.models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import com.dih008.dihel.annotations.Listable;
import com.dih008.dihel.annotations.Type;
import com.dih008.dihel.services.PackageService;
import com.dih008.dihel.validators.NotInPast;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Delivery {

	@Id
	@Listable
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "hidden")
	private int id;

	@NotEmpty(message = "Cannot be empty")
	@Type(type = "text")
	@Listable
	private String address;
	
	@Type(type = "date")
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@Listable
	@NotInPast
	private LocalDate deliveryDate;
	
	@Column(name = "car_id", insertable = false, updatable = false, nullable = true)
	private Integer carId; 
	
	@ManyToOne
	private Car car;

	@Column(name = "driver_id", insertable = false, updatable = false, nullable = true)
	private Integer driverId; 

	
	@Column(name = "customer_id", insertable = false, updatable = false, nullable = true)
	private Integer customerId; 
	
	@ManyToOne
	private Driver driver;
	
	@ManyToOne
	private Slot slot;

	@ManyToOne
	private Customer customer;
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "slot_id", insertable = false, updatable = false, nullable = true)
	private Integer slotId; 

	@OneToMany(mappedBy = "delivery")
	private List<Package> packages;
	
	public Delivery() {}
	
	public Delivery(int id, String address, LocalDate deliveryDate) {
		this.id = id;
		this.address = address;
		this.deliveryDate = deliveryDate;
	}
	
	public Delivery(int id, String address, LocalDate deliveryDate, Integer driverId, Integer slotId, Integer carId, Integer customerId) {
		System.out.println(
				"WTFFFFFFFFFFFF"
				);
		this.id = id;
		this.address = address;
		this.deliveryDate = deliveryDate;
		this.driverId = driverId;
		this.slotId = slotId;
		this.carId = carId;
		this.customerId = customerId;
	}
	
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Car getCar() {
		return car;
	}
	
	public Integer getCarId() {
		return carId;
	}
	
	public int getId() {
		return id;
	}
	
	public List<Package> getPackages() {
		return packages;
	}
	
	public Slot getSlot() {
		return slot;
	}
	
	public Integer getSlotId() {
		return slotId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	public void setPackages(List<Package> packages) {
		this.packages = packages;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDateTime) {
		this.deliveryDate = deliveryDateTime;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}
	
}
