package com.cabioglu.tefas.service;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabioglu.tefas.dto.FundDetailDTO;
import com.cabioglu.tefas.entity.Fund;
import com.cabioglu.tefas.entity.FundDataHistory;
import com.cabioglu.tefas.entity.FundFounder;
import com.cabioglu.tefas.entity.FundPerformance;
import com.cabioglu.tefas.entity.FundTitleType;
import com.cabioglu.tefas.entity.UmbrellaFundType;
import com.cabioglu.tefas.repository.FunFounderRepository;
import com.cabioglu.tefas.repository.FundDataHistroyRepository;
import com.cabioglu.tefas.repository.FundRepository;
import com.cabioglu.tefas.repository.FundTitleTypeRepository;
import com.cabioglu.tefas.repository.UmbrellaFundTypeRepository;
import com.cabioglu.tefas.repository.FundPerformanceRepository;

@Service
public class FundService {

	@Autowired
	FundRepository fundRepository;

	@Autowired
	FundDataHistroyRepository dataHistroyRepository;

	@Autowired
	UmbrellaFundTypeRepository umbrellaFundTypeRepository;

	@Autowired
	FundTitleTypeRepository fundTitleTypeRepository;

	@Autowired
	FunFounderRepository funFounderRepository;

	@Autowired
	FundPerformanceRepository fundPerformanceRepository;

	public FundDetailDTO getFundById(long id) {
		Fund fund = fundRepository.getFundById(id);
		return new FundDetailDTO(fund);
	}

	public List<Fund> getFunds() {
		return fundRepository.findAll();
	}

	public List<Fund> getAllData() {
		// Set ChromeOptions for headless mode
		ChromeOptions options = new ChromeOptions();

		// Initialize WebDriver
		WebDriver driver = null;

		int retryCount = 0;
		boolean success = false;

		try {
			driver = new RemoteWebDriver(URI.create("http://selenium-hub:4444/wd/hub").toURL(), options);
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
			startDateField.sendKeys("03.12.2024");

			// Locate the end date field and input the value
			WebElement endDateField = driver.findElement(By.id("TextBoxEndDate"));
			endDateField.clear();
			endDateField.sendKeys("29.12.2024");

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

								Date date = formatter.parse(columns.get(0).getText().replace("\"", ""));
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

								FundDataHistory fundData = new FundDataHistory(null, fund, date, unitPrice, totalUnits,
										quantity, totalValue);

								List<FundDataHistory> fundDataHistoryList = new ArrayList<FundDataHistory>();
								fundDataHistoryList.add(fundData);

								dataHistroyRepository.save(fundData);

								// System.out.println(fundData.toString()); // Print a new line after each row
								fundDataList.add(fund);
							}

							retry = false;
						}
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

	public void getFundsandTypes() {
		// Set ChromeOptions for headless mode
		ChromeOptions options = new ChromeOptions();

		// Initialize WebDriver
		WebDriver driver = null;

		int retryCount = 0;
		boolean success = false;

		try {
			driver = new RemoteWebDriver(URI.create("http://192.168.1.104:4444/wd/hub").toURL(), options);
			// Navigate to the webpage with the form
			driver.get("https://www.tefas.gov.tr/FonKarsilastirma.aspx");

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(240));

			wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
					.equals("complete"));

			System.out.println("Page Ready state");

			wait.until(ExpectedConditions.attributeToBe(By.id("table_fund_returns_processing"), "style",
					"display: none;"));

			System.out.println("Veriler Yüklendi...");

