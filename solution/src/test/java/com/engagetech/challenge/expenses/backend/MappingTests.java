package com.engagetech.challenge.expenses.backend;

import com.engagetech.challenge.expenses.backend.model.Expense;
import com.engagetech.challenge.expenses.backend.model.ModelTestFactory;
import com.engagetech.challenge.expenses.backend.model.VatRate;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseForm;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseListItem;
import com.engagetech.challenge.expenses.backend.service.CurrencyConverterService;
import com.engagetech.challenge.expenses.backend.service.VatService;
import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MappingTests {

    @Autowired
    private MapperFacade mapperFacade;

    @MockBean
    private VatService vatService;

    @MockBean
    private CurrencyConverterService currencyConverterService;

    @Test
    public void mapExpenseFormToExpense() throws Exception {
        VatRate vatRate = ModelTestFactory.defaultVatRate();
        ExpenseForm expenseForm = ModelTestFactory.newExpenseForm("2018-01-09", "17.89", "Some reason");
        Expense expense = ModelTestFactory.newExpense(null, "2018-01-09", 17.89, vatRate, "Some reason");

        given(vatService.getCurrentVat()).willReturn(vatRate);

        assertEquals(expense, mapperFacade.map(expenseForm, Expense.class));
    }

    @Test
    public void mapExpenseFormToExpenseWithCurrency() throws Exception {
        VatRate vatRate = ModelTestFactory.defaultVatRate();
        ExpenseForm expenseForm = ModelTestFactory.newExpenseForm("2018-01-09", "15.72 EUR", "Some another reason");
        Expense expense = ModelTestFactory.newExpense(null, "2018-01-09", 14.27, vatRate, "Some another reason");

        given(vatService.getCurrentVat()).willReturn(vatRate);
        given(currencyConverterService.convert(BigDecimal.valueOf(15.72), "EUR", MappingConfiguration.TO_CURRENCY_CODE))
                .willReturn(Optional.of(BigDecimal.valueOf(14.27)));

        assertEquals(expense, mapperFacade.map(expenseForm, Expense.class));
    }

    @Test
    public void mapExpenseToExpenseListItem() throws Exception {
        VatRate vatRate = ModelTestFactory.defaultVatRate();
        Expense expense = ModelTestFactory.newExpense(17L, "2018-02-25", 17.89, vatRate, "Test reason");
        ExpenseListItem expenseListItem = ModelTestFactory.newExpenseListItem("2018-02-25", 17.89, 2.98, "Test reason");

        given(vatService.calculateVat(expense.getAmount(), expense.getVatRate())).willReturn(expenseListItem.getVat());

        assertEquals(expenseListItem, mapperFacade.map(expense, ExpenseListItem.class));
    }

}