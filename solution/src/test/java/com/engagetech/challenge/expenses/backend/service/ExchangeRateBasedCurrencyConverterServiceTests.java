package com.engagetech.challenge.expenses.backend.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateBasedCurrencyConverterServiceTests
{
    @Mock
    private ExchangeRateProvider exchangeRateProvider;

    @InjectMocks
    private ExchangeRateBasedCurrencyConverterService currencyConverterService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void convert() {
        String fromCurrencyCode = "EUR";
        String toCurrencyCode = "GBP";

        when(exchangeRateProvider.getCurrencyExchangeRate(fromCurrencyCode, toCurrencyCode)).thenReturn(Optional.of(BigDecimal.valueOf(0.88)));

        assertEquals(Optional.of(new BigDecimal("8.80")), currencyConverterService.convert(BigDecimal.TEN, fromCurrencyCode, toCurrencyCode));
    }

    @Test
    public void convertWithIncorrectFromCurrency() {
        String fromCurrencyCode = "ZZZ";
        String toCurrencyCode = "GBP";

        when(exchangeRateProvider.getCurrencyExchangeRate(fromCurrencyCode, toCurrencyCode)).thenReturn(Optional.empty());

        assertFalse(currencyConverterService.convert(BigDecimal.TEN, fromCurrencyCode, toCurrencyCode).isPresent());
    }

    @Test
    public void convertWithIncorrectToCurrency() {
        String fromCurrencyCode = "EUR";
        String toCurrencyCode = "TTT";

        when(exchangeRateProvider.getCurrencyExchangeRate(fromCurrencyCode, toCurrencyCode)).thenReturn(Optional.empty());

        assertFalse(currencyConverterService.convert(BigDecimal.TEN, "EUR", "TTT").isPresent());
    }

    @Test
    public void convertWithNullAmount() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Amount must not be null");

        currencyConverterService.convert(null, "EUR", "GBP");
    }

    @Test
    public void convertWithNegativeAmount() {
        exception.expect(IllegalStateException .class);
        exception.expectMessage("Amount must positive");

        currencyConverterService.convert(BigDecimal.valueOf(-10), "EUR", "GBP");
    }

    @Test
    public void convertWithEmptyFromCurrency() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("fromCurrencyCode must not be empty");

        currencyConverterService.convert(BigDecimal.TEN, "", "GBP");
    }

    @Test
    public void convertWithWhitespacesFromCurrency() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("fromCurrencyCode must not be empty");

        currencyConverterService.convert(BigDecimal.TEN, "   ", "GBP");
    }

    @Test
    public void convertWithNullFromCurrency() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("fromCurrencyCode must not be empty");

        currencyConverterService.convert(BigDecimal.TEN, null, "GBP");
    }

    @Test
    public void convertWithEmptyToCurrency() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("toCurrencyCode must not be empty");

        currencyConverterService.convert(BigDecimal.TEN, "EUR", " ");
    }

    @Test
    public void convertWithNullToCurrency() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("toCurrencyCode must not be empty");

        currencyConverterService.convert(BigDecimal.TEN, "EUR", null);
    }

    @Test
    public void convertWithNullAmountAndFromToCurrencies() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Amount must not be null");

        currencyConverterService.convert(null, null, null);
    }

    @Test
    public void convertWithNullAmountAndEmptyFromToCurrencies() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Amount must not be null");

        currencyConverterService.convert(null, "  ", "  ");
    }
}