package com.example.demo.controllers;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.customer.Customer;
import com.example.demo.reading.Reading;
import com.example.demo.repos.CustomerRepo;
import com.example.demo.repos.ReadingRepo;
import com.example.demo.repos.TaiffRepo;
import com.example.demo.repos.VoucherRepo;
import com.example.demo.taiff.Taiff;
import com.example.demo.voucher.Voucher;

@Controller
public class DashboardCustomer {
	
//	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	@Autowired
	private ReadingRepo repo;
	@Autowired
	private CustomerRepo cRepo;
	@Autowired
	private VoucherRepo vRepo;
	@Autowired
	private TaiffRepo tRepo;
	
	@PostMapping("/dashboard")
	public String customerDashboard(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<Reading> readings = repo.findByCustomerID(auth.getName());
		
		Customer customer = cRepo.findByCustomerID(auth.getName());
		Reading reading = new Reading();
		
		model.addAttribute("readings", readings);
		model.addAttribute("customer", customer);
		model.addAttribute("voucher", new Voucher());
		
		model.addAttribute("reading", reading);
		return "customer/dashboard";
		
//		if (auth.getAuthorities().toString() == "ADMIN") {
//			return "admin/dashboard";
//		} else if (auth.getAuthorities().toString() == "USER") {
//			Collection<Reading> readings = repo.findByCustomerID(auth.getName());
//			model.addAttribute("reading", readings);
//			return "customer/dashboard";
//		} else {
//			return "error";
//		}
		
	}
	
	@GetMapping("/dashboard")
	public String getCustomerDashboard(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<Reading> readings = repo.findByCustomerID(auth.getName());
		
		Customer customer = cRepo.findByCustomerID(auth.getName());
		Reading reading = new Reading();
		
		model.addAttribute("readings", readings);
		model.addAttribute("customer", customer);
		model.addAttribute("voucher", new Voucher());
		
		model.addAttribute("reading", reading);
		
		return "customer/dashboard";
		
//		if (auth.getAuthorities().toString() == "ADMIN") {
//			return "admin/dashboard";
//		} else if (auth.getAuthorities().toString() == "USER") {
//			Collection<Reading> readings = repo.findByCustomerID(auth.getName());
//			model.addAttribute("reading", readings);
//			return "customer/dashboard";
//		} else {
//			return "error";
//		}
		
	}
	
	  @PostMapping("/dashboard/processform")
	  public String addReading(@ModelAttribute("reading") Reading reading, Model model, RedirectAttributes redirectAttrs) throws ParseException {
	    
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    reading.setCustomer_id(cRepo.findByCustomerID(auth.getName().toString()));
	    reading.setStatus("pending");
	    
	    repo.save(reading);
	    
	    Reading preReading = repo.findPreviousReading(reading.getReading_id());
	    
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String inputString1 = reading.getSubmission_date().toString();
		  
		String inputString2 = preReading.getSubmission_date().toString();
		  
		long daysBetween = 0;
		  
		LocalDate date1 = LocalDate.parse(inputString1, dtf);
		LocalDate date2 = LocalDate.parse(inputString2, dtf);
		daysBetween = ChronoUnit.DAYS.between(date2, date1);
	    
	    if (reading.getElec_readings_day() < preReading.getElec_readings_day() || reading.getElet_reading_night()
	    		< preReading.getElet_reading_night() || reading.getGas_reading() < preReading.getGas_reading() ||
	    		daysBetween < 0) {
	    	repo.delete(reading);
	    	redirectAttrs.addFlashAttribute("readingSubmissionError", 
	    			"Readings entered have to be larger than previous reading!");
	    	return "redirect:/dashboard";
	    }
	    
	    redirectAttrs.addFlashAttribute("readingSubmissionError", 
    			null);
	    redirectAttrs.addFlashAttribute("readingSubmissionSuccess", 
    			"Successfully added new reading.");
	    
	    return "redirect:/dashboard";
	  }
	  
	  @PostMapping("/dashboard/addcredits")
	  public String addCredits(Voucher voucher, RedirectAttributes redirectAttrs) throws ParseException {
		  
		  if (vRepo.findByEVCCode(voucher.getEVC_code()) != null) {
			  if (voucher.getUsed() == 1) {
				  voucher.setUsed(1);
				  vRepo.save(voucher);
				  
				  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				  Customer customer = cRepo.findByCustomerID(auth.getName().toString());
				  double newBalance = customer.getBalance() + 200;
				  customer.setBalance(newBalance);
				  cRepo.save(customer);
			  } else {
				  redirectAttrs.addFlashAttribute("creditErrorMessage", "Voucher previously used!");
				  redirectAttrs.addFlashAttribute("creditSuccessMessage", null);
				  return "redirect:/dashboard";
			  } 
		  } else {
			  redirectAttrs.addFlashAttribute("creditErrorMessage", "Voucher code doesn't exist!");
			  redirectAttrs.addFlashAttribute("creditSuccessMessage", null);
			  return "redirect:/dashboard";
		  }
		  redirectAttrs.addFlashAttribute("creditSuccessMessage", "£200 added!");
		  redirectAttrs.addFlashAttribute("creditErrorMessage", null);
	    return "redirect:/dashboard";
	  }
	  
