package com.engagetech.challenge.expenses.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
@WebMvcTest(ExchangeRateProvider.class)
public class FreeCurrencyExchangeRateProviderTests {

    @Autowired
    private ExchangeRateProvider exchangeRateProvider;

    @SpyBean
    private ObjectMapper objectMapper;

    @Test
    public void getCurrencyExchangeRate() {
        assertTrue(exchangeRateProvider.getCurrencyExchangeRate("EUR", "GBP").get().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void getCurrencyExchangeRateWithNotUpperCaseCurrencies() {
        assertTrue(exchangeRateProvider.getCurrencyExchangeRate("eUr", "GbP").get().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void getCurrencyExchangeRateWithException() throws Exception {
        doThrow(new IOException("Failed to get exchange rate")).when(objectMapper)
                .readValue(any(URL.class), any(TypeReference.class));

        assertFalse(exchangeRateProvider.getCurrencyExchangeRate("EUR", "GBP").isPresent());
    }

    @Test
    public void getCurrencyExchangeRateWithNonExistingFromCurrency() {
        assertFalse(exchangeRateProvider.getCurrencyExchangeRate("YYY", "GBP").isPresent());
    }

    @Test
    public void getCurrencyExchangeRateWithNonExistingToCurrency() {
        assertFalse(exchangeRateProvider.getCurrencyExchangeRate("EUR", "TTT").isPresent());
    }

    @Test
    public void getCurrencyExchangeRateWithNonExistingToAndFromCurrencies() {
        assertFalse(exchangeRateProvider.getCurrencyExchangeRate("ZZZ", "JJJ").isPresent());
    }
}