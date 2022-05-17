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

import com.dih008.dihel.models.Customer;
import com.dih008.dihel.models.Delivery;

@Repository
@ConditionalOnProperty(
		  value="accessType", 
		  havingValue = "jpa", 
		  matchIfMissing = false)
public class DeliveryRepositoryJPA implements RepositoryInterface<Delivery> 
{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Delivery> findAll() {
		return em.createQuery("SELECT p FROM Delivery p", Delivery.class).getResultList();
	}

	@Transactional
	@Override
	public Delivery save(Delivery entity) {
		if (entity.getId() == 0) {
			em.persist(entity);
		} else {
			entity = em.merge(entity);
		}
		return entity;
	}

	@Override
	public Delivery getById(long id) {
		return em.find(Delivery.class, (int)id);
	}
	
	@Override
	public List<Delivery> findBy(Map<String, String> where) {
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
		TypedQuery<Delivery> tq = em.createQuery("SELECT p FROM Delivery p WHERE " + sqlWhere, Delivery.class);
	
		for (Map.Entry<String, String> entry : where.entrySet()) {
			tq.setParameter(entry.getKey(), entry.getValue());
		}
		return tq.getResultList();
	}
	
	@Transactional
	public boolean delete(long id) {
		Delivery entity = this.getById(id);
		if (entity != null) {
			em.remove(entity);
			return true;
		}
		return false;
	}
}
