package com.cabioglu.tefas.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cabioglu.tefas.dto.DateInterval;
import com.cabioglu.tefas.dto.FundDTO;
import com.cabioglu.tefas.dto.FundDataHistoryDTO;
import com.cabioglu.tefas.dto.FundPerformanceDTO;
import com.cabioglu.tefas.entity.Fund;
import com.cabioglu.tefas.entity.FundDataHistory;
import com.cabioglu.tefas.repository.FundDataHistroyRepository;
import com.cabioglu.tefas.repository.FundRepository;

@Service
public class FundHistoryService {

	@Autowired
	FundDataHistroyRepository dataHistroyRepository;
	
	@Autowired
	FundRepository fundRepository;

	public List<FundDataHistoryDTO> getAllFundsHistory() {
		return dataHistroyRepository.getAllFundHistoryDetails();
	}
	
	public List<FundDTO> getHistoryDataWithChanges() {
		// Calculate adjusted dates for different intervals
		Date baseDate = adjustToNearestWeekday(new Date());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Date oneDayEarlier = calculateAdjustedDate(baseDate, -1, Calendar.DATE);
		Date oneWeekEarlier = calculateAdjustedDate(baseDate, -1, Calendar.WEEK_OF_YEAR);
		//Date oneMonthEarlier = calculateAdjustedDate(baseDate, -1, Calendar.MONTH);
		//Date threeMonthsEarlier = calculateAdjustedDate(baseDate, -3, Calendar.MONTH);
		//Date sixMonthsEarlier = calculateAdjustedDate(baseDate, -6, Calendar.MONTH);
		//Date oneYearEarlier = calculateAdjustedDate(baseDate, -1, Calendar.YEAR);
		//Date threeYearsEarlier = calculateAdjustedDate(baseDate, -3, Calendar.YEAR);
		//Date fiveYearsEarlier = calculateAdjustedDate(baseDate, -5, Calendar.YEAR);

		List<Date> dates = Arrays.asList(
		    baseDate, oneDayEarlier, oneWeekEarlier
		);

		// Fetch all funds with data history for the specified dates
		List<Fund> fundList = dataHistroyRepository.findAllWithDataHistory(dates);
		
		List<FundDTO> dtos = new ArrayList();

		for (Fund fund : fundList) {
		    List<FundDataHistory> historyList = fund.getDataHistory();

		    // Find the base data (unit price at baseDate)
		    FundDataHistory baseData = historyList.stream()
		    	    .filter(h -> isSameDay(h.getDate(), baseDate))
		    	    .findFirst()
		    	    .orElse(null);

		    if (baseData != null) {
		        double baseUnitPrice = baseData.getUnitPrice();

		        // Calculate percentage changes directly for each interval
		        double oneDay = historyList.stream()
		        		.filter(h -> isSameDay(h.getDate(), oneDayEarlier))
		            .findFirst()
		            .map(h -> (( baseUnitPrice - h.getUnitPrice()) / h.getUnitPrice()) * 100)
		            .orElse(0.0);

		        double oneWeek = historyList.stream()
		        		.filter(h -> isSameDay(h.getDate(), oneWeekEarlier))
		            .findFirst()
		            .map(h -> (( baseUnitPrice - h.getUnitPrice()) / h.getUnitPrice()) * 100)
		            .orElse(0.0);

		        //double oneMonth = historyList.stream()
		        		//.filter(h -> isSameDay(h.getDate(), oneMonthEarlier))
		            //.findFirst()
		            //.map(h -> (( baseUnitPrice - h.getUnitPrice()) / h.getUnitPrice()) * 100)
		            //.orElse(0.0);

		        //double threeMonth = historyList.stream()
		        		//.filter(h -> isSameDay(h.getDate(), threeMonthsEarlier))
		            //.findFirst()
		            //.map(h -> (( baseUnitPrice - h.getUnitPrice()) / h.getUnitPrice()) * 100)
		            //.orElse(0.0);

		        //double sixMonth = historyList.stream()
		        		//.filter(h -> isSameDay(h.getDate(), sixMonthsEarlier))
		            //.findFirst()
		            //.map(h -> (( baseUnitPrice - h.getUnitPrice()) / h.getUnitPrice()) * 100)
		            //.orElse(0.0);

		        //double oneYear = historyList.stream()
		        		//.filter(h -> isSameDay(h.getDate(), oneYearEarlier))
		            //.findFirst()
		            //.map(h -> (( baseUnitPrice - h.getUnitPrice()) / h.getUnitPrice()) * 100)
		            //.orElse(0.0);

		        //double threeYear = historyList.stream()
		        		//.filter(h -> isSameDay(h.getDate(), threeYearsEarlier))
		            //.findFirst()
		            //.map(h -> (( baseUnitPrice - h.getUnitPrice()) / h.getUnitPrice()) * 100)
		            //.orElse(0.0);

		        //double fiveYear = historyList.stream()
		        		//.filter(h -> isSameDay(h.getDate(), fiveYearsEarlier))
		            //.findFirst()
		            //.map(h -> (( baseUnitPrice - h.getUnitPrice()) / h.getUnitPrice()) * 100)
		            //.orElse(0.0);

		        // Optionally print or log the results
		        //System.out.printf("Fund: %s, OneDay: %.2f%%, OneWeek: %.2f%%, OneMonth: %.2f%%, ThreeMonth: %.2f%%, SixMonth: %.2f%%, OneYear: %.2f%%, ThreeYear: %.2f%%, FiveYear: %.2f%%\n",
		        //    fund.getFundName(), oneDay, oneWeek, oneMonth, threeMonth, sixMonth, oneYear, threeYear, fiveYear);
		       FundDTO dto = new FundDTO();
		       dto.setFund(fund);
			   if(fund.getFundPerformance() != null){
				dto.setPerformanceDTO(new FundPerformanceDTO(oneDay, oneWeek,fund.getFundPerformance().getOneMonth(),fund.getFundPerformance().getThreeMonths(),fund.getFundPerformance().getSixMonths(),fund.getFundPerformance().getOneYear(),fund.getFundPerformance().getThreeYears(),fund.getFundPerformance().getFiveYears()));
			   }else{
				dto.setPerformanceDTO(new FundPerformanceDTO(oneDay, oneWeek,0.0,0.0,0.0,0.0,0.0,0.0));
			   }
		       //dto.setPerformanceDTO(new FundPerformanceDTO(oneDay, oneWeek,fund.getFundPerformance().getOneMonth(),fund.getFundPerformance().getThreeMonths(),fund.getFundPerformance().getSixMonths(),fund.getFundPerformance().getOneYear(),fund.getFundPerformance().getThreeYears(),fund.getFundPerformance().getFiveYears()));
		       dtos.add(dto);
		    }
		}

		
		
		return dtos;
	}
	
