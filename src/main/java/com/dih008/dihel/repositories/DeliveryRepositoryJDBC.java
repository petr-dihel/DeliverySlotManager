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

import com.dih008.dihel.mappers.CustomerMapper;
import com.dih008.dihel.mappers.DeliveryMapper;
import com.dih008.dihel.models.Customer;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Package;
import com.dih008.dihel.services.CarService;
import com.dih008.dihel.services.CustomerService;
import com.dih008.dihel.services.DriverService;
import com.dih008.dihel.services.PackageService;
import com.dih008.dihel.services.SlotService;

import javassist.compiler.ast.Pair;

@Repository
@ConditionalOnProperty(
		  value="accessType", 
		  havingValue = "jdbc", 
		  matchIfMissing = true)
public class DeliveryRepositoryJDBC implements RepositoryInterface<Delivery> {

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private CarService carService;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private PackageService packageService;
	
	@Autowired
	private SlotService slotService;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("Delivery").usingGeneratedKeyColumns("id")
				.usingColumns("address", "delivery_date", "driver_id", "slot_id", "car_id", "customer_id");
	}
	
	@PostConstruct
	public void init() {
		try (Statement stm = jdbcTemplate.getDataSource().getConnection().createStatement()) {
			stm.executeUpdate("CREATE TABLE Delivery (" + "id INT NOT NULL auto_increment," 
					+ " address varchar(255), "
					+ " delivery_date DATE, "
					+ " driver_id INTEGER, " 
					+ " slot_id INTEGER, " 
					+ " car_id INTEGER, " 
					+ " customer_id INTEGER, " 
					+ " PRIMARY KEY (id)" + ", constraint FKGTS2R2ETFNCL0Q1L1CVEE3X29 "
							+ "        foreign key (DRIVER_ID) references DRIVER (ID), "
							+ "    constraint FKGVY4LPTS4E5QO88I4VWECGTGE "
							+ "        foreign key (SLOT_ID) references SLOT (ID), "
							+ "    constraint FKR0MG2E4P18FRSJU6QUT84G8FS "
							+ "        foreign key (CUSTOMER_ID) references CUSTOMER (ID), "
							+ "    constraint FKSNPQ9WM3BUDTU599DLPO1HTMW "
							+ "        foreign key (CAR_ID) references CAR (ID);"
					);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void setCustomer(Delivery delivery) {
		//Map<String, String> where = new HashMap<String, String>();
		//where.put("id", Integer.toString(delivery.getCustomerId()));
		//List<Customer> entities = customerService.findBy(where);
		//Customer customer = entities.get(0);
		if (delivery.getCustomerId() != null) {
			delivery.setCustomer(customerService.getById(delivery.getCustomerId()));
		}
	}
	
	
	private void setDriver(Delivery delivery) {
		if (delivery.getDriverId() != null) {
			delivery.setDriver(driverService.getById(delivery.getDriverId()));
		}
	}
	
	private void setCar(Delivery delivery) {
		if (delivery.getCarId() != null) {
			delivery.setCar(carService.getById(delivery.getCarId()));
		}
	}
	
	private void setSlot(Delivery delivery) {
		if (delivery.getSlotId() != null) {
			delivery.setSlot(slotService.getById(delivery.getSlotId()));
		}
	}
	
	private void setPackages(Delivery delivery) {
		Map<String, String> where = new HashMap<String, String>();
		where.put("delivery_id", Integer.toString(delivery.getId()));
		List<Package> packages = packageService.findBy(where);
		delivery.setPackages(packages);
		for (Package entity : packages) {
			entity.setDelivery(delivery);
		}
	}
	
	public List<Delivery> findAll() {
		List<Delivery> deliveries = jdbcTemplate.query("select * from Delivery", new DeliveryMapper());
		for (Delivery delivery : deliveries) {
			setCar(delivery);
			setSlot(delivery);
			setPackages(delivery);
			setDriver(delivery);
			setCustomer(delivery);
		}
		return deliveries;
	}

	public Delivery save(Delivery entity) {
		int id = 0;
		
		if (entity.getId() == 0) {
			id = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(entity)).intValue();
		} else {
			jdbcTemplate.update(
					"UPDATE Delivery set "
					+ " address = ?, "
					+ " delivery_date = ?, "
					+ " customer_id = ?, "
					+ " driver_id = ?, "
					+ " slot_id = ? "
					+ " WHERE id = ?",
					entity.getAddress(), 
					entity.getDeliveryDate(), 
					entity.getCustomerId(), 
					entity.getDriverId(), 
					entity.getSlotId(), 
					entity.getId()
					);
		}
		entity.setId(id);
		return entity;
	}

	@Override
	public Delivery getById(long id) {
		Delivery delivery = jdbcTemplate.queryForObject("select * from Delivery WHERE id = ?", new DeliveryMapper(), id);
		
		setCar(delivery);
		setSlot(delivery);
		setPackages(delivery);
		setDriver(delivery);
		setCustomer(delivery);
		return delivery;
	}
	
	public boolean delete(long id) {
		int update = jdbcTemplate.update("DELETE FROM Delivery WHERE id = ?", id);
		return update > 0;
	}
	
	public List<Delivery> findBy(Map<String, String> where) {
		
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
		
		return jdbcTemplate.query("select * from Delivery  "
				+ " WHERE " + sqlWhere, new DeliveryMapper(), parameters);
	}
}
