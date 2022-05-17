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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dih008.dihel.annotations.Type;
import com.dih008.dihel.form.ListEntities;
import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Slot;
import com.dih008.dihel.services.DeliveryService;
import com.dih008.dihel.services.SlotService;

@Controller
public class SlotController extends EntityController {
	
	@Autowired
	private SlotService service;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@PostConstruct
	public void init() {
		this.entityName = this.getEntityName(Slot.class);
	}
	
	@RequestMapping("/slot/create")
	public String create(Model model) {
		model.addAttribute("action", "create");
		Slot entity = new Slot();
		this.initFields(model, entity, false);	
		
		return "detail";
	}
	
	@RequestMapping("/slot/edit/{index}")
	public String edit(@PathVariable int index, Model model) {
		model.addAttribute("action", "edit");
		Slot entity = service.getById(index);
		this.initFields(model, entity, false);	
		
		return "detail";
	}
	
	@RequestMapping("/slot/detail/{index}")
	public String detail(@PathVariable int index, Model model) {
		model.addAttribute("action", "detail");
		Slot entity = service.getById(index);
		this.initFields(model, entity, false);
		
		List<ListEntities> listEntities = new ArrayList<ListEntities>(); 
		listEntities.add(getDelivieriesList(index, model));
		model.addAttribute("listEntities", listEntities);
		
		return "detail";
	}

	@RequestMapping("/slot/list")
	public String list(Model model) {
		model.addAttribute("action", "List");
		model.addAttribute("entities", service.findAll());
		Slot entity = new Slot();
		this.initColumns(model, entity, false);
		
		return "list";
	}	

	@RequestMapping("/slot/save")
	public String save(@ModelAttribute @Validated Slot formEntity, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("action", "edit");
		model.addAttribute("typeClass", Type.class);
		this.initFields(model, formEntity, false);
		model.addAttribute("bindingResult", result);
		
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("meessage", "Error while saving");
			model.addAttribute("bindingResult", result);
			return "detail";
		}
		service.save(formEntity);
		redirectAttributes.addFlashAttribute("meessage", "Successfully saved");
		if (formEntity.getId() > 0) {
			return "redirect:/slot/detail/" + formEntity.getId();
		} else {
			return "redirect:/slot/list";
		}
	}	
	
	@RequestMapping("/slot/delete/{index}")
	public String delete(@PathVariable int index, Model model, RedirectAttributes redirectAttributes) {
		if (service.delete(index)) {
			redirectAttributes.addFlashAttribute("flashMessage", new FlashMessage("Deleted", "success"));
		} else {
			redirectAttributes.addFlashAttribute("flashMessage", new FlashMessage("Error while deleting",  "danger"));
		}
		return "redirect:/slot/list";
	}	
	
	
	protected ListEntities getDelivieriesList(int index, Model model) {
		Map<String, String> where = new HashMap<String, String>();
		where.put("slot_id", Integer.toString(index));
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
