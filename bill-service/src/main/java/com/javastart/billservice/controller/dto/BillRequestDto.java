package com.javastart.billservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BillRequestDto {

    @NotNull
    private Long accountId;

    private Boolean isDefault;

    @NotNull
    private BigDecimal amount;

    private Boolean overdraftEnabled;

}
