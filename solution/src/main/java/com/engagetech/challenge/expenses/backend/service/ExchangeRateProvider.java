package com.engagetech.challenge.expenses.backend.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeRateProvider {

    Optional<BigDecimal> getCurrencyExchangeRate(String fromCurrencyCode, String toCurrencyCode);
}
