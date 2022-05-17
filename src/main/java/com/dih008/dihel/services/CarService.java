package com.dih008.dihel.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.repositories.RepositoryInterface;

@Service
public class CarService {

	@Autowired
	private RepositoryInterface<Car> repositoryInterface;
	
	public Car getById(int id) {
		return repositoryInterface.getById(id);
	}
	
	public List<Car> findAll() {
		return repositoryInterface.findAll();
	}
	
	public boolean delete(int id) {
		return repositoryInterface.delete(id);
	}
	
	public Car save(Car entity) {
		return repositoryInterface.save(entity);
	}
	
	public List<Car> findBy(Map<String, String> where) {
		return repositoryInterface.findBy(where);
	}
}
