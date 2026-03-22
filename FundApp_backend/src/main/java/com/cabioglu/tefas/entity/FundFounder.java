package com.cabioglu.tefas.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class FundFounder {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fundFounderName;
	
	@OneToMany(mappedBy = "fundFounder")
	@JsonIgnore
	private List<Fund> fundList = new ArrayList<Fund>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFundFounderName() {
		return fundFounderName;
	}

	public void setFundFounderName(String fundFounderName) {
		this.fundFounderName = fundFounderName;
	}

	public List<Fund> getFundList() {
		return fundList;
	}

	public void setFundList(List<Fund> fundList) {
		this.fundList = fundList;
	}

	
}
