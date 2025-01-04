package com.example.cryptoservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CryptoExchangeService {

    private static final String EXCHANGE_API_URL = "https://api.coingecko.com/api/v3/simple/price";

    public Map<String, Object> getExchangeRate(String crypto, String fiat) {
        String cryptoId = crypto.toLowerCase(); // Ensure crypto is lowercase (e.g., bitcoin)
        String fiatCurrency = fiat.toLowerCase(); // Ensure fiat is lowercase (e.g., usd)
        String url = String.format("%s?ids=%s&vs_currencies=%s", EXCHANGE_API_URL, cryptoId, fiatCurrency);

        RestTemplate restTemplate = new RestTemplate();
        try {
            // Fetch the response
            Map<String, Map<String, Object>> response = restTemplate.getForObject(url, Map.class);

            // Validate the response structure
            if (response == null || !response.containsKey(cryptoId)) {
                throw new RuntimeException("Invalid crypto currency: " + crypto);
            }
            if (!response.get(cryptoId).containsKey(fiatCurrency)) {
                throw new RuntimeException("Invalid fiat currency: " + fiat);
            }

            // Extract the rate
            Object rateObj = response.get(cryptoId).get(fiatCurrency);

            Double rate;
            if (rateObj instanceof Integer) {
                rate = ((Integer) rateObj).doubleValue();
            } else if (rateObj instanceof Double) {
                rate = (Double) rateObj;
            } else {
                throw new RuntimeException("Unexpected rate type: " + rateObj.getClass());
            }

            // Return the rate in a map
            return Map.of("rate", rate);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching exchange rates: " + e.getMessage(), e);
        }
    }





    public Double calculateCryptoPurchase(Double amount, Double rate) {
        return amount / rate; // Converts fiat to crypto
    }

    public Double calculateCryptoSale(Double amount, Double rate) {
        return amount * rate; // Converts crypto to fiat
    }

    public Double convertCryptoToFiat(Double cryptoAmount, Double rate) {
        return cryptoAmount * rate; // Converts crypto to fiat
    }

    public Double convertFiatToCrypto(Double fiatAmount, Double rate) {
        return fiatAmount / rate; // Converts fiat to crypto
    }
}
