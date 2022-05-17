package com.dih008.dihel.controllers;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dih008.dihel.converters.ToStringFromLocalDate;
import com.dih008.dihel.form.SelectInput;
import com.dih008.dihel.models.Delivery;
import com.dih008.dihel.models.Driver;
import com.dih008.dihel.models.Package;
import com.dih008.dihel.services.DeliveryService;
import com.dih008.dihel.services.DriverService;

@Controller
public class IndexController extends AbstractController {

	@Autowired
	private DeliveryService service;
	
	@Autowired
	private DriverService driverService;
	
	
	@RequestMapping("/")
	public String index(
			Model model,
			@RequestParam(value="driver_id", required = false) Integer driverId
		) {	
		this.initUser(model);
		model.addAttribute("message", "Home");
		if (this.userRole.equals("ROLE_ADMIN")) {
			model.addAttribute("rows", this.getRows(driverId));
			model.addAttribute("selectInput", this.getDriverSelectInput(driverId));
		} else {
			Map<String, String> where = new HashMap<String, String>();
			where.put("email", this.userName);
			Driver driver = driverService.findBy(where).get(0);
			model.addAttribute("rows", this.getRows(driver.getId()));
		}
		
		return "index";
	}
	
	
	private List<List<String>> getRows(Integer driverId) {
		List<Delivery> deliveries;
		if (driverId != null) {
			Map<String, String> where = new HashMap<String, String>();
			where.put("driver_id", Integer.toString(driverId));
			deliveries = service.findBy(where);
		} else {
			deliveries = service.findAll();
		}
		
		List<List<String>> rows = new ArrayList<List<String>>();
		
		for (Delivery delivery : deliveries) {
			List<String> row = new ArrayList<String>();
			row.add(Integer.toString(delivery.getId()));
			String slotNumber = delivery.getSlot() != null ? Integer.toString(delivery.getSlot().getDaySlotNumber()) : "-";
			row.add(slotNumber);
			row.add(new ToStringFromLocalDate().convert(delivery.getDeliveryDate()));
			row.add(delivery.getAddress());
			
			List<Package> packages = delivery.getPackages();
			StringBuilder packageNumbers = new StringBuilder(200);
			boolean isFirstPackage = true;
			if (packages != null) {
				for (Package packageItem : packages) {
					if (!isFirstPackage) {
						packageNumbers.append(", ");
					}
					packageNumbers.append(packageItem.getNumber());
					isFirstPackage = false;
				}
			}
		
			row.add(packageNumbers.toString());
			
			if (delivery.getCustomer() != null) {
				row.add(delivery.getCustomer().getFirstName() + " " + delivery.getCustomer().getLastName());
				row.add(delivery.getCustomer().getPhoneNumber());
			}
			
			rows.add(row);
		}
		return rows;
	} 
	
	private SelectInput getDriverSelectInput(Integer driverId) {
		List<Driver> drivers = driverService.findAll();
		Map<Integer, String> options = new HashMap<Integer, String>();
		List<Integer> selected = new ArrayList<Integer>();
		options.put(null, "---");
		if (driverId != null) {
			selected.add(driverId);	
		}
		for (Driver driver: drivers) {
			String name = Integer.toString(driver.getId()) + " - " + driver.getFirstName() + " " + driver.getLastName();
			options.put(driver.getId(), name);
		}
		
		SelectInput slotInput = new SelectInput("driver_id", options, false, selected);
		return slotInput;
		
	}
	
	@RequestMapping("/403")
	public String accessDenied() {
		return "accessDenied";
	}
	
}
