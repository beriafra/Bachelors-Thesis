package com.example.demo.voucher;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="voucher")
public class Voucher {
	
	@Id
	@Column(nullable = false, unique = true, length = 20)
	private String EVC_code;
	
	@Column(nullable = false, length = 1)
	private int used;
	
	public String getEVC_code() {
		return EVC_code;
	}
	public void setEVC_code(String eVC_code) {
		EVC_code = eVC_code;
	}
	public int getUsed() {
		return used;
	}
	public void setUsed(int used) {
		this.used = used;
	}

}
