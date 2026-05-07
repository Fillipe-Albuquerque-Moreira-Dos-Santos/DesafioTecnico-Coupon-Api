package br.com.coupon.api.cupom.infrastructure;

import br.com.coupon.api.cupom.domain.Cupom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cupons")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CupomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean deleted;

    public static CupomEntity from(Cupom c) {
        return new CupomEntity(c.getId(), c.getCode(), c.getDescription(),
                c.getDiscountValue(), c.getExpirationDate(), c.isPublished(), c.isDeleted());
    }

    public Cupom toDomain() {
        return Cupom.restaurar(id, code, description, discountValue, expirationDate, published, deleted);
    }
}
