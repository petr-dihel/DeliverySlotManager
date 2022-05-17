package com.dih008.dihel.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Driver;
import com.dih008.dihel.models.Package;

@Repository
@ConditionalOnProperty(
		  value="accessType", 
		  havingValue = "jpa", 
		  matchIfMissing = false)
public class PackageRepositoryJPA implements RepositoryInterface<Package>{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Package> findAll() {
		return em.createQuery("SELECT p FROM Package p", Package.class).getResultList();
	}

	@Transactional
	@Override
	public Package save(Package entity) {
		if (entity.getId() == 0) {
			em.persist(entity);
		} else {
			entity = em.merge(entity);
		}
		return entity;
	}

	@Override
	public Package getById(long id) {
		return em.find(Package.class, (int)id);
	}

	@Transactional
	public boolean delete(long id) {
		Package entity = this.getById(id);
		if (entity != null) {
			em.remove(entity);
			return true;
		}
		return false;
	}
	
	@Override
	public List<Package> findBy(Map<String, String> where) {
		StringBuffer sqlWhere = new StringBuffer(200);
		boolean isFirst = true;
		
		int j = 0;
		for (Map.Entry<String, String> entry : where.entrySet()) {
			j++;
			if (!isFirst) {
				sqlWhere.append(" AND ");
			}
			isFirst = false;
			sqlWhere.append(entry.getKey()).append(" = :").append(entry.getKey());
			
		}
		TypedQuery<Package> tq = em.createQuery("SELECT p FROM Package p WHERE " + sqlWhere, Package.class);
	
		for (Map.Entry<String, String> entry : where.entrySet()) {
			tq.setParameter(entry.getKey(), entry.getValue());
		}
		return tq.getResultList();
	}
}
