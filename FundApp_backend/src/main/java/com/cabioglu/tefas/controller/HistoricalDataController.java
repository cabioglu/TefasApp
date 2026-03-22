package com.cabioglu.tefas.controller;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cabioglu.tefas.dto.DateInterval;
import com.cabioglu.tefas.dto.FundDTO;
import com.cabioglu.tefas.dto.FundDataHistoryDTO;
import com.cabioglu.tefas.entity.Fund;
import com.cabioglu.tefas.service.FundHistoryService;

@RestController
@RequestMapping("/history")
public class HistoricalDataController {

	@Autowired
	FundHistoryService service;

	@CrossOrigin(origins = "http://localhost:5173")
	@GetMapping("/getallhistory")
	List<FundDataHistoryDTO> getAllFundsHistory() {
		return service.getAllFundsHistory();
	}

	@CrossOrigin(origins = "http://localhost:5173")
	@GetMapping("/gethistorywithdate")
	List<Fund> getHistoryDataWithDate(@RequestParam String startDate, @RequestParam String endDate)
			throws ParseException, MalformedURLException {
		return service.getHistoryDataWithDate(new DateInterval(startDate, endDate));
	}

	@CrossOrigin(origins = "http://localhost:5173")
	@GetMapping("/gethistorydatawithchanges")
	List<FundDTO> getHistoryDataWithChanges() throws ParseException {
		return service.getHistoryDataWithChanges();
	}

}
