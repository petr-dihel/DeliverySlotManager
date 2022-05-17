package com.dih008.dihel.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dih008.dihel.models.Customer;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.repositories.RepositoryInterface;

@Service
public class CustomerService {

	@Autowired
	private RepositoryInterface<Customer> repositoryInterface;
	
	public Customer getById(int id) {
		return repositoryInterface.getById(id);
	}
	
	public List<Customer> findAll() {
		return repositoryInterface.findAll();
	}
	
	public Customer save(Customer entity) {
		return repositoryInterface.save(entity);
	}
	
	public boolean delete(int id) {
		return repositoryInterface.delete(id);
	}
	
	public List<Customer> findBy(Map<String, String> where) {
		return repositoryInterface.findBy(where);
	}
	
}
