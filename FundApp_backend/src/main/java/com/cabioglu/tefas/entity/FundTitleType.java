package com.cabioglu.tefas.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class FundTitleType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fundTitleTypeName;

	@ManyToMany(mappedBy = "fundTitles")
	private List<Fund> fundTitle_fund;

	public FundTitleType() {

	}

	public FundTitleType(Long id, String fundTitleTypeName) {
		super();
		this.id = id;
		this.fundTitleTypeName = fundTitleTypeName;
	}

	public FundTitleType(Long id, String fundTitleTypeName, List<Fund> fundTitle_fund) {
		super();
		this.id = id;
		this.fundTitleTypeName = fundTitleTypeName;
		this.fundTitle_fund = fundTitle_fund;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFundTitleTypeName() {
		return fundTitleTypeName;
	}

	public void setFundTitleTypeName(String fundTitleTypeName) {
		this.fundTitleTypeName = fundTitleTypeName;
	}

}
