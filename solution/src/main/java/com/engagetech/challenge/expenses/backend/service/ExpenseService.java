package com.engagetech.challenge.expenses.backend.service;

import com.engagetech.challenge.expenses.backend.dao.ExpenseRepository;
import com.engagetech.challenge.expenses.backend.model.Expense;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Transactional
    public Expense saveExpense(Expense expense) {
        log.debug("Save expense [{}]", expense);
        return expenseRepository.save(expense);
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpenses() {
        log.debug("Get expenses");

        return expenseRepository.findAllByOrderByDateDesc();
    }
}




