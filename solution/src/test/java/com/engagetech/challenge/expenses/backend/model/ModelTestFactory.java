package com.engagetech.challenge.expenses.backend.model;

import com.engagetech.challenge.expenses.backend.model.view.ExpenseForm;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseListItem;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ModelTestFactory {

    public static Expense newExpense(Long id, String localDateString, double amount, VatRate vatRate, String reason) {
        Expense expense = new Expense();
        expense.setId(id);
        expense.setDate(LocalDate.parse(localDateString));
        expense.setAmount(BigDecimal.valueOf(amount));
        expense.setVatRate(vatRate);
        expense.setReason(reason);

        return expense;
    }

    public static Expense newExpense(String localDateString, double amount, VatRate vatRate, String reason) {
        return newExpense(null, localDateString, amount, vatRate, reason);
    }

    public static ExpenseForm newExpenseForm(String localDateString, String amountString, String reason) {
        ExpenseForm expense = new ExpenseForm();
        expense.setDate(LocalDate.parse(localDateString));
        expense.setAmount(amountString);
        expense.setReason(reason);
        return expense;
    }

    public static ExpenseForm newExpenseForm(String amountString, String reason) {
        ExpenseForm expense = new ExpenseForm();
        expense.setAmount(amountString);
        expense.setReason(reason);
        return expense;
    }

    public static ExpenseListItem newExpenseListItem(String localDateString, Double amount, Double vat, String reason) {
        ExpenseListItem expenseListItem = new ExpenseListItem();
        expenseListItem.setDate(LocalDate.parse(localDateString));
        expenseListItem.setAmount(BigDecimal.valueOf(amount));
        expenseListItem.setVat(BigDecimal.valueOf(vat));
        expenseListItem.setReason(reason);

        return expenseListItem;
    }

    public static VatRate defaultVatRate() {
        VatRate expectedVatRate = new VatRate();
        expectedVatRate.setId(2);
        expectedVatRate.setRate((byte) 20);
        expectedVatRate.setStartDate(LocalDate.parse("2011-01-04"));

        return expectedVatRate;
    }
}
