package com.cabioglu.tefas.controller;

import java.text.ParseException;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cabioglu.tefas.dto.DateInterval;
import com.cabioglu.tefas.entity.Fund;
import com.cabioglu.tefas.service.TCMBService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/tcmb")
public class TCMBController {

	@Autowired
	TCMBService service;

	@CrossOrigin(origins = "http://localhost:5173")
	@GetMapping("/getusdtrywithdate")
	String getUSDTRY(@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
		return service.getUSDTRY(new DateInterval(startDate, endDate)).toString();
	}

}
