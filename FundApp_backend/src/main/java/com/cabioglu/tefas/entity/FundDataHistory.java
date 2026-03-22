package com.cabioglu.tefas.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FundDataHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "fund_id")
	@JsonIgnore
	private Fund fund;

	private Date date;
	private double unitPrice;
	private double totalUnits;
	private double quantity;
	private double totalValue;

	public FundDataHistory(Long id, Fund fund, Date date, double unitPrice, double totalUnits, double quantity,
			double totalValue) {
		super();
		this.id = id;
		this.fund = fund;
		this.date = date;
		this.unitPrice = unitPrice;
		this.totalUnits = totalUnits;
		this.quantity = quantity;
		this.totalValue = totalValue;
	}

	public FundDataHistory() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(double totalUnits) {
		this.totalUnits = totalUnits;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	@Override
	public String toString() {
		return "FundDataHistory [date=" + date + ", unitPrice=" + unitPrice + ", totalUnits=" + totalUnits
				+ ", quantity=" + quantity + ", totalValue=" + totalValue + "]";
	}

}
