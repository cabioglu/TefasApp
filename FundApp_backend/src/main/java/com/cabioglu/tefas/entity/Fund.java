package com.cabioglu.tefas.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Fund {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	private String fundName;

	private boolean isProcessedTefas;

	@ManyToOne
	@JoinColumn(name = "fund_founder_id")
	private FundFounder fundFounder;

	@ManyToOne
	@JoinColumn(name = "umbrella_fund_type_id")
	private UmbrellaFundType umbrellaFundType;

	@ManyToMany
	@JoinTable(name = "fund_fund_title", joinColumns = @JoinColumn(name = "fund_id"), inverseJoinColumns = @JoinColumn(name = "fund_title_id"))
	private List<FundTitleType> fundTitles = new ArrayList<FundTitleType>();

	@OneToMany(mappedBy = "fund")
	@Fetch(FetchMode.SUBSELECT)
	@JsonIgnore
	private List<FundDataHistory> dataHistory;

	@OneToOne(mappedBy = "fund")
	@JsonIgnore
	private FundPerformance fundPerformance;

	public Fund(String code, String fundName) {
		super();
		this.code = code;
		this.fundName = fundName;
	}

	public Fund(String code, String fundName, List<FundDataHistory> dataHistory) {
		super();
		this.code = code;
		this.fundName = fundName;
		this.dataHistory = dataHistory;
	}

	public Fund(Long id, String code, String fundName, UmbrellaFundType umbrellaFundType,
			List<FundTitleType> fundTitles, List<FundDataHistory> dataHistory) {
		super();
		this.id = id;
		this.code = code;
		this.fundName = fundName;
		this.umbrellaFundType = umbrellaFundType;
		this.fundTitles = fundTitles;
		this.dataHistory = dataHistory;
	}

	public Fund(Long id, String code, String fundName, boolean isProcessedTefas, UmbrellaFundType umbrellaFundType) {
		super();
		this.id = id;
		this.code = code;
		this.fundName = fundName;
		this.isProcessedTefas = isProcessedTefas;
		this.umbrellaFundType = umbrellaFundType;
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

	public Fund() {
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Fund [id=");
		builder.append(id);
		builder.append(", code=");
		builder.append(code);
		builder.append(", fundName=");
		builder.append(fundName);
		builder.append(", dataHistory=");
		builder.append(dataHistory);
		builder.append("]");
		return builder.toString();
	}

}
