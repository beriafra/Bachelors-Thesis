package com.example.demo.taiff;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="taiff")
public class Taiff {
	
	@Id
	@Column(nullable = false, unique = true, length = 20)
	private String taiff_type;
	
	@Column(nullable = false, length = 5)
	private double rate;
	

	public String getTaiff_type() {
		return taiff_type;
	}
	public void setTaiff_type(String taiff_type) {
		this.taiff_type = taiff_type;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}

}
