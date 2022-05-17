package com.dih008.dihel.controllers.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Customer;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Driver;
import com.dih008.dihel.models.Slot;
import com.dih008.dihel.services.CarService;
import com.dih008.dihel.services.CustomerService;
import com.dih008.dihel.services.DeliveryService;
import com.dih008.dihel.services.DriverService;
import com.dih008.dihel.services.SlotService;

@RestController
public class DeliveryRestController {

	@Autowired
	DeliveryService deliveryService;

	@Autowired
	CarService carService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	SlotService slotService;
	
	@Autowired
	DriverService driverService;
	
	@GetMapping("api/v1/deliveries")
	public List<Delivery> findAll(){
		return deliveryService.findAll(); 
	}
	
	@GetMapping("api/v1/deliveries/{id}")
	public Delivery getById(@PathVariable long id)
	{
		return deliveryService.getById((int)id);
	}
	
	@PostMapping("api/v1/deliveries")
	public ResponseEntity<Delivery> newEntity(@RequestBody Delivery entity)
	{
		if (entity.getCar() != null && entity.getCarId() == null) {
			Car carEntity = carService.save(entity.getCar());
			entity.setCar(carEntity);
			entity.setCarId(carEntity.getId());
		}
		
		if (entity.getCustomer() != null && entity.getCustomerId() == null) {
			Customer customerEntity = customerService.save(entity.getCustomer());
			entity.setCustomer(customerEntity);
			entity.setCustomerId(customerEntity.getId());
		}
		
		if (entity.getSlot() != null && entity.getSlotId() == null) {
			Slot slotEntity = slotService.save(entity.getSlot());
			entity.setSlot(slotEntity);
			entity.setSlotId(slotEntity.getId());
		}
		
		if (entity.getDriver() != null && entity.getDriverId() == null) {
			Driver driverEntity = driverService.save(entity.getDriver());
			entity.setDriver(driverEntity);
			entity.setDriverId(driverEntity.getId());
		}
		
		Delivery newEntity = deliveryService.save(entity);
		return ResponseEntity.ok(newEntity);
	}
	
	@PutMapping("api/v1/deliveries/{id}")
	public ResponseEntity<Delivery> updateEntity(@RequestBody Delivery entity)
	{
		Delivery newEntity = deliveryService.save(entity);
		return ResponseEntity.ok(newEntity);
	}
	
	
}
