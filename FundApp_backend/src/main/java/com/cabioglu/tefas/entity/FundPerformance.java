package com.cabioglu.tefas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;

@Entity
public class FundPerformance {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fund_id")
    private Fund fund;

    private Double oneDay;
    private Double oneWeek;
    private Double oneMonth;
    private Double threeMonths;
    private Double sixMonths;
    private Double oneYear;
    private Double threeYears;
    private Double fiveYears;


    public FundPerformance() {
    }

    public FundPerformance(Fund fund,Double oneDay, Double oneWeek, Double oneMonth, Double threeMonths, Double sixMonths, Double oneYear, Double threeYears, Double fiveYears) {
        this.fund = fund;
        this.oneDay = oneDay;
        this.oneWeek = oneWeek;
        this.oneMonth = oneMonth;
        this.threeMonths = threeMonths;
        this.sixMonths = sixMonths;
        this.oneYear = oneYear;
        this.threeYears = threeYears;
        this.fiveYears = fiveYears;
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

    public Double getOneWeek() {
        return oneWeek;
    }

    public void setOneWeek(Double oneWeek) {
        this.oneWeek = oneWeek;
    }

    public Double getOneMonth() {
        return oneMonth;
    }

    public void setOneMonth(Double oneMonth) {
        this.oneMonth = oneMonth;
    }

    public Double getThreeMonths() {
        return threeMonths;
    }

    public void setThreeMonths(Double threeMonths) {
        this.threeMonths = threeMonths;
    }

    public Double getSixMonths() {
        return sixMonths;
    }

    public void setSixMonths(Double sixMonths) {
        this.sixMonths = sixMonths;
    }

    public Double getOneYear() {
        return oneYear;
    }

    public void setOneYear(Double oneYear) {
        this.oneYear = oneYear;
    }

    public Double getThreeYears() {
        return threeYears;
    }

    public void setThreeYears(Double threeYears) {
        this.threeYears = threeYears;
    }

    public Double getFiveYears() {
        return fiveYears;
    }

    public void setFiveYears(Double fiveYears) {
        this.fiveYears = fiveYears;
    }
    
    
    
}