	public List<Fund> getHistoryDataWithDate(DateInterval date) throws MalformedURLException {
		// Set the WebDriver path for ChromeDriver or another browser driver you're
		// using
	//	System.setProperty("webdriver.chrome.driver",
	//			"..\\FundApp_backend\\\\src\\\\main\\\\resources\\\\driver\\\\chromedriver.exe");

		// Set ChromeOptions for headless mode
		ChromeOptions options = new ChromeOptions();

		// Initialize WebDriver
		WebDriver driver = new RemoteWebDriver(new URL("http://192.168.1.104:4444/wd/hub"), options);
		//WebDriver driver = new ChromeDriver(options);

		int retryCount = 0;
		boolean success = false;

		try {
			// Navigate to the webpage with the form
			driver.get("https://www.tefas.gov.tr/TarihselVeriler.aspx");

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));

			wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
					.equals("complete"));

			System.out.println("Page Ready state");

			wait.until(ExpectedConditions.attributeToBe(By.id("table_general_info_processing"), "style",
					"display: none;"));

			System.out.println("Veriler Yüklendi...");

			WebElement dropdownElement = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.name("table_general_info_length")));

			// Create a Select object to interact with the dropdown
			Select select = new Select(dropdownElement);

			// Select the option by its value (250)
			select.selectByValue("250");

			// Verify the selection (optional)
			String selectedValue = select.getFirstSelectedOption().getText();
			System.out.println("Selected value: " + selectedValue); // Should print "250"

			// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("table_general_info_processing")));
			// Locate the start date field and input the value
			WebElement startDateField = driver.findElement(By.id("TextBoxStartDate"));
			startDateField.clear();
			startDateField.sendKeys(date.getStartDateString());

			// Locate the end date field and input the value
			WebElement endDateField = driver.findElement(By.id("TextBoxEndDate"));
			endDateField.clear();
			endDateField.sendKeys(date.getEndDateString());

			// Locate and click the search button
			WebElement searchButton = driver.findElement(By.id("ButtonSearchDates"));
			searchButton.click();

			// Wait for the table to be visible
			WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(120));

			wait2.until(ExpectedConditions.attributeToBe(By.id("table_general_info_processing"), "style",
					"display: none;"));

			System.out.println("table_general_info");
			List<Fund> fundDataList = new ArrayList<>();

			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

			// Loop through the pages
			boolean hasNextPage = true;
			while (hasNextPage) {
				// Thread.sleep(1000);

				boolean retry = true;
				while (retry) {
					try {
						// Locate the table and rows
						WebElement table = driver.findElement(By.id("table_general_info"));
						List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));
						
						// Loop through the rows and print the results
						for (WebElement row : rows) {
							List<WebElement> columns = row.findElements(By.tagName("td"));
							if (columns.size() == 7) { // Ensure correct number of columns

								Date histDate = formatter.parse(columns.get(0).getText().replace("\"", ""));
								String code = columns.get(1).getText().replace("\"", "");
								String fundName = columns.get(2).getText().replace("\"", "");
								double unitPrice = parseNumber(columns.get(3).getText());
								double totalUnits = parseNumber(columns.get(4).getText());
								double quantity = parseNumber(columns.get(5).getText());
								double totalValue = parseNumber(columns.get(6).getText());
								
								Fund fund = fundRepository.findByCode(code);

								if (fund == null) {
									fund = new Fund(code, fundName);
									fund = fundRepository.save(fund);
								}

								FundDataHistory fundData = new FundDataHistory(null, fund, histDate, unitPrice, totalUnits,
										quantity, totalValue);

								List<FundDataHistory> fundDataHistoryList = new ArrayList<FundDataHistory>();
								fundDataHistoryList.add(fundData);
								
								if(!dataHistroyRepository.existsFundHistoryDetail(code, histDate))
									dataHistroyRepository.save(fundData);

								// System.out.println(fundData.toString()); // Print a new line after each row
								fundDataList.add(fund);
							}

							retry = false;
						}
						System.out.println("Sayfa bitti " + new Date());
					} catch (StaleElementReferenceException e) {
						// Element is stale, retry by re-locating the element
						System.out.println("Element is stale, retrying...");
					}
				}

				// Check for the next page button
				WebElement nextPageButton = driver.findElement(By.id("table_general_info_next"));
				if (nextPageButton.getAttribute("class").contains("disabled")) {
					hasNextPage = false; // No more pages to navigate
				} else {
					nextPageButton.click(); // Click the next page button
					wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("table_general_info"))); // Wait for
																												// the
																												// table
																												// to
																												// reload
				}
			}
			return fundDataList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// Close the driver after the test
			driver.quit();
		}
	}
	
	private static double parseNumber(String numberStr) {
		if (!numberStr.equalsIgnoreCase("-")) {
			String cleanedNumber = numberStr.replace(".", "").replace(",", ".");
			return Double.parseDouble(cleanedNumber);
		} else {
			return 0;
		}
		// Replace dots for thousands with empty and commas for decimals with dot

	}
	
	private Date adjustToNearestWeekday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SATURDAY) {
            // If the date is Saturday, move back to Friday
            calendar.add(Calendar.DATE, -1);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            // If the date is Sunday, move back to Friday
            calendar.add(Calendar.DATE, -2);
        }

		Date maxDate = dataHistroyRepository.getMaxDate();
		if(maxDate != null){
			calendar.setTime(maxDate);
			return calendar.getTime();
		}

        return calendar.getTime();
    }
	
	public Date calculateAdjustedDate(Date baseDate, int amount, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);
        calendar.add(field, amount); // Add/subtract the time interval
        return adjustToNearestWeekday(calendar.getTime()); // Adjust if it falls on a weekend
    }
	
	public boolean isSameDay(Date date1, Date date2) {
	    Calendar cal1 = Calendar.getInstance();
	    Calendar cal2 = Calendar.getInstance();
	    cal1.setTime(date1);
	    cal2.setTime(date2);
	    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
	           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}

}