			WebElement dropdownElement = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.name("table_fund_returns_length")));

			// Create a Select object to interact with the dropdown
			Select select = new Select(dropdownElement);

			// Select the option by its value (250)
			select.selectByValue("15");

			WebElement dropdownElementFundType = driver.findElement(By.id("DropDownListFundTypeExplanationYAT"));

			// Wrap the dropdown element in a Select object
			Select dropdownFundType = new Select(dropdownElementFundType);

			WebElement dropdownElementProcessStatus = driver.findElement(By.id("DropDownListProcessStatusYAT"));

			Select dropdownProcessStatus = new Select(dropdownElementProcessStatus);

			WebElement dropdownFundFounder = driver.findElement(By.id("DropDownListFounderYAT"));

			Select dropdownFundFounderSelect = new Select(dropdownFundFounder);

			int count = 0;
			// Get all options in the dropdown
			for (WebElement option : dropdownFundType.getOptions()) {
				// Get the visible text of the option
				String value = option.getText();
				System.out.println("Selecting: " + value);

				if (!value.equals("Tümü")) {
					// Select the option by its visible text
					dropdownFundType.selectByVisibleText(value);

					for (WebElement optionProcess : dropdownProcessStatus.getOptions()) {

						String valueProcess = optionProcess.getText();
						System.out.println("Selecting Process: " + valueProcess);

						if (!valueProcess.equals("Tümü")) {
							dropdownProcessStatus.selectByVisibleText(valueProcess);

							// Verify the selection (optional)
							String selectedValue = select.getFirstSelectedOption().getText();
							System.out.println("Selected value: " + selectedValue); // Should print "250"

							// Wait for the table to be visible
							WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(300));

							wait2.until(ExpectedConditions.attributeToBe(By.id("table_fund_returns_processing"),
									"style", "display: none;"));

							// Optional: Wait for a second to observe the selection
							Thread.sleep(1000);

							System.out.println("table_fund_returns");
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
										WebElement table = driver.findElement(By.id("table_fund_returns"));
										List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));

										// Loop through the rows and print the results
										for (WebElement row : rows) {
											List<WebElement> columns = row.findElements(By.tagName("td"));

											if (columns.size() == 10) { // Ensure correct number of columns

												// Date date =
												// formatter.parse(columns.get(0).getText().replace("\"", ""));
												String code = columns.get(0).getText().replace("\"", "");
												String fundName = columns.get(1).getText().replace("\"", "");
												String umbrellaType = columns.get(2).getText().replace("\"", "");
												double oneMonth = parseNumber(columns.get(3).getText());
												double threeMonth = parseNumber(columns.get(4).getText());
												double sixMonth = parseNumber(columns.get(5).getText());
												double afterNeewYear = parseNumber(columns.get(6).getText());
												double oneYear = parseNumber(columns.get(7).getText());
												double threeYear = parseNumber(columns.get(8).getText());
												double fiveYear = parseNumber(columns.get(9).getText());

												System.out.println("Code: " + code + ", Fund Name: " + fundName
														+ ", Umbrella Type: " + umbrellaType + ", One Month: "
														+ oneMonth + ", Three Month: " + threeMonth + ", Six Month: "
														+ sixMonth + ", After New Year: " + afterNeewYear
														+ ", One Year: " + oneYear + ", Three Year: " + threeYear
														+ ", Five Year: " + fiveYear);

												UmbrellaFundType umbrellaFundType = umbrellaFundTypeRepository
														.findByName(umbrellaType);

												if (umbrellaFundType == null) {
													umbrellaFundType = new UmbrellaFundType();
													umbrellaFundType.setUmbrellaFundTypeName(umbrellaType);
													umbrellaFundType = umbrellaFundTypeRepository
															.save(umbrellaFundType);
												}

												FundTitleType fundTitleType = fundTitleTypeRepository.findByName(value);
												if (fundTitleType == null) {
													fundTitleType = new FundTitleType();
													fundTitleType.setFundTitleTypeName(value);
													fundTitleType = fundTitleTypeRepository.save(fundTitleType);
												}

												Fund fund = fundRepository.findByCode(code);

												if (fund == null) {
													fund = new Fund(code, fundName);
													fund.setProcessedTefas(valueProcess.equals("TEFAS'ta işlem gören"));
													fund.setUmbrellaFundType(umbrellaFundType);
													fund.getFundTitles().add(fundTitleType);
												} else {
													fund.setUmbrellaFundType(umbrellaFundType);
													fund.setProcessedTefas(valueProcess.equals("TEFAS'ta işlem gören"));
													fund.getFundTitles().add(fundTitleType);
												}
												//System.out.println(fund);
												FundPerformance fundPerformance = fund.getFundPerformance();
												if (fundPerformance == null) {
													fundPerformance = new FundPerformance(fund, Double.valueOf(0.0), Double.valueOf(0.0), oneMonth, threeMonth, sixMonth, oneYear, threeYear, fiveYear);
												} else {
													fundPerformance.setOneMonth(oneMonth);
													fundPerformance.setThreeMonths(threeMonth);
													fundPerformance.setSixMonths(sixMonth);
													fundPerformance.setOneYear(oneYear);
													fundPerformance.setThreeYears(threeYear);
													fundPerformance.setFiveYears(fiveYear);
												}
												fundPerformance = fundPerformanceRepository.save(fundPerformance);
												fund.setFundPerformance(fundPerformance);
												fund = fundRepository.save(fund);

												count++;

											}

											retry = false;
										}
									} catch (StaleElementReferenceException e) {
										// Element is stale, retry by re-locating the element
										System.out.println("Element is stale, retrying...");
									}
								}

								// Check for the next page button
								WebElement nextPageButton = driver.findElement(By.id("table_fund_returns_next"));
								if (nextPageButton.getAttribute("class").contains("disabled")) {
									hasNextPage = false; // No more pages to navigate
								} else {
									nextPageButton.click(); // Click the next page button
									wait2.until(
											ExpectedConditions.visibilityOfElementLocated(By.id("table_fund_returns"))); // Wait
																															// for
																															// the
																															// table
																															// to
																															// reload
								}

							}

						}
						// Optional: Wait for a second to observe the selection
						Thread.sleep(5000);

					}
				}

			}
			if (true) {

				for (WebElement optionsFundFounder : dropdownFundFounderSelect.getOptions()) {

					String founderValue = optionsFundFounder.getText();

					if (!founderValue.equals("Tümü")) {

						dropdownFundFounderSelect.selectByVisibleText(founderValue);
						dropdownProcessStatus.selectByVisibleText("Tümü");
						dropdownFundType.selectByVisibleText("Tümü");

						// Optional: Wait for a second to observe the selection
						Thread.sleep(1000);
						WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(120));

						System.out.println("table_fund_returns");
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
									WebElement table = driver.findElement(By.id("table_fund_returns"));
									List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));

									// Loop through the rows and print the results
									for (WebElement row : rows) {
										List<WebElement> columns = row.findElements(By.tagName("td"));

										if (columns.size() == 10) { // Ensure correct number of columns

											// Date date =
											// formatter.parse(columns.get(0).getText().replace("\"", ""));
											String code = columns.get(0).getText().replace("\"", "");
											String fundName = columns.get(1).getText().replace("\"", "");
											String umbrellaType = columns.get(2).getText().replace("\"", "");
											double oneMonth = parseNumber(columns.get(3).getText());
											double threeMonth = parseNumber(columns.get(4).getText());
											double sixMonth = parseNumber(columns.get(5).getText());
											double afterNeewYear = parseNumber(columns.get(6).getText());
											double oneYear = parseNumber(columns.get(7).getText());
											double threeYear = parseNumber(columns.get(8).getText());
											double fiveYear = parseNumber(columns.get(9).getText());

											System.out.println("Code: " + code + ", Fund Name: " + fundName
													+ ", Umbrella Type: " + umbrellaType + ", One Month: " + oneMonth
													+ ", Three Month: " + threeMonth + ", Six Month: " + sixMonth
													+ ", After New Year: " + afterNeewYear + ", One Year: " + oneYear
													+ ", Three Year: " + threeYear + ", Five Year: " + fiveYear);

											FundFounder founder = funFounderRepository.findByName(founderValue);

											UmbrellaFundType umbrellaFundType = umbrellaFundTypeRepository
													.findByName(umbrellaType);

											if (umbrellaFundType == null) {
												umbrellaFundType = new UmbrellaFundType();
												umbrellaFundType.setUmbrellaFundTypeName(umbrellaType);
												umbrellaFundType = umbrellaFundTypeRepository.save(umbrellaFundType);
											}

											if (founder == null) {
												founder = new FundFounder();
												founder.setFundFounderName(founderValue);
												founder = funFounderRepository.save(founder);
											}

											Fund fund = fundRepository.findByCode(code);

											if (fund == null) {
												fund = new Fund(code, fundName);
												fund.setUmbrellaFundType(umbrellaFundType);
											} else {
												fund.setUmbrellaFundType(umbrellaFundType);
											}
											fund.setFundFounder(founder);

											fund = fundRepository.save(fund);

										}

										retry = false;
									}
								} catch (StaleElementReferenceException e) {
									// Element is stale, retry by re-locating the element
									System.out.println("Element is stale, retrying...");
								}
							}

							// Check for the next page button
							WebElement nextPageButton = driver.findElement(By.id("table_fund_returns_next"));
							if (nextPageButton.getAttribute("class").contains("disabled")) {
								hasNextPage = false; // No more pages to navigate
							} else {
								nextPageButton.click(); // Click the next page button
								wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("table_fund_returns"))); // Wait
																															// for
																															// the
																															// table
																															// to
																															// reload
							}
						}

					}

				}

			}

			System.out.println("Count = " + count);
		} catch (Exception e) {
			e.printStackTrace();
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

}
