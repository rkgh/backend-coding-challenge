package com.engagetech.challenge.expenses.backend.service;

import com.engagetech.challenge.expenses.backend.dao.ExpenseRepository;
import com.engagetech.challenge.expenses.backend.model.Expense;
import com.engagetech.challenge.expenses.backend.model.ModelTestFactory;
import com.engagetech.challenge.expenses.backend.model.VatRate;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ExpenseService.class)
public class ExpenseServiceTests {

    @Autowired
    private ExpenseService expenseService;

    @MockBean
    private ExpenseRepository expenseRepository;

    @Test
    public void saveExpense() throws Exception {
        VatRate vatRate = ModelTestFactory.defaultVatRate();
        Expense newExpense = ModelTestFactory.newExpense("2018-01-12", 14.65, vatRate, "Test reason");
        Expense savedExpense = ModelTestFactory.newExpense(7L, "2018-01-12", 14.65, vatRate, "Test reason");
        given(expenseRepository.save(newExpense)).willReturn(savedExpense);

        assertEquals(savedExpense, expenseService.saveExpense(newExpense));
        verify(expenseRepository, times(1)).save(newExpense);
    }

    @Test
    public void getExpenses() throws Exception {
        VatRate expectedVatRate = ModelTestFactory.defaultVatRate();
        List<Expense> expectedExpenses = Lists.newArrayList(
                ModelTestFactory.newExpense(17L, "2018-02-25", 17.89, expectedVatRate, "Test reason"),
                ModelTestFactory.newExpense(16L, "2018-02-15", 21.75, expectedVatRate, "Some test reason"),
                ModelTestFactory.newExpense(18L, "2018-02-12", 56.42, expectedVatRate, "Another test reason")
        );
        when(expenseRepository.findAllByOrderByDateDesc()).thenReturn(expectedExpenses);

        assertEquals(expectedExpenses, expenseService.getExpenses());
        verify(expenseRepository, only()).findAllByOrderByDateDesc();
    }
}