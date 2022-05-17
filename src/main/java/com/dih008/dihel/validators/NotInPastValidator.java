package com.dih008.dihel.validators;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NotInPastValidator implements ConstraintValidator<NotInPast, LocalDate>{

	@Override
	public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
		if (date != null) {
			return LocalDate.now().isBefore(date);
		}
		return true;
	}
}
