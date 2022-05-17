package com.dih008.dihel.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dih008.dihel.models.Slot;
import com.dih008.dihel.repositories.RepositoryInterface;

@Service
public class SlotService {

	@Autowired
	private RepositoryInterface<Slot> repositoryInterface;
	
	public Slot getById(int id) {
		return repositoryInterface.getById(id);
	}
	
	public List<Slot> findAll() {
		return repositoryInterface.findAll();
	}
	
	public Slot save(Slot entity) {
		return repositoryInterface.save(entity);
	}
	
	public boolean delete(int id) {
		return repositoryInterface.delete(id);
	}
	
	public List<Slot> findBy(Map<String, String> where) {
		return repositoryInterface.findBy(where);
	}
}
