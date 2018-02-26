package com.engagetech.challenge.expenses.backend.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface CurrencyConverterService {

    Optional<BigDecimal> convert(BigDecimal amount, String fromCurrencyCode, String toCurrencyCode);
}

