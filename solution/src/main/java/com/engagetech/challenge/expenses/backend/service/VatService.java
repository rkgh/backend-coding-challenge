package com.engagetech.challenge.expenses.backend.service;

import com.engagetech.challenge.expenses.backend.dao.VatRepository;
import com.engagetech.challenge.expenses.backend.model.VatRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class VatService {

    @Autowired
    private VatRepository vatRepository;

    @Transactional(readOnly = true)
    public VatRate getCurrentVat() {
        return vatRepository.findFirstByOrderByFinishDateDesc();
    }

    public BigDecimal calculateVat(BigDecimal amountWithVat, VatRate vatRate) {
        BigDecimal vatRateBigDecimal = BigDecimal.valueOf(vatRate.getRate());
        return amountWithVat.multiply(vatRateBigDecimal).divide(
                vatRateBigDecimal.add(BigDecimal.valueOf(100)), 2, RoundingMode.HALF_UP);
    }

}
