package com.dih008.dihel.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.dih008.dihel.converters.ToLocalDateTimeFromTimestamp;
import com.dih008.dihel.models.Slot;

public class SlotMapper implements RowMapper<Slot> {

	@Override
	public Slot mapRow(ResultSet rs, int index) throws SQLException {
		return new Slot(
				rs.getInt("id"),
				new ToLocalDateTimeFromTimestamp().convert(rs.getTimestamp("since")),
				rs.getInt("day_slot_number")
		);
	}
}
