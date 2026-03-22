package com.cabioglu.tefas.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateInterval {

	private Date startDate;
	private Date endDate;
	
	public DateInterval(Date startDate, Date endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public DateInterval(String startDate, String endDate) throws ParseException {
		super();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		this.startDate = formatter.parse(startDate);
		this.endDate = formatter.parse(endDate);
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getStartDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(this.startDate);
	}
	
	public String getEndDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(this.endDate);
	}
	
	public String getStartDateStringforTCMB() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(this.startDate);
	}
	
	public String getEndDateStringforTCMB() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(this.endDate);
	}
	

}
