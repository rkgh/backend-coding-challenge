package com.engagetech.challenge.expenses.backend.dao;

import com.engagetech.challenge.expenses.backend.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long>
{
    List<Expense> findAllByOrderByDateDesc();
}
