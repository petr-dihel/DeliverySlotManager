package com.dih008.dihel.controllers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dih008.dihel.annotations.Type;
import com.dih008.dihel.form.ListEntities;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Driver;
import com.dih008.dihel.services.DeliveryService;
import com.dih008.dihel.services.DriverService;

@Controller
public class DriverController extends EntityController {

	@Autowired
	private DriverService service;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@PostConstruct
	public void init() {
		this.entityName = this.getEntityName(Driver.class);
	}
	
	@RequestMapping("/driver/create")
	public String create(Model model) {
		model.addAttribute("action", "create");
		Driver entity = new Driver();
		this.initFields(model, entity, true);	
		model.addAttribute("entityName", this.entityName);
		
		return "detail";
	}
	
	@RequestMapping("/driver/edit/{index}")
	public String edit(@PathVariable int index, Model model) {
		model.addAttribute("action", "edit");
		Driver entity = service.getById(index);
		this.initFields(model, entity, true);	
		model.addAttribute("entityName", this.entityName);
		
		return "detail";
	}
	
	@RequestMapping("/driver/detail/{index}")
	public String detail(@PathVariable int index, Model model) {
		model.addAttribute("action", "detail");
		Driver entity = service.getById(index);
		this.initFields(model, entity, true);
		model.addAttribute("entityName", this.entityName);
		
		List<ListEntities> listEntities = new ArrayList<ListEntities>(); 
		listEntities.add(getDelivieriesList(index, model));
		model.addAttribute("listEntities", listEntities);
		
		return "detail";
	}

	@RequestMapping("/driver/list")
	public String list(Model model) {
		model.addAttribute("action", "List");
		model.addAttribute("entities", service.findAll());
		model.addAttribute("entityName", this.entityName);
		Driver entity = new Driver();
		this.initColumns(model, entity, true);
		
		return "list";
	}	

	@RequestMapping("/driver/save")
	public String save(@ModelAttribute @Validated Driver formEntity, BindingResult result, Model model) {
		model.addAttribute("action", "edit");
		model.addAttribute("typeClass", Type.class);		
		model.addAttribute("entityName", this.entityName);
		this.initFields(model, formEntity, true);
		model.addAttribute("bindingResult", result);
		
		if (result.hasErrors()) {
			return "detail";
		}
		service.save(formEntity);
		
		if (formEntity.getId() > 0) {
			return "redirect:/driver/detail/" + formEntity.getId();
		} else {
			return "redirect:/driver/list";
		}
	}	
	
	protected ListEntities getDelivieriesList(int index, Model model) {
		Map<String, String> where = new HashMap<String, String>();
		where.put("driver_id", Integer.toString(index));
		List<Delivery> list = deliveryService.findBy(where);
		List<Field> columns = this.getColums(new Delivery(), false);
		
		ListEntities listEntity = new ListEntities();
		
		List<Object> entities = new ArrayList<Object>();
		for (Delivery entity : list) {
			entities.add(entity);
		}
		listEntity.entities = entities;
		listEntity.fields = columns;
		listEntity.entityName = this.getEntityName(Delivery.class);
		return listEntity;
	}
}
