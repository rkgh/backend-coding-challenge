package com.engagetech.challenge.expenses.backend.service;

import com.engagetech.challenge.expenses.backend.dao.VatRepository;
import com.engagetech.challenge.expenses.backend.model.ModelTestFactory;
import com.engagetech.challenge.expenses.backend.model.VatRate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(VatService.class)
public class VatServiceTests {

    @Autowired
    private VatService vatService;

    @MockBean
    private VatRepository vatRepository;

    @Test
    public void getCurrentVat() throws Exception {
        VatRate expectedVatRate = ModelTestFactory.defaultVatRate();

        given(vatRepository.findFirstByOrderByFinishDateDesc()).willReturn(expectedVatRate);

        assertEquals(expectedVatRate, vatService.getCurrentVat());
        verify(vatRepository, only()).findFirstByOrderByFinishDateDesc();
    }

    @Test
    public void calculateVat() throws Exception {
        VatRate vatRate = ModelTestFactory.defaultVatRate();

        assertEquals(new BigDecimal("2.00"), vatService.calculateVat(BigDecimal.valueOf(12), vatRate));
        assertEquals(new BigDecimal("1.63"), vatService.calculateVat(BigDecimal.valueOf(9.75), vatRate));
        assertEquals(new BigDecimal("4.15"), vatService.calculateVat(BigDecimal.valueOf(24.92), vatRate));
        assertEquals(new BigDecimal("4.16"), vatService.calculateVat(BigDecimal.valueOf(24.93), vatRate));
        assertEquals(new BigDecimal("4.16"), vatService.calculateVat(BigDecimal.valueOf(24.94), vatRate));
        assertEquals(new BigDecimal("4.16"), vatService.calculateVat(BigDecimal.valueOf(24.95), vatRate));
        assertEquals(new BigDecimal("4.16"), vatService.calculateVat(BigDecimal.valueOf(24.96), vatRate));
        assertEquals(new BigDecimal("4.16"), vatService.calculateVat(BigDecimal.valueOf(24.97), vatRate));
        assertEquals(new BigDecimal("4.16"), vatService.calculateVat(BigDecimal.valueOf(24.98), vatRate));
        assertEquals(new BigDecimal("4.17"), vatService.calculateVat(BigDecimal.valueOf(24.99), vatRate));
    }



}