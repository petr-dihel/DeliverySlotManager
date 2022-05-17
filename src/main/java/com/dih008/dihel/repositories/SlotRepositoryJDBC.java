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

import com.dih008.dihel.mappers.PackageMapper;
import com.dih008.dihel.mappers.SlotMapper;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Driver;
import com.dih008.dihel.models.Package;
import com.dih008.dihel.models.Slot;
import com.dih008.dihel.services.DeliveryService;

@Repository
@ConditionalOnProperty(
		  value="accessType", 
		  havingValue = "jdbc", 
		  matchIfMissing = true)
public class SlotRepositoryJDBC implements RepositoryInterface<Slot> {

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("Slot").usingGeneratedKeyColumns("id")
				.usingColumns("since", "day_slot_number");
	}
	
	@PostConstruct
	public void init() {
		try (Statement stm = jdbcTemplate.getDataSource().getConnection().createStatement()) {
			stm.executeUpdate("CREATE TABLE Slot (" + "id INT NOT NULL auto_increment," 
					+ " since timestamp, "
					+ " day_slot_number varchar(255), " 
					+ " PRIMARY KEY (id)" + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Slot> findAll() {
		return jdbcTemplate.query("select * from Slot", new SlotMapper());
	}
	

	public Slot save(Slot entity) {
		int id = 0;
		if (entity.getId() == 0) {
			id = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(entity)).intValue();
		} else {
			jdbcTemplate.update(
					"UPDATE Slot set "
					+ " since = ?, "
					+ " day_slot_number = ? "
					+ " WHERE id = ?",
					entity.getSince(), 
					entity.getDaySlotNumber(), 
					entity.getId()
					);
		}
		entity.setId(id);
		return entity;
	}

	@Override
	public Slot getById(long id) {
		return jdbcTemplate.queryForObject("select * from Slot WHERE id = ?", new SlotMapper(), id);
	}
	
	public boolean delete(long id) {
		int update = jdbcTemplate.update("DELETE FROM Slot WHERE id = ?", id);
		return update > 0;
	}
	
	public List<Slot> findBy(Map<String, String> where) {
		
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
		
		return jdbcTemplate.query("select * from Slot  "
				+ " WHERE " + sqlWhere, new SlotMapper(), parameters);
	}
	
}
