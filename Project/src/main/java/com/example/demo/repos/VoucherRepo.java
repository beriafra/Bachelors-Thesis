package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.voucher.Voucher;

public interface VoucherRepo extends JpaRepository<Voucher, String> {

	@Query("SELECT u FROM Voucher u WHERE u.EVC_code = ?1")
	Voucher findByEVCCode(String EVC_code);
}
