package com.engagetech.challenge.expenses.backend.model.view;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpenseFormValidationTests {

    private static Validator validator;

    @BeforeClass
    public static void setUp() throws Exception {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    public void validationForCorrectData() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("15.00 EUR");
        expenseForm.setReason("Test reason");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    @Test
    public void validationForAmountWithMultipleWhitespaces() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("15.00      EUR");
        expenseForm.setReason("R");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    @Test
    public void validationForAmountCurrencyWithLowerSpaceCharacters() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("15.00 EuR");
        expenseForm.setReason("Test reason");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    @Test
    public void validationForNullDate() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setAmount("15.00 EUR");
        expenseForm.setReason("Test reason");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<ExpenseForm> violation =  constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("date");
        assertThat(violation.getMessage()).isEqualTo("Date must be not null");
    }

    @Test
    public void validationForNullAmount() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setReason("Test reason");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<ExpenseForm> violation =  constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
        assertThat(violation.getMessage()).isEqualTo("Amount must be not null");
    }

    @Test
    public void validationForAmountWithInvalidNumber() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("amount");
        expenseForm.setReason("Test reason");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<ExpenseForm> violation =  constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
        assertThat(violation.getMessage()).isEqualTo("Invalid amount");
    }

    @Test
    public void validationForNonEuroCurrncry() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("15.75 GBP");
        expenseForm.setReason("Test reason");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<ExpenseForm> violation =  constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
        assertThat(violation.getMessage()).isEqualTo("Invalid amount");
    }

    @Test
    public void validationForAmountWithIncorrectCurrency() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("15.75 EURR");
        expenseForm.setReason("Test reason");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<ExpenseForm> violation =  constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
        assertThat(violation.getMessage()).isEqualTo("Invalid amount");
    }

    @Test
    public void validationForAmountWithNoSpaceBetweenAmountAndCurrency() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("15.75EUR");
        expenseForm.setReason("Test reason");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<ExpenseForm> violation =  constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
        assertThat(violation.getMessage()).isEqualTo("Invalid amount");
    }

    @Test
    public void validationForNullReason() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("15.75 eur");
        //expenseForm.setReason("Test reason");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<ExpenseForm> violation =  constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("reason");
        assertThat(violation.getMessage()).isEqualTo("Reason must be not null");
    }

    @Test
    public void validationForEmptyReason() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("15.75 eur");
        expenseForm.setReason("");

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<ExpenseForm> violation =  constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("reason");
        assertThat(violation.getMessage()).isEqualTo("Reason must be between 1 and 256");
    }

    @Test
    public void validationForReasonWithMoreThanMaxSize() {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setDate(LocalDate.parse("2018-02-14"));
        expenseForm.setAmount("15.75 eur");
        expenseForm.setReason(
                "Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason " +
                "Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason " +
                "Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason " +
                "Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason " +
                "Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason " +
                "Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason Reason "
        );

        Set<ConstraintViolation<ExpenseForm>> constraintViolations = validator.validate(expenseForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<ExpenseForm> violation =  constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("reason");
        assertThat(violation.getMessage()).isEqualTo("Reason must be between 1 and 256");
    }

}