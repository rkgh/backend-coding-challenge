package com.engagetech.challenge.expenses.backend.model.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class ExpenseForm
{
    @NotNull(message="{date.null.message}")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @NotNull(message="{amount.null.message}")
    @Pattern(
            regexp = "\\d{0,19}\\.?\\d{0,2}(\\s+EUR)?",
            flags = {Pattern.Flag.CASE_INSENSITIVE},
            message="{amount.invalid.message}")
    private String amount;

    @NotNull(message="{reason.null.message}")
    @Size(min = 1, max = 256, message="{reason.invalid.size.message}")
    private String reason;
}
