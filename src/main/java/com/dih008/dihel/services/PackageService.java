package com.dih008.dihel.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dih008.dihel.models.Driver;
import com.dih008.dihel.models.Package;
import com.dih008.dihel.repositories.RepositoryInterface;

@Service
public class PackageService {

	@Autowired
	private RepositoryInterface<Package> repositoryInterface;
	
	public Package getById(int id) {
		return repositoryInterface.getById(id);
	}
	
	public List<Package> findAll() {
		return repositoryInterface.findAll();
	}
	
	public Package save(Package entity) {
		return repositoryInterface.save(entity);
	}
	
	public boolean delete(int id) {
		return repositoryInterface.delete(id);
	}
	
	public List<Package> findBy(Map<String, String> where) {
		return repositoryInterface.findBy(where);
	}
}
