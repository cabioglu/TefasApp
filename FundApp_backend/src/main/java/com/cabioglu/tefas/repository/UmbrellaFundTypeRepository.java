package com.cabioglu.tefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cabioglu.tefas.entity.UmbrellaFundType;

public interface UmbrellaFundTypeRepository extends JpaRepository<UmbrellaFundType, Long> {

	@Query("Select u from UmbrellaFundType u where u.umbrellaFundTypeName = ?1 ")
	UmbrellaFundType findByName(String umbrellaFundTypeName);

}
