package com.dih008.dihel.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.dih008.dihel.models.Delivery;

public class DeliveryMapper implements RowMapper<Delivery> {
	
	@Override
	public Delivery mapRow(ResultSet rs, int index) throws SQLException {
		return new Delivery(
				rs.getInt("id"),
				rs.getString("address"),
				rs.getDate("delivery_date").toLocalDate(),
				rs.getInt("driver_id"),
				rs.getInt("slot_id"),
				rs.getInt("car_id"),
				rs.getInt("customer_id")		
		);
	}
	
}
