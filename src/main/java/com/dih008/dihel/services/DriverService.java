package com.dih008.dihel.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dih008.dihel.models.Driver;
import com.dih008.dihel.repositories.RepositoryInterface;

@Service
public class DriverService {

	@Autowired
	private RepositoryInterface<Driver> repositoryInterface;
	
	public Driver getById(int id) {
		return repositoryInterface.getById(id);
	}
	
	public List<Driver> findAll() {
		return repositoryInterface.findAll();
	}
	
	public Driver save(Driver entity) {
		return repositoryInterface.save(entity);
	}
	
	public boolean delete(int id) {
		return repositoryInterface.delete(id);
	}
	
	public List<Driver> findBy(Map<String, String> where) {
		return repositoryInterface.findBy(where);
	}
}
