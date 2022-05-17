package com.dih008.dihel.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class ToStringFromLocalDate implements Converter<LocalDate, String> {

	@Override
	public String convert(LocalDate source) {
		return source.format(DateTimeFormatter.ofPattern("d. M. uuuu"));
	}

}
