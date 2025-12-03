package br.com.coupon.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CupomDTO {

    @NotBlank(message = "O código é obrigatório.")
    private String code;

    @NotBlank(message = "A descrição é obrigatória.")
    private String description;

    @NotNull(message = "O valor do desconto é obrigatório.")
    @DecimalMin(value = "0.5", message = "O valor do desconto deve ser no mínimo 0.5.")
    private BigDecimal discountValue;

    @NotNull(message = "A data de expiração é obrigatória.")
    private LocalDate expirationDate;

    private Boolean published = false;
}
