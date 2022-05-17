package com.dih008.dihel.controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class AbstractController {
	
	protected String userRole;
	protected String userName;
	
	public void initUser(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		this.userName = authentication.getName();
		
		Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) authentication.getAuthorities();
		GrantedAuthority grantedAuthority = roles.iterator().next();
		this.userRole = grantedAuthority.getAuthority();
		
		model.addAttribute("userName", this.userName);
		model.addAttribute("userRole", this.userRole);
	}
	
}
