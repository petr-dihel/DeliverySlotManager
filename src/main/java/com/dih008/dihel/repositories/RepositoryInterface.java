package com.dih008.dihel.repositories;

import java.util.List;
import java.util.Map;

import com.dih008.dihel.models.Delivery;

public interface RepositoryInterface<T> 
{
	public List<T> findAll();
	public T save(T entity);
	public T getById(long id);
	public boolean delete(long id);
	public List<T> findBy(Map<String, String> where);
}
