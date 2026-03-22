package com.cabioglu.tefas.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class UmbrellaFundType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String umbrellaFundTypeName;

	@OneToMany(mappedBy = "umbrellaFundType")
	private List<Fund> fundList;

	public UmbrellaFundType() {

	}

	public UmbrellaFundType(Long id, String umbrellaFundTypeName) {
		super();
		this.id = id;
		this.umbrellaFundTypeName = umbrellaFundTypeName;
	}

	public UmbrellaFundType(Long id, String umbrellaFundTypeName, List<Fund> fundList) {
		super();
		this.id = id;
		this.umbrellaFundTypeName = umbrellaFundTypeName;
		this.fundList = fundList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUmbrellaFundTypeName() {
		return umbrellaFundTypeName;
	}

	public void setUmbrellaFundTypeName(String umbrellaFundTypeName) {
		this.umbrellaFundTypeName = umbrellaFundTypeName;
	}

}
