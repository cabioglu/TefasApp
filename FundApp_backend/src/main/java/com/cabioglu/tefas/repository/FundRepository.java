package com.cabioglu.tefas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cabioglu.tefas.entity.Fund;

public interface FundRepository extends JpaRepository<Fund, Long> {
	
	@Query("Select f from Fund f where f.code = ?1 ")
	Fund findByCode(String code);
	
	@Query("Select f from Fund f where f.id = ?1 ")
	Fund getFundById(Long id);
	
	@Query("Select f from Fund f where f.code IN :codes")
	List<Fund> findAllByCodeIn(List<String> codes);
}