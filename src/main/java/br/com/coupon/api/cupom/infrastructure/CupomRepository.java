package br.com.coupon.api.cupom.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CupomRepository extends JpaRepository<CupomEntity, Long> {
    boolean existsByCodeAndDeletedFalse(String code);
}
