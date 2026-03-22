package com.cabioglu.tefas.dto;

import com.cabioglu.tefas.entity.Fund;

public class FundDTO {

	private Fund fund;
	private FundPerformanceDTO performanceDTO;

	public FundDTO() {
		
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public FundPerformanceDTO getPerformanceDTO() {
		return performanceDTO;
	}

	public void setPerformanceDTO(FundPerformanceDTO performanceDTO) {
		this.performanceDTO = performanceDTO;
	}

}
