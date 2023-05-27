package com.example.demo.repos;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.reading.Reading;

public interface ReadingRepo extends JpaRepository<Reading, Long> {
	
	@Query("SELECT u FROM Reading u WHERE u.reading_id = ?1")
	Reading findByReadingID(long reading_id);
	
	@Query("SELECT u FROM Reading u WHERE u.customer_id = :field1")
	Collection<Reading> findByCustomerID(@Param("field1") String customerID);
	
	@Query(value = "select * from reading where reading_id = (select max(reading_id) from reading where reading_id < ?1)",
			nativeQuery = true)
	Reading findPreviousReading(long reading_id);

	@Query(value = "SELECT * FROM Reading ORDER BY submission_date DESC LIMIT 1;", 
			nativeQuery = true)
	Reading findLastReading();
	
	@Query(value = "SELECT * FROM Reading ORDER BY submission_date ASC LIMIT 1;", 
			nativeQuery = true)
	Reading findFirstReading();
	
}
