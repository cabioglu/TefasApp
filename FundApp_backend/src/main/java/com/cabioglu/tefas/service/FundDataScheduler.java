package com.cabioglu.tefas.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cabioglu.tefas.dto.DateInterval;
import jakarta.annotation.PostConstruct;

@Service
public class FundDataScheduler {

	@Autowired
	FundHistoryService fundHistoryService;

	@Scheduled(cron = "0 35 10 * * ?") // Runs every day at 10:30 AM
	//@Scheduled(cron = "0 * * * * ?") // Runs every minute
	public void fetchAndSaveFundData() {
		try {
			// Generate today's date range
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Calendar calendar = Calendar.getInstance();
			String today = dateFormat.format(calendar.getTime());

			// Assuming DateInterval is a class that accepts start and end dates
			DateInterval dateInterval = new DateInterval(today, today);

			// Call the function
			fundHistoryService.getHistoryDataWithDate(dateInterval);

			System.out.println("Fund data fetched successfully at 10:30 AM.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* @PostConstruct
	public void fetchHistoricalFundData() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Calendar startDate = Calendar.getInstance();
			
			// Set start date to 27.04.2020
			startDate.set(2020, Calendar.APRIL, 27);
			
			// Get today's date
			Calendar today = Calendar.getInstance();
			
			while (startDate.before(today)) {
				String startDateStr = dateFormat.format(startDate.getTime());
				
				// Add 3 months to get end date
				Calendar endDate = (Calendar) startDate.clone();
				endDate.add(Calendar.MONTH, 3);
				if (endDate.after(today)) {
					endDate = today;
				}
				String endDateStr = dateFormat.format(endDate.getTime());
				
				// Create date interval and fetch data
				DateInterval dateInterval = new DateInterval(startDateStr, endDateStr);
				fundHistoryService.getHistoryDataWithDate(dateInterval);
				
				System.out.println("Historical fund data fetched for period: " + startDateStr + " to " + endDateStr);
				
				// Move to next period
				startDate.add(Calendar.MONTH, 3);
			}
			
			System.out.println("All historical fund data fetched successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	} */
}
