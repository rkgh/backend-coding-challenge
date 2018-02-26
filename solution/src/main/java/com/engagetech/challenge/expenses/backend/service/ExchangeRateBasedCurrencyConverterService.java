package com.engagetech.challenge.expenses.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class ExchangeRateBasedCurrencyConverterService implements CurrencyConverterService {

    @Autowired
    private ExchangeRateProvider exchangeRateProvider;

    @Override
    public Optional<BigDecimal> convert(BigDecimal amount, String fromCurrencyCode, String toCurrencyCode)
    {
        Assert.notNull(amount, "Amount must not be null");
        Assert.state(amount.compareTo(BigDecimal.ZERO) > 0, "Amount must positive");
        Assert.hasText(fromCurrencyCode, "fromCurrencyCode must not be empty");
        Assert.hasText(toCurrencyCode, "toCurrencyCode must not be empty");

        Optional<BigDecimal> currencyExchangeRate = exchangeRateProvider.getCurrencyExchangeRate(fromCurrencyCode, toCurrencyCode);

        return currencyExchangeRate.map(exchangeRate -> exchangeRate.multiply(amount));
    }
}