	  @PostMapping("dashboard/pay")
	  public String payReading(@RequestParam("readingId") Long reading_id, Model model) throws ParseException {
		  // Retrieve the item with the given ID from the database
		  Reading reading = repo.findByReadingID(reading_id);
		  model.addAttribute("reading", reading);
		  
		  Taiff EDTaiff = tRepo.findByTaiffType("electricity_day");
		  model.addAttribute("EDTaiff", EDTaiff.getRate());
		  Taiff ENTaiff = tRepo.findByTaiffType("electricity_night");
		  model.addAttribute("ENTaiff", ENTaiff.getRate());
		  Taiff GTaiff = tRepo.findByTaiffType("gas");
		  model.addAttribute("GTaiff", GTaiff.getRate());
		  Taiff STaiff = tRepo.findByTaiffType("sanding_charge");
		  model.addAttribute("STaiff", STaiff.getRate());
		  
		  Reading preReading = repo.findPreviousReading(reading_id);
		  model.addAttribute("preReading", preReading);
		  
		  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		  String inputString1 = reading.getSubmission_date().toString();
		  
		  String inputString2 = preReading.getSubmission_date().toString();
		  
		  long daysBetween = 0;
		  
		  LocalDate date1 = LocalDate.parse(inputString1, dtf);
		  LocalDate date2 = LocalDate.parse(inputString2, dtf);
		  daysBetween = ChronoUnit.DAYS.between(date2, date1);

		  model.addAttribute("days", daysBetween);
		  
		  Double edc = (reading.getElec_readings_day() - preReading.getElec_readings_day()) * EDTaiff.getRate();
		  model.addAttribute("edc", edc);
		  Double enc = (reading.getElet_reading_night() - preReading.getElet_reading_night()) * ENTaiff.getRate();
		  model.addAttribute("enc", enc);
		  Double gc = (reading.getGas_reading() - preReading.getGas_reading()) * GTaiff.getRate();
		  model.addAttribute("gc", gc);
		  Double rc = daysBetween * STaiff.getRate();
		  model.addAttribute("rc", rc);
		  
		  Double total = edc + enc + gc + rc;
		  
		  model.addAttribute("total", total);
		  
		  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		  Customer customer = cRepo.findByCustomerID(auth.getName());
		  model.addAttribute("customer", customer);
		  
		  
		  return "customer/pay";
		}
	  
	  @PostMapping("dashboard/delete")
	  public String deleteReading(@RequestParam("readingId") Long reading_id, RedirectAttributes redirectAttrs) {
		  
		  redirectAttrs.addFlashAttribute("deletedReadingMessage", "The reading is deleted!");
		  
		  repo.deleteById(reading_id);
		  
		return "redirect:/dashboard";
	  }
	  
	  @PostMapping("dashboard/payconfirm")
	  public String confirmPayment(@RequestParam("readingId") Long reading_id, Model model, RedirectAttributes redirectAttrs) {
		  
		  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		  Customer customer = cRepo.findByCustomerID(auth.getName());
		  Reading reading = repo.findByReadingID(reading_id);
		  Taiff EDTaiff = tRepo.findByTaiffType("electricity_day");
		  Taiff ENTaiff = tRepo.findByTaiffType("electricity_night");
		  Taiff GTaiff = tRepo.findByTaiffType("gas");
		  Taiff STaiff = tRepo.findByTaiffType("sanding_charge");
		  Reading preReading = repo.findPreviousReading(reading_id);
		  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		  String inputString1 = reading.getSubmission_date().toString();
		  String inputString2 = preReading.getSubmission_date().toString();
		  long daysBetween = 0;
		  
		  LocalDate date1 = LocalDate.parse(inputString1, dtf);
		  LocalDate date2 = LocalDate.parse(inputString2, dtf);
		  daysBetween = ChronoUnit.DAYS.between(date2, date1);
		  Double edc = (reading.getElec_readings_day() - preReading.getElec_readings_day()) * EDTaiff.getRate();
		  Double enc = (reading.getElet_reading_night() - preReading.getElet_reading_night()) * ENTaiff.getRate();
		  Double gc = (reading.getGas_reading() - preReading.getGas_reading()) * GTaiff.getRate();
		  Double rc = daysBetween * STaiff.getRate();
		  
		  Double total = edc + enc + gc + rc;
		  
		  if (customer.getBalance() >= total) {
			  reading.setStatus("paid");
			  repo.save(reading);
			  customer.setBalance(customer.getBalance() - total);
			  cRepo.save(customer);
			  redirectAttrs.addFlashAttribute("paymentComplete", "Payment completed!");
			  redirectAttrs.addFlashAttribute("paymentError", null);
			  return "redirect:/dashboard";
		  } else {
			  redirectAttrs.addFlashAttribute("paymentComplete", null);
			  redirectAttrs.addFlashAttribute("paymentError", "Insufficient Balance!");
			  return "redirect:/dashboard/pay";
		  }	
	  }

}
