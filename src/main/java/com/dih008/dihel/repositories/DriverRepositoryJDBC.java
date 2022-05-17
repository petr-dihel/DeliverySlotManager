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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.dih008.dihel.mappers.CarMapper;
import com.dih008.dihel.mappers.DeliveryMapper;
import com.dih008.dihel.mappers.DriverMapper;
import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Customer;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Driver;
import com.dih008.dihel.services.DeliveryService;

@Repository
@ConditionalOnProperty(
		  value="accessType", 
		  havingValue = "jdbc", 
		  matchIfMissing = true)
public class DriverRepositoryJDBC implements RepositoryInterface<Driver> {

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("Driver").usingGeneratedKeyColumns("id")
				.usingColumns("first_name", "last_name", "email", "password");
	}
	
	@PostConstruct
	public void init() {
		try (Statement stm = jdbcTemplate.getDataSource().getConnection().createStatement()) {
			stm.executeUpdate("CREATE TABLE Driver (" + "id INT NOT NULL auto_increment," 
					+ " first_name varchar(255), "
					+ " last_name varchar(255), "  
					+ " email varchar(255), "  
					+ " password varchar(2555), " 
					+" PRIMARY KEY (id)" + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void setDeliveries(Driver driver) {
		Map<String, String> where = new HashMap<String, String>();
		where.put("driver_id", Integer.toString(driver.getId()));
		List<Delivery> deliveries = deliveryService.findBy(where);
		driver.setDeliveries(deliveries);
		for (Delivery entity : deliveries) {
			entity.setDriver(driver);
		}
	}
	
	public List<Driver> findAll() 
	{
		List<Driver> drivers = jdbcTemplate.query("select * from Driver", new DriverMapper());
		for (Driver driver : drivers) {
			setDeliveries(driver);
		}
		return drivers;
	}

	public Driver save(Driver entity) {
		int id = 0;
		if (entity.getId() == 0) {
			id = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(entity)).intValue();
		} else {
			jdbcTemplate.update(
					"UPDATE Driver SET "
					+ "	first_name = ?,"
					+ " last_name = ?, "
					+ " email = ?, "
					+ " password = ? "
					+ " WHERE id = ?",
					entity.getFirstName(),
					entity.getLastName(),
					entity.getEmail(),
					entity.getPassword(),
					entity.getId()
					);
		}
		entity.setId(id);
		return entity;
	}

	@Override
	public Driver getById(long id) {
		Driver driver = jdbcTemplate.queryForObject("select * from Driver WHERE id = ?", new DriverMapper(), id);
		setDeliveries(driver);
		return driver;
	}
	
	public boolean delete(long id) {
		int update = jdbcTemplate.update("DELETE FROM Driver WHERE id = ?", id);
		return update > 0;
	}
	
	public List<Driver> findBy(Map<String, String> where) {
		
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
		
		return jdbcTemplate.query("select * from Driver  "
				+ " WHERE " + sqlWhere, new DriverMapper(), parameters);
	}
}
