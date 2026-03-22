package com.cabioglu.tefas.dto;

import java.util.ArrayList;
import java.util.List;

import com.cabioglu.tefas.entity.Fund;
import com.cabioglu.tefas.entity.FundDataHistory;
import com.cabioglu.tefas.entity.FundFounder;
import com.cabioglu.tefas.entity.FundPerformance;
import com.cabioglu.tefas.entity.FundTitleType;
import com.cabioglu.tefas.entity.UmbrellaFundType;

public class FundDetailDTO {
	
	private Long id;

	private String code;

	private String fundName;

	private boolean isProcessedTefas;
	
	private FundFounder fundFounder;

	private UmbrellaFundType umbrellaFundType;

	private FundPerformance fundPerformance;

	private List<FundTitleType> fundTitles = new ArrayList<FundTitleType>();

	private List<FundDataHistory> dataHistory;

	public FundDetailDTO() {
		
	}
	
	public FundDetailDTO(Fund fund) {
		super();
		this.id = fund.getId();
		this.code = fund.getCode();
		this.fundName = fund.getFundName();
		this.isProcessedTefas = fund.isProcessedTefas();
		this.fundFounder = fund.getFundFounder();
		this.umbrellaFundType = fund.getUmbrellaFundType();
		this.fundTitles = fund.getFundTitles();
		this.dataHistory = fund.getDataHistory();
		this.fundPerformance = fund.getFundPerformance();
	}

	public FundDetailDTO(Long id, String code, String fundName, boolean isProcessedTefas, FundFounder fundFounder,
			UmbrellaFundType umbrellaFundType, List<FundTitleType> fundTitles, List<FundDataHistory> dataHistory) {
		super();
		this.id = id;
		this.code = code;
		this.fundName = fundName;
		this.isProcessedTefas = isProcessedTefas;
		this.fundFounder = fundFounder;
		this.umbrellaFundType = umbrellaFundType;
		this.fundTitles = fundTitles;
		this.dataHistory = dataHistory;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public boolean isProcessedTefas() {
		return isProcessedTefas;
	}

	public void setProcessedTefas(boolean isProcessedTefas) {
		this.isProcessedTefas = isProcessedTefas;
	}

	public FundFounder getFundFounder() {
		return fundFounder;
	}

	public void setFundFounder(FundFounder fundFounder) {
		this.fundFounder = fundFounder;
	}

	public UmbrellaFundType getUmbrellaFundType() {
		return umbrellaFundType;
	}

	public void setUmbrellaFundType(UmbrellaFundType umbrellaFundType) {
		this.umbrellaFundType = umbrellaFundType;
	}

	public List<FundTitleType> getFundTitles() {
		return fundTitles;
	}

	public void setFundTitles(List<FundTitleType> fundTitles) {
		this.fundTitles = fundTitles;
	}

	public List<FundDataHistory> getDataHistory() {
		return dataHistory;
	}

	public void setDataHistory(List<FundDataHistory> dataHistory) {
		this.dataHistory = dataHistory;
	}

	public FundPerformance getFundPerformance() {
		return fundPerformance;
	}

	public void setFundPerformance(FundPerformance fundPerformance) {
		this.fundPerformance = fundPerformance;
	}
	
	
	

}
