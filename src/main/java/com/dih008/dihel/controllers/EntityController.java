package com.dih008.dihel.controllers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import com.dih008.dihel.annotations.Listable;
import com.dih008.dihel.annotations.Type;

public abstract class EntityController extends AbstractController {

	protected String entityName;
	
	protected void initFields(Model model, Object entity, boolean hasSuperClass) {
		List<Field> fieldsToTemplate = new ArrayList<Field>();
		Field[] fields = entity.getClass().getDeclaredFields();

		if (hasSuperClass) {
			Field[] superClassFields = entity.getClass().getSuperclass().getDeclaredFields();
			for (Field field : superClassFields) {
				if (field.isAnnotationPresent(Type.class)) {
					fieldsToTemplate.add(field);
				}
			}
		}
		
		for (Field field : fields) {
			if (field.isAnnotationPresent(Type.class)) {
				fieldsToTemplate.add(field);
			}
		}
		
		model.addAttribute("formEntity", entity);
		model.addAttribute("typeClass", Type.class);
		model.addAttribute("entityfields", fieldsToTemplate);
		model.addAttribute("entityName", this.entityName);

		this.initUser(model);
	}
	
	protected String getEntityName(Class entityClass) {
		return entityClass.getSimpleName().toLowerCase();
	}
	
	protected void initColumns(Model model, Object entity, boolean hasSuperClass) {
		model.addAttribute("formEntity", entity);
		model.addAttribute("typeClass", Type.class);
		model.addAttribute("entityfields", this.getColums(entity, hasSuperClass));
		model.addAttribute("entityName", this.entityName);

		this.initUser(model);
	}
	
	protected List<Field> getColums(Object entity, boolean hasSuperClass) {
		List<Field> columnsToTemplate = new ArrayList<Field>();
		Field[] fields = entity.getClass().getDeclaredFields();

		if (hasSuperClass) {
			Field[] superClassFields = entity.getClass().getSuperclass().getDeclaredFields();
			for (Field field : superClassFields) {
				if (field.isAnnotationPresent(Listable.class)) {
					columnsToTemplate.add(field);
				}
			}
		}
		
		for (Field field : fields) {
			if (field.isAnnotationPresent(Listable.class)) {
				columnsToTemplate.add(field);
			}
		}
		return columnsToTemplate;
	}
	
	
}
