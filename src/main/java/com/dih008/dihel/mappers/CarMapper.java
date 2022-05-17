package com.dih008.dihel.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dih008.dihel.models.Car;

public class CarMapper implements RowMapper<Car> {

	@Override
	public Car mapRow(ResultSet rs, int index) throws SQLException {
		return new Car(
				rs.getInt("id"),
				rs.getString("type"),
				rs.getString("license_plate")
			);
	}

}
