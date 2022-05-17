package com.dih008.dihel;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dih008.dihel.converters.LocalDateConverter;
import com.dih008.dihel.converters.LocalDateTimeConverter;


@Configuration
public class ConvertersConfig implements WebMvcConfigurer{

	@Override
	public void addFormatters(FormatterRegistry registry) {
		WebMvcConfigurer.super.addFormatters(registry);
		registry.addConverter(new LocalDateTimeConverter());
		registry.addConverter(new LocalDateConverter());
	}

	
}
