package br.com.coupon.api.cupom.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CupomRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String description;

    @NotNull
    private BigDecimal discountValue;

    @NotNull
    private LocalDate expirationDate;

    private boolean published;
}
