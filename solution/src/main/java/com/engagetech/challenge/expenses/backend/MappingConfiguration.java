package com.engagetech.challenge.expenses.backend;

import com.engagetech.challenge.expenses.backend.model.Expense;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseForm;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseListItem;
import com.engagetech.challenge.expenses.backend.service.CurrencyConverterService;
import com.engagetech.challenge.expenses.backend.service.VatService;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Optional;

@Configuration
public class MappingConfiguration {

    public static final String TO_CURRENCY_CODE = "GBP";

    @Autowired
    private VatService vatService;

    @Autowired
    private CurrencyConverterService currencyConverterService;

    @Bean
    public MapperFacade mapperFacade() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(ExpenseForm.class, Expense.class)
                .exclude("amount")
                .byDefault()
                .customize(new CustomMapper<ExpenseForm, Expense>() {
                    @Override
                    public void mapAtoB(ExpenseForm expenseForm, Expense expense, MappingContext context) {
                        String amountWithCurrency = expenseForm.getAmount();
                        String[] splitAmountAndCurrency = amountWithCurrency.split("\\s+");
                        BigDecimal amount = new BigDecimal(splitAmountAndCurrency[0]);
                        if (splitAmountAndCurrency.length > 1) {
                            String currency = splitAmountAndCurrency[1];
                            Optional<BigDecimal> convertedAmount = currencyConverterService.convert(amount, currency, TO_CURRENCY_CODE);
                            amount = convertedAmount.orElse(null);
                        }

                        expense.setAmount(amount);
                        expense.setVatRate(vatService.getCurrentVat());
                    }
                })
                .register();

        mapperFactory.classMap(Expense.class, ExpenseListItem.class)
                .byDefault()
                .customize(new CustomMapper<Expense, ExpenseListItem>() {
                    @Override
                    public void mapAtoB(Expense expense, ExpenseListItem expenseListItem, MappingContext context) {
                        expenseListItem.setVat(vatService.calculateVat(expense.getAmount(), expense.getVatRate()));
                    }
                })
                .register();

        return mapperFactory.getMapperFacade();
    }
}
