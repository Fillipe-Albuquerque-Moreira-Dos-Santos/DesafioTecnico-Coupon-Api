package br.com.coupon.api.cupom.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CupomRepository extends JpaRepository<CupomEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM CupomEntity c WHERE c.code = ?1 AND c.deleted = false")
    boolean existeCodigoAtivo(String code);
}
