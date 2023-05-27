package com.example.demo.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.customer.Customer;
import com.example.demo.reading.Reading;
import com.example.demo.repos.CustomerRepo;
import com.example.demo.repos.ReadingRepo;
import com.example.demo.repos.TaiffRepo;
import com.example.demo.repos.VoucherRepo;
import com.example.demo.taiff.Taiff;
import com.example.demo.voucher.Voucher;

@RestController
@RequestMapping(path="/igse", produces="application/json")
@CrossOrigin(origins="*")
public class RestApi {

	@Autowired
	private ReadingRepo repo;
	@Autowired
	private CustomerRepo cRepo;
	@Autowired
	private VoucherRepo vRepo;
	@Autowired
	private TaiffRepo tRepo;
	
	private static Collection<Reading> readings = new ArrayList<>();
	private static Collection<Customer> customers = new ArrayList<>();
	private static Collection<Voucher> vouchers = new ArrayList<>();
	private static Collection<Taiff> taiffs = new ArrayList<>();
	
	@GetMapping("/readings")
	public Collection<Reading> readingsList() {
		readings = repo.findAll();
		return readings;
	}
	
	@GetMapping("/customers")
	public Collection<Customer> customersList() {
		customers = cRepo.findAll();
		return customers;
	}
	
	@GetMapping("/vouchers")
	public Collection<Voucher> vouchersList() {
		vouchers = vRepo.findAll();
		return vouchers;
	}
	
	@GetMapping("/taiffs")
	public Collection<Taiff> taiffsList() {
		taiffs = tRepo.findAll();
		return taiffs;
	}
	
	@GetMapping("test")
	public Taiff testTaiff() {
		return tRepo.findByTaiffType("gas");
	}
	
	@GetMapping("/propertycount")
	public Map<String, Integer> propertyCount() {
		
		customers = cRepo.findAll();
		
		List<String> listPTypes = Arrays.asList("Detached","Semi-Detached","Terraced","End-of-Terrace","Flats",
				"Converted Flats","Split-Level Flats","Studio Flats","Cottages","Bungalows");
		
		Map<String, Integer> propertyCount = new HashMap<String, Integer>();
		
		int val = 0;
		for (String a : listPTypes) {
	        for (Customer s : customers) {
	        	if (s.getProperty_type().equalsIgnoreCase(a)) {
	        		val = val+1;
	        	}
	        }
	        propertyCount.put(a, val);
	        val = 0;
		}
        
		
		return propertyCount;
	}
	
	@GetMapping("/{type}/{bedroom_num}")
	public Map<String, String> statistics(@PathVariable String type, @PathVariable int bedroom_num) {
		
		customers = cRepo.findAll();
		Taiff taiff = tRepo.findByTaiffType("electricity_day");
		
		double average1 = 0;
		double totalReading = 0;
		int count = 0;
		
		for (Customer s : customers) {
        	if (s.getProperty_type().equalsIgnoreCase(type)) {
        		if (s.getBedroom_num() == bedroom_num) {
        			readings = repo.findByCustomerID(s.getCustomer_id());
        			for (Reading r : readings) {
        				totalReading = r.getElec_readings_day()*tRepo.findByTaiffType("electricity_day").getRate() +
        						r.getElet_reading_night()*tRepo.findByTaiffType("electricity_night").getRate() +
        						r.getGas_reading()*tRepo.findByTaiffType("gas").getRate() +
        						r.getElec_readings_day()*tRepo.findByTaiffType("electricity_day").getRate();
        				average1 = average1 + totalReading;
            			count++;
        			}
        			
        		}
        	}
        }
		Reading first = repo.findFirstReading();
		Reading last = repo.findLastReading();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String inputString1 = last.getSubmission_date().toString();
	  
		String inputString2 = first.getSubmission_date().toString();
		  
		long daysBetween = 0;
		  
		LocalDate date1 = LocalDate.parse(inputString1, dtf);
		LocalDate date2 = LocalDate.parse(inputString2, dtf);
		daysBetween = ChronoUnit.DAYS.between(date2, date1);
		
		average1 = (average1/daysBetween);
		int average = (int)average1;
		
		Map<String, String> statistics = new HashMap<String, String>();
		statistics.put("type", type);
		statistics.put("bedroom", Integer.toString(bedroom_num));
		statistics.put("average_electricity_gas_cost_per_day", Double.toString(average));
		statistics.put("unit", "pound");
		
		
		return statistics;
		
	}
}
