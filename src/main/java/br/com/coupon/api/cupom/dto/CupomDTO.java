package br.com.coupon.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CupomDTO {

    @NotBlank(message = "Code é obrigatório")
    private String code;

    @NotBlank(message = "Description é obrigatório")
    private String description;

    @NotNull(message = "DiscountValue é obrigatório")
    @DecimalMin(value = "0.5", message = "DiscountValue deve ser no mínimo 0.5")
    private BigDecimal discountValue;

    @NotNull(message = "ExpirationDate é obrigatório")
    private LocalDate expirationDate;

    private Boolean published = false;
}
