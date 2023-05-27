package com.example.demo.reading;

import java.sql.Date;

import com.example.demo.customer.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="reading")
public class Reading {
	
	@Id
	@Column(nullable = false, unique = true, length = 4)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reading_id;
	
    @Column(name = "customer_id", insertable=false, updatable=false)
    private String customer_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Column(nullable = false, length = 50)
	private java.sql.Date submission_date;
	
	@Column(nullable = true, length = 50)
	private long elec_readings_day;
	
	@Column(nullable = true, length = 50)
	private long elet_reading_night;
	
	@Column(nullable = true, length = 50)
	private long gas_reading;
	
	@Column(nullable = false, length = 50)
	private String status;
	
	
	public Long getReading_id() {
		return reading_id;
	}
	public void setReading_id(Long reading_id) {
		this.reading_id = reading_id;
	}
	public Customer getCustomer_id() {
		return customer;
	}
	public void setCustomer_id(Customer customer) {
		this.customer = customer;
	}
	public Date getSubmission_date() {
		return submission_date;
	}
	public void setSubmission_date(Date submission_date) {
		this.submission_date = submission_date;
	}
	public long getGas_reading() {
		return gas_reading;
	}
	public void setGas_reading(long gas_reading) {
		this.gas_reading = gas_reading;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getElec_readings_day() {
		return elec_readings_day;
	}
	public void setElec_readings_day(long elec_readings_day) {
		this.elec_readings_day = elec_readings_day;
	}
	public long getElet_reading_night() {
		return elet_reading_night;
	}
	public void setElet_reading_night(long elet_reading_night) {
		this.elet_reading_night = elet_reading_night;
	}
	

}
