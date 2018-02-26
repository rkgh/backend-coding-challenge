package com.engagetech.challenge.expenses.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class FreeCurrencyExchangeRateProvider implements ExchangeRateProvider {

    private static final String CURRENCY_CONVERTER_API_URL = "http://free.currencyconverterapi.com/api/v5/convert?compact=ultra&q=";
    private static final TypeReference<Map<String, BigDecimal>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, BigDecimal>>() {};

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Optional<BigDecimal> getCurrencyExchangeRate(String fromCurrencyCode, String toCurrencyCode) {
        try {
            String conversionCurrenciesCode = (fromCurrencyCode + "_" + toCurrencyCode).toUpperCase();
            URL currencyConverterUrl = new URL(CURRENCY_CONVERTER_API_URL + conversionCurrenciesCode);
            Map<String, BigDecimal> exchangeRate = objectMapper.readValue(currencyConverterUrl, MAP_TYPE_REFERENCE);

            return Optional.ofNullable(exchangeRate.get(conversionCurrenciesCode));
        } catch (Exception e) {
            log.error("Failed to get exchange rate from [{}] to [{}]", fromCurrencyCode, toCurrencyCode, e);
        }

        return Optional.empty();
    }
}
