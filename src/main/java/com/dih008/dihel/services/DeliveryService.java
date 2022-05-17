package com.dih008.dihel.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.repositories.RepositoryInterface;

@Service
public class DeliveryService {

	@Autowired
	private RepositoryInterface<Delivery> repositoryInterface;
	
	public Delivery getById(int id) {
		return repositoryInterface.getById(id);
	}
	
	public List<Delivery> findAll() {
		return repositoryInterface.findAll();
	}
	
	public List<Delivery> findBy(Map<String, String> where) {
		return repositoryInterface.findBy(where);
	}
	
	public Delivery save(Delivery entity) {
		return repositoryInterface.save(entity);
	}
	
	public boolean delete(int id) {
		return repositoryInterface.delete(id);
	}
}
