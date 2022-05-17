package com.dih008.dihel.repositories;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.dih008.dihel.mappers.CarMapper;
import com.dih008.dihel.mappers.CustomerMapper;
import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Customer;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.services.DeliveryService;

@Repository
@ConditionalOnProperty(
		  value="accessType", 
		  havingValue = "jdbc", 
		  matchIfMissing = true)
public class CustomerRepositoryJDBC implements RepositoryInterface<Customer> {
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("Customer").usingGeneratedKeyColumns("id")
				.usingColumns("first_name", "first_name", "email", "phone_number");
	}
	
	@PostConstruct
	public void init() {
		try (Statement stm = jdbcTemplate.getDataSource().getConnection().createStatement()) {
			stm.executeUpdate("CREATE TABLE Customer (" + "id INT NOT NULL auto_increment," 
					+ " first_name varchar(255), "
					+ " last_name varchar(255), " 
					+ " email varchar(255), " 
					+ " phone_number varchar(14), " 
					+ " PRIMARY KEY (id)" + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Customer> findAll() {
		List<Customer> customers = jdbcTemplate.query("select * from Customer", new CustomerMapper());
		for (Customer entity : customers) {
			setDeliveries(entity);
		}
		return customers;
	}

	public Customer save(Customer entity) {
		int id = 0;
		if (entity.getId() == 0) {
			id = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(entity)).intValue();
		} else {
			jdbcTemplate.update(
					"UPDATE Customer set "
					+ " first_name = ?, "
					+ " last_name = ?, "
					+ " email = ?, "
					+ " phone_number = ? "
					+ "WHERE id = ?",
					entity.getFirstName(),
					entity.getLastName(),
					entity.getEmail(),
					entity.getPhoneNumber(),
					entity.getId()
					);
		}
		entity.setId(id);
		return entity;
	}

	
	private void setDeliveries(Customer customer) {
		Map<String, String> where = new HashMap<String, String>();
		where.put("customer_id", Integer.toString(customer.getId()));
		List<Delivery> deliveries = deliveryService.findBy(where);
		customer.setDeliveries(deliveries);
		for (Delivery entity : deliveries) {
			entity.setCustomer(customer);
		}
	}

	@Override
	public Customer getById(long id) {
		Customer customer = jdbcTemplate.queryForObject("select * from Customer WHERE id = ?", new CustomerMapper(), id);
		setDeliveries(customer);
		return customer;
	}
	
	public boolean delete(long id) {
		int update = jdbcTemplate.update("DELETE FROM Customer WHERE id = ?", id);
		return update > 0;
	}
	
	public List<Customer> findBy(Map<String, String> where) {
		
		StringBuffer sqlWhere = new StringBuffer(200);
	
		String[] parameters = new String[where.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : where.entrySet()) {
			if (i != 0) {
				sqlWhere.append(" AND ");
			}			
			sqlWhere.append(entry.getKey()).append(" = ? ");
			parameters[i] = entry.getValue();
			i++;
		}
		
		return jdbcTemplate.query("select * from Customer "
				+ " WHERE " + sqlWhere, new CustomerMapper(), parameters);
	}
}
