package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.customer.CustomerDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
	private CustomAuthenticationProvider authenticationProvider;
	@Autowired
	private CustomerDetailsService service;
	
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources");
    }
    
    @Bean
    @Order(1)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
    		.authorizeHttpRequests()
    			.requestMatchers("/", "/portal", "/register/**", "/igse/**", "/processlogin").permitAll()
    			.requestMatchers("/dashboardadmin/**", "/processlogin", "/dashboardadmin/update/**"
    					, "/dashboardadmin/vouchers/**", "/dashboardadmin/readings/**", "/dashboardadmin/customers/**"
    					).hasAuthority("ADMIN")
    			.requestMatchers("/dashboard/**").hasAuthority("USER")
    			;
    	
    	http
    		.csrf().disable()
    		.formLogin()
    			.loginPage("/portal")
    			.usernameParameter("email")
    			.loginProcessingUrl("/portal")
    			.defaultSuccessUrl("/processlogin", true)
    			.permitAll()
    		.and()
    		.logout()
    			.logoutUrl("/logout")
    			.logoutSuccessUrl("/")
    	;
    return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
      return PasswordEncoderFactories.createDelegatingPasswordEncoder();
     }
    
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(service).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
