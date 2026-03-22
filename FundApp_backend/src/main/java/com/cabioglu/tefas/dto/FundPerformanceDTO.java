package com.cabioglu.tefas.dto;

public class FundPerformanceDTO {
	
	double oneDay;
	double oneWeek;
	double oneMonth;
	double threeMonth;
	double sixMonth;
	double oneYear;
	double threeYear;
	double fiveYear;
	
	public FundPerformanceDTO() {
		
	}
	
	public FundPerformanceDTO(double oneDay, double oneWeek, double oneMonth, double threeMonth, double sixMonth,
			double oneYear, double threeYear, double fiveYear) {
		super();
		this.oneDay = oneDay;
		this.oneWeek = oneWeek;
		this.oneMonth = oneMonth;
		this.threeMonth = threeMonth;
		this.sixMonth = sixMonth;
		this.oneYear = oneYear;
		this.threeYear = threeYear;
		this.fiveYear = fiveYear;
	}

	public double getOneDay() {
		return oneDay;
	}

	public void setOneDay(double oneDay) {
		this.oneDay = oneDay;
	}

	public double getOneWeek() {
		return oneWeek;
	}

	public void setOneWeek(double oneWeek) {
		this.oneWeek = oneWeek;
	}

	public double getOneMonth() {
		return oneMonth;
	}

	public void setOneMonth(double oneMonth) {
		this.oneMonth = oneMonth;
	}

	public double getThreeMonth() {
		return threeMonth;
	}

	public void setThreeMonth(double threeMonth) {
		this.threeMonth = threeMonth;
	}

	public double getSixMonth() {
		return sixMonth;
	}

	public void setSixMonth(double sixMonth) {
		this.sixMonth = sixMonth;
	}

	public double getOneYear() {
		return oneYear;
	}

	public void setOneYear(double oneYear) {
		this.oneYear = oneYear;
	}

	public double getThreeYear() {
		return threeYear;
	}

	public void setThreeYear(double threeYear) {
		this.threeYear = threeYear;
	}

	public double getFiveYear() {
		return fiveYear;
	}

	public void setFiveYear(double fiveYear) {
		this.fiveYear = fiveYear;
	}
	
	

	
}
