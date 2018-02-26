package com.engagetech.challenge.expenses.backend.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseListItem {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    private BigDecimal amount;

    private BigDecimal vat;

    private String reason;
}
