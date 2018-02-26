package com.engagetech.challenge.expenses.backend.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Expense
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate date;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vat_id", nullable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private VatRate vatRate;

    @NotNull
    @Size(min = 1, max = 256)
    private String reason;
}
