package com.github.fernandobossini.productms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.github.fernandobossini.productms.utility.Constants;

@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(Constants.USER).password("{noop}" + Constants.PASSWORD).roles(Constants.ROLES);
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()			
			.antMatchers("/**").hasRole(Constants.ROLES)
				.and()
			.httpBasic()
				.and()		
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.csrf()
				.disable()
			.headers()
				.frameOptions().disable();
	}
	
}
