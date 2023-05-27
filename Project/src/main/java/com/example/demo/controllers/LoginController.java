package com.example.demo.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.customer.Customer;
import com.example.demo.repos.CustomerRepo;

@Controller
public class LoginController {
	
	@Autowired
	private CustomerRepo repo;
	
	@GetMapping("/processlogin")
	public String loginProcess() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = repo.findByCustomerID(auth.getName());
		if (customer != null) {
			if (customer.getType().equals("ADMIN")) {
				return "redirect:/dashboardadmin";
			} 
			if (customer.getType().equals("USER")) {
				return "redirect:/dashboard";
			} else {
				return "errorlogin";
			}
		} else {
			return "errorlogin";
		}
	}
	
	@GetMapping("/portal")
	public String customerPortal() {
		return "login";
	}
	
	@PostMapping("/portal")
	public String customerPortalPost() {
		return "login";
	}
	
	//Registration
		
	@GetMapping("register")
	public String customerSignup(Model model) {
			
		Customer signCustomer = new Customer();
			
		model.addAttribute("customer", signCustomer);
			
		List<String> listPTypes = Arrays.asList("Detached","Semi-Detached","Terraced","End-of-Terrace","Flats",
				"Converted Flats","Split-Level Flats","Studio Flats","Cottages","Bungalows");
		model.addAttribute("listPTypes", listPTypes);
			
		List<Integer> listNumB = Arrays.asList(1,2,3,4,5,6,7,8,9);
		model.addAttribute("listNumB", listNumB);
			
		return "entry/signup_form";
	}
		
	@PostMapping("/register/process_register")
	public String processRegistration(Customer customer) throws NoSuchAlgorithmException {
		customer.setType("USER");
			
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(customer.getPassword_hash().getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		customer.setPassword_hash(sb.toString());
		repo.save(customer);
			
		return "entry/register_success";
	}
	
}
