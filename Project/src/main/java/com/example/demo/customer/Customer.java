package com.example.demo.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="customer")
public class Customer {
	
	@Id
	@Column(nullable = false, unique = true, length = 40)
	private String customer_id;
	
	@Column(nullable = false, length = 100)
	private String password_hash;
	
	@Column(nullable = false, length = 50)
	private String address;
	
	@Column(nullable = false, length = 20)
	private String property_type;
	
	@Column(nullable = false, length = 2)
	private int bedroom_num;
	
	@Column
	private double balance;
	
	@Column
	private String type;
	
//	@OneToMany(mappedBy = "customer")
//	private Set<Reading> readings;

	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getPassword_hash() {
		return password_hash;
	}
	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProperty_type() {
		return property_type;
	}
	public void setProperty_type(String property_type) {
		this.property_type = property_type;
	}
	public int getBedroom_num() {
		return bedroom_num;
	}
	public void setBedroom_num(int bedroom_num) {
		this.bedroom_num = bedroom_num;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double d) {
		d = Math.round(d * 100.0) / 100.0;
		this.balance = d;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
//	public Set<Reading> getReadings() {
//		return readings;
//	}
//	public void setReadings(Set<Reading> readings) {
//		this.readings = readings;
//	}
}
