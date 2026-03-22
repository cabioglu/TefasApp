package com.cabioglu.tefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cabioglu.tefas.entity.FundFounder;

public interface FunFounderRepository extends JpaRepository<FundFounder, Long> {

	@Query("Select u from FundFounder u where u.fundFounderName = ?1 ")
	FundFounder findByName(String fundFounderName);

}
