package br.com.coupon.api.cupom.web;

import br.com.coupon.api.cupom.domain.Cupom;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class CupomResponse {
    Long id;
    String code;
    String description;
    BigDecimal discountValue;
    LocalDate expirationDate;
    boolean published;

    public static CupomResponse from(Cupom c) {
        return new CupomResponse(c.getId(), c.getCode(), c.getDescription(),
                c.getDiscountValue(), c.getExpirationDate(), c.isPublished());
    }
}
