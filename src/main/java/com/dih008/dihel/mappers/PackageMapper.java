package com.dih008.dihel.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dih008.dihel.models.Package;;

public class PackageMapper implements RowMapper<Package> {
	
	@Override
	public Package mapRow(ResultSet rs, int index) throws SQLException {
		return new Package(
				rs.getInt("id"),
				rs.getFloat("weigth"),
				rs.getString("number"),
				rs.getInt("delivery_id")
		);
	}

}
