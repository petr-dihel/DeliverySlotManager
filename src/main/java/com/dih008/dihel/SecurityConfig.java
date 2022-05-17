package com.dih008.dihel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.dih008.dihel.models.Driver;
import com.dih008.dihel.services.CustomerService;
import com.dih008.dihel.services.DriverService;

@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DriverService driverService; 

	@Autowired
	CustomerService customerService;
	
	@Autowired
	public void initGlobal(AuthenticationManagerBuilder builder) throws Exception {

		//builder.inMemoryAuthentication().withUser("david").password("{noop}david").roles("USER", "EDITOR");
		
		builder.authenticationProvider(new AuthenticationProvider() {

			@Override
			public boolean supports(Class<?> authentication) {
				return authentication.equals(UsernamePasswordAuthenticationToken.class);
			}

			
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
				String name = authentication.getName();
				String password = authentication.getCredentials().toString();
				
				Map<String, String> where = new HashMap<String, String>();
				where.put("email", name);
				where.put("password", password);
				
				List<Driver> drivers = driverService.findBy(where);
				//List<Driver> drivers = driverService.findAll();
				if (drivers.size() > 0) {
					List<GrantedAuthority> grantedAuths = new ArrayList<>();
					grantedAuths.add(new SimpleGrantedAuthority("ROLE_DRIVER"));
					Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
					return auth;
				} else if ( name.equals("admin") && password.equals("123456")) {
					List<GrantedAuthority> grantedAuths = new ArrayList<>();
					grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
					Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
					return auth;
				}
				
				return null;
			}
		});
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
			.antMatchers("/bootstrap-5.1.3-dist/css/bootstrap.min.css").permitAll()
			.antMatchers("/bootstrap-5.1.3-dist/js/bootstrap.bundle.min.js").permitAll()
			.antMatchers("/api/v1/**").permitAll()
			.antMatchers("/").hasAnyRole("DRIVER", "ADMIN")
			.antMatchers("/customer/**").hasAnyRole("DRIVER", "ADMIN")
			.antMatchers("/package/**").hasAnyRole("DRIVER", "ADMIN")
			.antMatchers("/delivery/**").hasAnyRole("DRIVER", "ADMIN")
			.antMatchers("/**").hasAnyRole("ADMIN")
			.and().formLogin().permitAll()
			.and().logout().permitAll().invalidateHttpSession(true)
			.logoutSuccessUrl("/login").permitAll().and()
			.exceptionHandling().accessDeniedPage("/403");
	}
}
