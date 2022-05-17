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
import org.springframework.stereotype.Repository;

import com.dih008.dihel.mappers.CarMapper;
import com.dih008.dihel.mappers.DeliveryMapper;
import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.services.DeliveryService;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Repository
@ConditionalOnProperty(
		  value="accessType", 
		  havingValue = "jdbc", 
		  matchIfMissing = true)
public class CarRepositoryJDBC implements RepositoryInterface<Car> {

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("Car").usingGeneratedKeyColumns("id")
				.usingColumns("type", "license_plate");
	}
	
	@PostConstruct
	public void init() {
		try (Statement stm = jdbcTemplate.getDataSource().getConnection().createStatement()) {
			stm.executeUpdate("CREATE TABLE Car (" + "id INT NOT NULL auto_increment," + " type varchar(255), "
					+ "license_plate varchar(255), " + " PRIMARY KEY (id)" + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void setDeliveries(Car car) {
		Map<String, String> where = new HashMap<String, String>();
		where.put("car_id", Integer.toString(car.getId()));
		List<Delivery> deliveries = deliveryService.findBy(where);
		car.setDeliveries(deliveries);
		for (Delivery entity : deliveries) {
			entity.setCar(car);
		}
	}
	
	public List<Car> findAll() {
		List<Car> cars = jdbcTemplate.query("select * from Car", new CarMapper());
		for (Car car : cars) {
			setDeliveries(car);
		}
		return cars;
	}

	public Car save(Car entity) {
		int id = 0;
		
		if (entity.getId() == 0) {
			id = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(entity)).intValue();
		} else {
			jdbcTemplate.update(
					"UPDATE Car set type = ?, license_plate = ? WHERE id = ?",
					entity.getType(), entity.getLicensePlate(), entity.getId()
					);
		}
		entity.setId(id);
		return entity;
	}

	@Override
	public Car getById(long id) {
		Car car = jdbcTemplate.queryForObject("select * from Car WHERE id = ?", new CarMapper(), id);
		setDeliveries(car);
		return car;
	}
	
	public boolean delete(long id) {
		int update = jdbcTemplate.update("DELETE FROM Car WHERE id = ?", id);
		return update > 0;
	}
	
	public List<Car> findBy(Map<String, String> where) {
		
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
		
		return jdbcTemplate.query("select * from Car "
				+ " WHERE " + sqlWhere, new CarMapper(), parameters);
	}
	
}
