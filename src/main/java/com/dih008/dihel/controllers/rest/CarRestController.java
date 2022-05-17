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
import com.dih008.dihel.services.CarService;

@RestController
public class CarRestController {
	
	@Autowired
	CarService carService;
	
	@GetMapping("api/v1/cars")
	public List<Car> findAll(){
		return carService.findAll(); 
	}
	
	@GetMapping("api/v1/cars/{id}")
	public Car getById(@PathVariable long id)
	{
		return carService.getById((int)id);
	}
	
	@PostMapping("api/v1/cars")
	public ResponseEntity<Car> newCar(@RequestBody Car car)
	{
		Car newCar = carService.save(car);
		return ResponseEntity.ok(newCar);
	}
	
	@PutMapping("api/v1/cars/{id}")
	public ResponseEntity<Car> updateCar(@RequestBody Car car)
	{
		Car newCar = carService.save(car);
		return ResponseEntity.ok(newCar);
	}
	
}
