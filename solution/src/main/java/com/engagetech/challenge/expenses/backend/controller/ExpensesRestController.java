package com.engagetech.challenge.expenses.backend.controller;

import com.engagetech.challenge.expenses.backend.model.Expense;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseForm;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseListItem;
import com.engagetech.challenge.expenses.backend.service.ExpenseService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/expenses")
@CrossOrigin(origins = "http://localhost:8080")
public class ExpensesRestController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private MapperFacade mapperFacade;

    @PostMapping
    public void saveExpense(@Valid @RequestBody ExpenseForm expenseForm) {
        Expense expense = mapperFacade.map(expenseForm, Expense.class);
        expenseService.saveExpense(expense);
    }

    @GetMapping
    public List<ExpenseListItem> getExpenses() {
        return expenseService.getExpenses()
                .stream()
                .map(expense -> mapperFacade.map(expense, ExpenseListItem.class))
                .collect(Collectors.toList());
    }
}
