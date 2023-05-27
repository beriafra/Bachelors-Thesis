package com.example.demo.controllers;

import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
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
public class DashboardAdmin {

	@Autowired
	private ReadingRepo repo;
	@Autowired
	private CustomerRepo cRepo;
	@Autowired
	private VoucherRepo vRepo;
	@Autowired
	private TaiffRepo tRepo;
	
	@PostMapping("/dashboardadmin")
	public String dashboardAdminPost(Model model) {
		
		Taiff EDTaiff = tRepo.findByTaiffType("electricity_day");
		model.addAttribute("EDTaiff", EDTaiff.getRate());
		model.addAttribute("EDTaiffB", EDTaiff.getTaiff_type());
		Taiff ENTaiff = tRepo.findByTaiffType("electricity_night");
		model.addAttribute("ENTaiff", ENTaiff.getRate());
		model.addAttribute("ENTaiffB", ENTaiff.getTaiff_type());
		Taiff GTaiff = tRepo.findByTaiffType("gas");
		model.addAttribute("GTaiff", GTaiff.getRate());
		model.addAttribute("GTaiffB", GTaiff.getTaiff_type());
		Taiff STaiff = tRepo.findByTaiffType("sanding_charge");
		model.addAttribute("STaiff", STaiff.getRate());
		model.addAttribute("STaiffB", STaiff.getTaiff_type());
		
		model.addAttribute("taiff", new Taiff());
		
		return "admin/dashboard";
	}
	
	@GetMapping("/dashboardadmin")
	public String dashboardAdminGet(Model model) {
		
		Taiff EDTaiff = tRepo.findByTaiffType("electricity_day");
		model.addAttribute("EDTaiff", EDTaiff.getRate());
		model.addAttribute("EDTaiffB", EDTaiff.getTaiff_type());
		Taiff ENTaiff = tRepo.findByTaiffType("electricity_night");
		model.addAttribute("ENTaiff", ENTaiff.getRate());
		model.addAttribute("ENTaiffB", ENTaiff.getTaiff_type());
		Taiff GTaiff = tRepo.findByTaiffType("gas");
		model.addAttribute("GTaiff", GTaiff.getRate());
		model.addAttribute("GTaiffB", GTaiff.getTaiff_type());
		Taiff STaiff = tRepo.findByTaiffType("sanding_charge");
		model.addAttribute("STaiff", STaiff.getRate());
		model.addAttribute("STaiffB", STaiff.getTaiff_type());
		
		model.addAttribute("taiff", new Taiff());
		
		return "admin/dashboard";
	}
	
	@PostMapping("/dashboardadmin/update")
	public String updateTariff(@RequestParam("EDT") String taiffName, Model model, RedirectAttributes redirectAttrs) {
		
		Taiff taiff = tRepo.findByTaiffType(taiffName);
		model.addAttribute("taiff", new Taiff());
		
		if (taiff.getTaiff_type().equals("electricity_day")) {
			model.addAttribute("taiffname", "electricity_day");
			model.addAttribute("taiffnames", "Electricity Day");
		} else if (taiff.getTaiff_type().equals("electricity_night")){
			model.addAttribute("taiffname", "electricity_night");
			model.addAttribute("taiffnames", "Electricity Night");
		} else if (taiff.getTaiff_type().equals("gas")){
			model.addAttribute("taiffname", "Gas");
			model.addAttribute("taiffnames", "Gas");
		} else if (taiff.getTaiff_type().equals("sanding_charge")){
			model.addAttribute("taiffname", "sanding_charge");
			model.addAttribute("taiffnames", "Sanding Charge");
		} else {
			redirectAttrs.addFlashAttribute("errorFindingTaiff", "Error finding the selected Taiff!");
		}
		
		
		
		return "admin/updatetariff";
	}
	
	@PostMapping("/dashboardadmin/updatetaiff")
	public String updateProcess(@ModelAttribute("taiff") Taiff taiff, @RequestParam("taiffname") String 
			taiffName, Model model, RedirectAttributes redirectAttrs) {
		
		taiff.setTaiff_type(taiffName);
		tRepo.save(taiff);
		redirectAttrs.addFlashAttribute("rateChanged", "Rate changed successfully!");
		
		return "redirect:/dashboardadmin";
	}
	
	@GetMapping("/dashboardadmin/customers")
	public String allCustomer(Model model) {
		
		Collection<Customer> customers = cRepo.findAll();
		model.addAttribute("customers", customers);
		
		return "admin/customers";
	}
	
	@GetMapping("/dashboardadmin/readings")
	public String allReadings(Model model) {
		
		Collection<Reading> readings = repo.findAll();
		model.addAttribute("readings", readings);
		
		return "admin/readings";
	}
	
	@GetMapping("/dashboardadmin/vouchers")
	public String allVouchers(Model model) {
		
		Collection<Voucher> vouchers = vRepo.findAll();
		model.addAttribute("vouchers", vouchers);
		
		return "admin/vouchers";
	}
	
	@GetMapping("/dashboardadmin/vouchers/newvoucher")
	public String newVoucher() {
		
        Random rand = new Random();
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index = rand.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        String randomString = sb.toString();
        
        Voucher voucher = new Voucher();
        voucher.setEVC_code(randomString);
        voucher.setUsed(0);
        vRepo.save(voucher);
		
		return "redirect:/dashboardadmin/vouchers";
	}
	
	@PostMapping("/dashboardadmin/vouchers/delete")
	public String deleteVoucher(@RequestParam("code") String code) {
		
		Voucher voucher = vRepo.findByEVCCode(code);
		vRepo.delete(voucher);
		
		return "redirect:/dashboardadmin/vouchers";
	}
	
}
