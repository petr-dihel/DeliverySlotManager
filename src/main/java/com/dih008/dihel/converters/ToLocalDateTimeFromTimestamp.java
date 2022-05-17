package com.dih008.dihel.converters;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class ToLocalDateTimeFromTimestamp implements Converter<Timestamp, LocalDateTime> {

	@Override
	public LocalDateTime convert(Timestamp source) {
		return source.toLocalDateTime();
	}
}
