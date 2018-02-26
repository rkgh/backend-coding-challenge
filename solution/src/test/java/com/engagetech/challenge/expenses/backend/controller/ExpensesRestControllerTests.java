package com.engagetech.challenge.expenses.backend.controller;

import com.engagetech.challenge.expenses.backend.model.Expense;
import com.engagetech.challenge.expenses.backend.model.ModelTestFactory;
import com.engagetech.challenge.expenses.backend.model.VatRate;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseForm;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseListItem;
import com.engagetech.challenge.expenses.backend.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.glasnost.orika.MapperFacade;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ExpensesRestController.class)
public class ExpensesRestControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseService expenseService;

    @MockBean
    private MapperFacade mapperFacade;


    @Test
    public void saveExpense() throws Exception {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-01-12"));
        expenseForm.setAmount("14.65");
        expenseForm.setReason("Test reason");
        VatRate vatRate = ModelTestFactory.defaultVatRate();
        Expense expense = ModelTestFactory.newExpense("2018-01-12", 14.65, vatRate, "Test reason");

        given(mapperFacade.map(expenseForm, Expense.class)).willReturn(expense);

        mvc.perform(
                post("/app/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseForm))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void saveExpenseWithNoData() throws Exception {
        ExpenseForm expenseForm = new ExpenseForm();

        mvc.perform(
                post("/app/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseForm))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(mapperFacade, never()).map(expenseForm, Expense.class);
    }

    @Test
    public void getExpenses() throws Exception {
        VatRate vatRate = ModelTestFactory.defaultVatRate();
        List<Expense> expenses = Lists.newArrayList(
                ModelTestFactory.newExpense(17L, "2018-02-25", 17.89, vatRate, "Test reason"),
                ModelTestFactory.newExpense(16L, "2018-02-15", 21.75, vatRate, "Some test reason"),
                ModelTestFactory.newExpense(18L, "2018-02-12", 56.42, vatRate, "Another test reason")
        );
        List<ExpenseListItem> expensesListItems = Lists.newArrayList(
                ModelTestFactory.newExpenseListItem("2018-02-25", 17.89, 2.98,"Test reason"),
                ModelTestFactory.newExpenseListItem("2018-02-15", 21.75, 3.63, "Some test reason"),
                ModelTestFactory.newExpenseListItem("2018-02-12", 56.42, 9.40, "Another test reason")
        );
        given(expenseService.getExpenses()).willReturn(expenses);
        Iterator<Expense> expenseIterator = expenses.iterator();
        Iterator<ExpenseListItem> expenseListItemIterator = expensesListItems.iterator();
        while (expenseIterator.hasNext() && expenseListItemIterator.hasNext()) {
            given(mapperFacade.map(expenseIterator.next(), ExpenseListItem.class)).willReturn(expenseListItemIterator.next());
        }
        mvc.perform(
                get("/app/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].date").value("25/02/2018"))
                .andExpect(jsonPath("$[0].amount").value(17.89))
                .andExpect(jsonPath("$[0].vat").value(2.98))
                .andExpect(jsonPath("$[0].reason").value("Test reason"))
                .andExpect(jsonPath("$[1].date").value("15/02/2018"))
                .andExpect(jsonPath("$[1].amount").value(21.75))
                .andExpect(jsonPath("$[1].vat").value(3.63))
                .andExpect(jsonPath("$[1].reason").value("Some test reason"))
                .andExpect(jsonPath("$[2].date").value("12/02/2018"))
                .andExpect(jsonPath("$[2].amount").value(56.42))
                .andExpect(jsonPath("$[2].vat").value(9.4))
                .andExpect(jsonPath("$[2].reason").value("Another test reason"))
        ;

        for (Expense expense : expenses) {
            verify(mapperFacade, times(1)).map(expense, ExpenseListItem.class);
        }
    }

}