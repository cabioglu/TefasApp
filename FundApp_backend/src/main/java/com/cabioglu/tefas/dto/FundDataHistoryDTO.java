package com.cabioglu.tefas.dto;

import java.util.Date;

public class FundDataHistoryDTO {

	private Long historyId;
	private String code;
	private String fundName;
	private Date date;
	private double unitPrice;
	private double totalUnits;
	private double quantity;
	private double totalValue;

	public FundDataHistoryDTO(Long historyId, String code, String fundName, Date date, double unitPrice,
			double totalUnits, double quantity, double totalValue) {

		this.historyId = historyId;
		this.code = code;
		this.fundName = fundName;
		this.date = date;
		this.unitPrice = unitPrice;
		this.totalUnits = totalUnits;
		this.quantity = quantity;
		this.totalValue = totalValue;
	}

	public FundDataHistoryDTO() {

	}

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
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
		StringBuilder builder = new StringBuilder();
		builder.append("FundDataHistoryDTO [historyId=");
		builder.append(historyId);
		builder.append(", code=");
		builder.append(code);
		builder.append(", fundName=");
		builder.append(fundName);
		builder.append(", date=");
		builder.append(date);
		builder.append(", unitPrice=");
		builder.append(unitPrice);
		builder.append(", totalUnits=");
		builder.append(totalUnits);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", totalValue=");
		builder.append(totalValue);
		builder.append("]");
		return builder.toString();
	}

}
