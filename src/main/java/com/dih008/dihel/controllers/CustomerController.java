package com.dih008.dihel.controllers;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dih008.dihel.annotations.Type;
import com.dih008.dihel.form.ListEntities;
import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Customer;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.services.CustomerService;
import com.dih008.dihel.services.DeliveryService;
import java.lang.reflect.Field;

@Controller
public class CustomerController extends EntityController 
{

	@Autowired
	private CustomerService service;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@PostConstruct
	public void init() {
		this.entityName = this.getEntityName(Customer.class);
	}
	
	@RequestMapping("/customer/create")
	public String create(Model model) {
		model.addAttribute("action", "create");
		Customer entity = new Customer();
		this.initFields(model, entity, true);	
		
		return "detail";
	}
	
	@RequestMapping("/customer/edit/{index}")
	public String edit(@PathVariable int index, Model model) {
		model.addAttribute("action", "edit");
		Customer entity = service.getById(index);
		this.initFields(model, entity, true);
		
		return "detail";
	}
	
	@RequestMapping("/customer/detail/{index}")
	public String detail(@PathVariable int index, Model model) {
		model.addAttribute("action", "detail");
		Customer entity = service.getById(index);
		this.initFields(model, entity, true);
		model.addAttribute("entityName", this.entityName);
		
		List<ListEntities> listEntities = new ArrayList<ListEntities>(); 
		
		listEntities.add(getDelivieriesList(index, model));
		model.addAttribute("listEntities", listEntities);
		return "detail";
	}
	
	protected ListEntities getDelivieriesList(int index, Model model) {
		Map<String, String> where = new HashMap<String, String>();
		where.put("customer_id", Integer.toString(index));
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

	@RequestMapping("/customer/list")
	public String list(Model model) {
		model.addAttribute("action", "List");
		model.addAttribute("entities", service.findAll());
		Customer entity = new Customer();
		this.initColumns(model, entity, true);
		
		return "list";
	}	

	@RequestMapping("/customer/save")
	public String save(@ModelAttribute @Validated Customer entity, BindingResult result, Model model) {
		model.addAttribute("action", "List");
		model.addAttribute("typeClass", Type.class);
		model.addAttribute("entityName", this.entityName);
		this.initFields(model, entity, true);
		model.addAttribute("bindingResult", result);
		
		if (result.hasErrors()) {
			model.addAttribute("bindingResult", result);
			return "detail";
		}
		service.save(entity);
		
		if (entity.getId() > 0) {
			return "redirect:/customer/detail/" + entity.getId();
		} else {
			return "redirect:/customer/list";
		}
	}	
	
	@RequestMapping("/customer/delete/{index}")
	public String delete(@PathVariable int index, Model model, RedirectAttributes redirectAttributes) {
		if (service.delete(index)) {
			redirectAttributes.addFlashAttribute("flashMessage", new FlashMessage("Deleted", "success"));
		} else {
			redirectAttributes.addFlashAttribute("flashMessage", new FlashMessage("Error while deleting",  "danger"));
		}
		return "redirect:/customer/list";
	}	
	
	
}
