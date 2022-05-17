package com.dih008.dihel.repositories;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.dih008.dihel.mappers.DriverMapper;
import com.dih008.dihel.mappers.PackageMapper;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Driver;
import com.dih008.dihel.models.Package;
import com.dih008.dihel.services.DeliveryService;

@Repository
@ConditionalOnProperty(value = "accessType", havingValue = "jdbc", matchIfMissing = true)
public class PackageRepositoryJDBC implements RepositoryInterface<Package> {

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("Package").usingGeneratedKeyColumns("id")
				.usingColumns("weigth", "number", "delivery_id");
	}

	@PostConstruct
	public void init() {
		try (Statement stm = jdbcTemplate.getDataSource().getConnection().createStatement()) {
			stm.executeUpdate("CREATE TABLE Package (" + " id INT NOT NULL auto_increment," + " weigth float, "
					+ " number varchar(255), " + " DELIVERY_ID INTEGER, " + " PRIMARY KEY (id)" + "), "
					+ "constraint FK3FX0GOVGIAM7RMKE92PSWB2P7\r\n"
					+ "        foreign key (DELIVERY_ID) references DELIVERY (ID);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Package> findAll() {
		List<Package> packageEntityies = jdbcTemplate.query("select * from Package", new PackageMapper());
		for (Package packageEntity : packageEntityies) {
			setDelivery(packageEntity);
		}
		return packageEntityies;
	}

	public Package save(Package entity) {
		int id = 0;
		String[] b;
		if (entity.getId() == 0) {
			id = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(entity)).intValue();
		} else {
			jdbcTemplate.update(
					"UPDATE Package set " + " weigth = ?, " + " number = ?, " + " delivery_id = ? " + " WHERE id = ?",
					entity.getWeigth(), entity.getNumber(), entity.getDeliveryId(), entity.getId());
		}

		entity.setId(id);
		return entity;
	}

	private void setDelivery(Package packageEntity) {
		if (packageEntity.getDeliveryId() != null) {
			Delivery delivery = deliveryService.getById(packageEntity.getDeliveryId());
			packageEntity.setDelivery(delivery);
		}
	}

	@Override
	public Package getById(long id) {
		Package packageEntity = jdbcTemplate.queryForObject("select * from Package WHERE id = ?", new PackageMapper(),
				id);
		setDelivery(packageEntity);
		return packageEntity;
	}

	public boolean delete(long id) {
		int update = jdbcTemplate.update("DELETE FROM Package WHERE id = ?", id);
		return update > 0;
	}

	public List<Package> findBy(Map<String, String> where) {

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

		return jdbcTemplate.query("select * from Package  " + " WHERE " + sqlWhere, new PackageMapper(), parameters);
	}
}
