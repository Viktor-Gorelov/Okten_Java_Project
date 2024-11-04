package com.okten.okten_project_java.services;

import com.okten.okten_project_java.dto.CurrencyRateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    public List<CurrencyRateDTO> getCurrencyRates() {
        CurrencyRateDTO[] rates = restTemplate.getForObject(API_URL, CurrencyRateDTO[].class);
        return Arrays.asList(rates);
    }

    public double convert(double amount, String fromCurrency, String toCurrency) {
        List<CurrencyRateDTO> rates = getCurrencyRates();
        double rateFrom = 1, rateTo = 1;

        if (fromCurrency.equals("UAH")) {
            rateFrom = 1;
        } else {
            rateFrom = rates.stream()
                    .filter(rate -> rate.getCcy().equals(fromCurrency))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unsupported currency '" + fromCurrency + "'"))
                    .getSale();
        }

        if (toCurrency.equals("UAH")) {
            rateTo = 1;
        } else {
            rateTo = rates.stream()
                    .filter(rate -> rate.getCcy().equals(toCurrency))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unsupported currency '" + toCurrency + "'"))
                    .getSale();
        }
        return (amount * rateFrom) / rateTo;
    }
}
