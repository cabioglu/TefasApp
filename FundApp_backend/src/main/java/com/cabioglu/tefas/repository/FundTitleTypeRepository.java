package com.cabioglu.tefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cabioglu.tefas.entity.FundTitleType;

public interface FundTitleTypeRepository extends JpaRepository<FundTitleType, Long> {

	@Query("Select u from FundTitleType u where u.fundTitleTypeName = ?1 ")
	FundTitleType findByName(String fundTitleTypeName);

}
