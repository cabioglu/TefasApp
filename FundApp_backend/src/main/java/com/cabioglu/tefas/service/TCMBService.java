package com.cabioglu.tefas.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.cabioglu.tefas.dto.DateInterval;

@Service
public class TCMBService {

	public JSONObject getUSDTRY(DateInterval dateInterval) {

		String url = String.format(
				"https://evds2.tcmb.gov.tr/service/evds/series=TP.DK.USD.S.YTL&startDate=%1$s&endDate=%2$s&type=json",
				dateInterval.getStartDateStringforTCMB(), dateInterval.getEndDateStringforTCMB());
		// https://evds2.tcmb.gov.tr/service/evds/series=TP.DK.USD.S.YTL&startDate=01-01-2017&endDate=29-12-2024&type=json
		// https://evds2.tcmb.gov.tr/service/evds/series=TP.DK.USD.S.YTL&startDate=%1$s&endDate=%2$s&type=json
		RestClient client = RestClient.create();

		String resultString = client.get()
				.uri(url)
				.header("key", "XiXNReaVAs")
				.retrieve()
				.body(String.class);

		JSONObject result = new JSONObject(resultString);

		return result;
	}
}
