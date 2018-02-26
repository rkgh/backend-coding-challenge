package com.engagetech.challenge.expenses.backend.dao;

import com.engagetech.challenge.expenses.backend.model.Expense;
import com.engagetech.challenge.expenses.backend.model.ModelTestFactory;
import com.engagetech.challenge.expenses.backend.model.VatRate;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql({"/ExpenseRepositoryTests.sql"})
public class ExpenseRepositoryTests {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    public void save() throws Exception {
        Expense newExpense = ModelTestFactory.newExpense("2018-02-25", 17.89, vatRate(), "Test reason");
        Expense savedExpense = expenseRepository.save(newExpense);

        Expense foundExpense = expenseRepository.findOne(savedExpense.getId());
        assertEquals(savedExpense, foundExpense);
    }

    @Test
    public void findAllByOrderByIdDesc() throws Exception {
        List<Expense> expenses = expenseRepository.findAllByOrderByDateDesc();

        VatRate expectedVatRate = vatRate();
        List<Expense> expectedExpences = Lists.newArrayList(
            ModelTestFactory.newExpense(17L, "2018-02-25", 17.89, expectedVatRate, "Test reason"),
            ModelTestFactory.newExpense(16L, "2018-02-15", 21.75, expectedVatRate, "Some test reason"),
            ModelTestFactory.newExpense(18L, "2018-02-12", 56.42, expectedVatRate, "Another test reason")
        );

        assertEquals(3, expenses.size());
        assertEquals(expectedExpences, expenses);
    }

    private VatRate vatRate() {
        VatRate expectedVatRate = new VatRate();
        expectedVatRate.setId(24);
        expectedVatRate.setRate((byte) 20);
        expectedVatRate.setStartDate(LocalDate.parse("2011-01-04"));
        return expectedVatRate;
    }

}