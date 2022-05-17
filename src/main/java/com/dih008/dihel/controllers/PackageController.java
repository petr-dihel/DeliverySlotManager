package com.dih008.dihel.controllers;

import java.lang.reflect.Field;
import java.net.http.HttpRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dih008.dihel.annotations.Type;
import com.dih008.dihel.form.SelectInput;
import com.dih008.dihel.services.DeliveryService;
import com.dih008.dihel.services.PackageService;
import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Package;
import com.dih008.dihel.models.Slot;

@Controller
public class PackageController extends EntityController {
	

	@Autowired
	private PackageService service;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@PostConstruct
	public void init() {
		this.entityName = this.getEntityName(Package.class);
	}
	
	@RequestMapping("/package/create")
	public String create(Model model) {
		model.addAttribute("action", "create");
		Package entity = new Package();
		this.initFields(model, entity, false);	
		this.initAddtitionalFields(model, entity);	
		
		return "detail";
	}
	
	@RequestMapping("/package/edit/{index}")
	public String edit(@PathVariable int index, Model model) {
		model.addAttribute("action", "edit");
		Package entity = service.getById(index);
		this.initFields(model, entity, false);	
		this.initAddtitionalFields(model, entity);
		
		return "detail";
	}
	
	@RequestMapping("/package/detail/{index}")
	public String detail(@PathVariable int index, Model model) {
		model.addAttribute("action", "detail");
		Package entity = service.getById(index);
		this.initFields(model, entity, false);
		this.initAddtitionalFields(model, entity);
		
		return "detail";
	}

	@RequestMapping("/package/list")
	public String list(Model model) {
		model.addAttribute("entities", service.findAll());
		Package entity = new Package();
		this.initColumns(model, entity, false);
		
		return "list";
	}	

	@RequestMapping("/package/save")
	public String save(
			@ModelAttribute @Validated Package formEntity, 
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes, 
			@RequestParam(value="delivery_id", required = false) 
			Integer deliveryId
	) {
		model.addAttribute("action", "edit");
		model.addAttribute("typeClass", Type.class);
		this.initFields(model, formEntity, false);
		model.addAttribute("bindingResult", result);
		this.initAddtitionalFields(model, formEntity);
		
		boolean isValid = false;
		if (deliveryId != null) {
			Delivery delivery = deliveryService.getById(deliveryId);
			if (delivery != null) {
				formEntity.setDelivery(delivery);
				formEntity.setDeliveryId(deliveryId);
			} else {
				isValid = false;
			}
		}
		
		if (result.hasErrors() || isValid) {
			model.addAttribute("flashMessage", new FlashMessage("Error",  "danger"));
			model.addAttribute("bindingResult", result);
			return "detail";
		}
		
		formEntity = service.save(formEntity);
		redirectAttributes.addFlashAttribute("meessage", "Successfully saved");
		
		if (formEntity.getId() > 0) {
			return "redirect:/package/detail/" + formEntity.getId();
		} else {
			return "redirect:/package/list";
		}
	}	
	
	protected void initAddtitionalFields(Model model, Package entity) {
		List<SelectInput> selectInputs = new ArrayList<SelectInput>();
		
		List<Delivery> deliveries = deliveryService.findAll();
		Map<Integer, String> options = new HashMap<Integer, String>();
		options.put(null, "---");
		List<Integer> selected = new ArrayList<Integer>();
		
		if (entity.getDelivery() != null) {
			selected.add(entity.getDelivery().getId());
		}
		for (Delivery delivery : deliveries) {
			String name = Integer.toString(delivery.getId()) + " - " + delivery.getDeliveryDate();
			options.put(delivery.getId(), name);
		}
		
		SelectInput delivery = new SelectInput("delivery_id", options, false, selected);
		
		
		selectInputs.add(delivery);
		model.addAttribute("selectInputs", selectInputs);		
	}
	
	@RequestMapping("/package/delete/{index}")
	public String delete(@PathVariable int index, Model model, RedirectAttributes redirectAttributes) {
		if (service.delete(index)) {
			redirectAttributes.addFlashAttribute("flashMessage", new FlashMessage("Deleted", "success"));
		} else {
			redirectAttributes.addFlashAttribute("flashMessage", new FlashMessage("Error while deleting",  "danger"));
		}
		return "redirect:/package/list";
	}	
	
}
