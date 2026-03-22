package com.cabioglu.tefas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabioglu.tefas.dto.FundDetailDTO;
import com.cabioglu.tefas.entity.Fund;
import com.cabioglu.tefas.service.FundService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/fund")
public class FundController {

	@Autowired
	FundService service;

	@GetMapping("/getfunds")
	List<Fund> getFunds() {
		return service.getFunds();
	}

	@GetMapping("/getfundbyid/{fundId}")
	FundDetailDTO getFundById(@PathVariable Long fundId) {
		return service.getFundById(fundId);
	}

	@GetMapping("/getalldata")
	List<Fund> getAllData() {
		return service.getAllData();
	}

	@GetMapping("/fundsfech")
	void getFundsandTypes() {
		service.getFundsandTypes();
	}

}
