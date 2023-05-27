package com.example.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.customer.Customer;

public interface CustomerRepo extends JpaRepository<Customer, String> {
	
	@Query("SELECT u FROM Customer u WHERE u.customer_id = ?1")
	Customer findByCustomerID(String CustomerID);

	@Query("SELECT u FROM Customer u WHERE u.customer_id = ?1 AND u.password_hash = ?2")
	String findPasswordHash(String CustomerID, String PasswordHash);
	
	@Query("SELECT u FROM Customer u WHERE u.customer_id = ?1 AND u.type = ?2")
	String findCustomerType(String CustomerID, String Type);
	
}
