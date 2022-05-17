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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dih008.dihel.annotations.Type;
import com.dih008.dihel.form.SelectInput;
import com.dih008.dihel.models.Car;
import com.dih008.dihel.models.Customer;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Driver;
import com.dih008.dihel.models.Package;
import com.dih008.dihel.models.Slot;
import com.dih008.dihel.services.CarService;
import com.dih008.dihel.services.CustomerService;
import com.dih008.dihel.services.DeliveryService;
import com.dih008.dihel.services.DriverService;
import com.dih008.dihel.services.SlotService;

@Controller
public class DeliveryController extends EntityController
{

	@Autowired
	private DeliveryService service;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SlotService slotService;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private CarService carService;
	
	@PostConstruct
	public void init() {
		this.entityName = this.getEntityName(Delivery.class);
	}
	
	@RequestMapping("/delivery/create")
	public String create(Model model) {
		model.addAttribute("action", "create");
		Delivery entity = new Delivery();
		this.initFields(model, entity, false);
		this.initAddtitionalFields(model, entity);
		
		return "detail";
	}
	
	@RequestMapping("/delivery/edit/{index}")
	public String edit(@PathVariable int index, Model model) {
		model.addAttribute("action", "edit");
		Delivery entity = service.getById(index);
		this.initFields(model, entity, false);	
		this.initAddtitionalFields(model, entity);
		
		return "detail";
	}
	
	@RequestMapping("/delivery/detail/{index}")
	public String detail(@PathVariable int index, Model model) {
		model.addAttribute("action", "detail");
		Delivery entity = service.getById(index);
		this.initFields(model, entity, false);	
		this.initAddtitionalFields(model, entity);
		
		return "detail";
	}

	@RequestMapping("/delivery/list")
	public String list(Model model) {
		model.addAttribute("entities", service.findAll());
		Delivery entity = new Delivery();
		this.initColumns(model, entity, false);
		
		return "list";
	}	

	@RequestMapping("/delivery/save")
	public String save(
			@ModelAttribute @Validated Delivery formEntity, 
			BindingResult result, Model model, 
			RedirectAttributes redirectAttributes, 
			@RequestParam(value="customer_id", required = false) Integer customerId,
			@RequestParam(value="driver_id", required = false) Integer driverId,
			@RequestParam(value="car_id", required = false) Integer carId,
			@RequestParam(value="slot_id", required = false) Integer slotId
	) {
		model.addAttribute("action", "edit");
		model.addAttribute("typeClass", Type.class);
		this.initFields(model, formEntity, false);
		model.addAttribute("bindingResult", result);
		this.initAddtitionalFields(model, formEntity);
		
		boolean isValid = true;
		if (customerId != null) {
			Customer customer = customerService.getById(customerId);
			if (customer != null) {
				formEntity.setCustomerId(customerId);
				formEntity.setCustomer(customer);	
			} else {
				isValid = false;
			}
		}

		if (driverId != null) {
			Driver driver = driverService.getById(driverId);
			if (driver != null) {
				formEntity.setDriverId(driverId);
				formEntity.setDriver(driver);
			} else {
				isValid = false;
			}
		}
		
		
		if (carId != null) {
			Car car = carService.getById(carId);
			if (car != null) {
				formEntity.setCarId(carId);
				formEntity.setCar(car);
			} else {
				isValid = false;
			}
		}
	
		
		if (slotId != null) {
			Slot slot = slotService.getById(slotId);
			if (slot != null) {
				formEntity.setSlotId(slotId);
				formEntity.setSlot(slot);
			} else {
				isValid = false;
			}
		}
		
		if (result.hasErrors() || !isValid ) {
			redirectAttributes.addFlashAttribute("meessage", "Error while saving");
			model.addAttribute("bindingResult", result);
			return "detail";
		}
		
		service.save(formEntity);
		
		redirectAttributes.addFlashAttribute("meessage", "Successfully saved");
		
		if (formEntity.getId() > 0) {
			return "redirect:/delivery/detail/" + formEntity.getId();
		} else {
			return "redirect:/delivery/list";
		}
	}	
	
	@RequestMapping("/delivery/delete/{index}")
	public String delete(@PathVariable int index, Model model, RedirectAttributes redirectAttributes) {
		if (service.delete(index)) {
			redirectAttributes.addFlashAttribute("flashMessage", new FlashMessage("Deleted", "success"));
		} else {
			redirectAttributes.addFlashAttribute("flashMessage", new FlashMessage("Error while deleting",  "danger"));
		}
		return "redirect:/delivery/list";
	}	
	
	private SelectInput getCustomerSelectInput(Delivery entity) {

		List<Customer> customers = customerService.findAll();
		Map<Integer, String> options = new HashMap<Integer, String>();
		options.put(null, "---");
		List<Integer> selected = new ArrayList<Integer>();
		
		if (entity.getCustomer() != null) {
			selected.add(entity.getCustomer().getId());
		}
		for (Customer customer: customers) {
			String name = Integer.toString(customer.getId()) + " - " + customer.getFirstName() +  " " + customer.getLastName();
			options.put(customer.getId(), name);
		}
		
		SelectInput deliveryInput = new SelectInput("customer_id", options, false, selected);
		return deliveryInput;
		
	}
	
	private SelectInput getSlotSelectInput(Delivery entity) {

		List<Slot> slots = slotService.findAll();
		Map<Integer, String> options = new HashMap<Integer, String>();
		List<Integer> selected = new ArrayList<Integer>();
		options.put(null, "---");
		if (entity.getSlot() != null) {
			selected.add(entity.getSlot().getId());
		}
		for (Slot slot: slots) {
			String name = Integer.toString(slot.getId()) + " - " + slot.getSince();
			options.put(slot.getId(), name);
		}
		
		SelectInput slotInput = new SelectInput("slot_id", options, false, selected);
		return slotInput;
		
	}
	
	private SelectInput getDriverSelectInput(Delivery entity) {

		List<Driver> drivers = driverService.findAll();
		Map<Integer, String> options = new HashMap<Integer, String>();
		List<Integer> selected = new ArrayList<Integer>();
		options.put(null, "---");
		if (entity.getDriver() != null) {
			selected.add(entity.getDriver().getId());
		}
		for (Driver driver: drivers) {
			String name = Integer.toString(driver.getId()) + " - " + driver.getFirstName() + " " + driver.getLastName();
			options.put(driver.getId(), name);
		}
		
		SelectInput slotInput = new SelectInput("driver_id", options, false, selected);
		return slotInput;
		
	}
	
	private SelectInput getCarSelectInput(Delivery entity) {

		List<Car> cars = carService.findAll();
		Map<Integer, String> options = new HashMap<Integer, String>();
		List<Integer> selected = new ArrayList<Integer>();
		options.put(null, "---");
		if (entity.getCar() != null) {
			selected.add(entity.getCar().getId());
		}
		for (Car car: cars) {
			String name = Integer.toString(car.getId()) + " - " + car.getType() + " " + car.getLicensePlate();
			options.put(car.getId(), name);
		}
		
		SelectInput carInput = new SelectInput("car_id", options, false, selected);
		return carInput;
		
	}
	
	protected void initAddtitionalFields(Model model, Delivery entity) {
		List<SelectInput> selectInputs = new ArrayList<SelectInput>();
		
		selectInputs.add(this.getCustomerSelectInput(entity));
		selectInputs.add(this.getSlotSelectInput(entity));
		selectInputs.add(this.getDriverSelectInput(entity));
		selectInputs.add(this.getCarSelectInput(entity));
		
		model.addAttribute("selectInputs", selectInputs);		
	}	
	
}
