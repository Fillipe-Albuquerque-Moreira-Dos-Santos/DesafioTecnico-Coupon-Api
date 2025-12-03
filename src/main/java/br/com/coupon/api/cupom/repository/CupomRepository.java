package br.com.coupon.api.cupom.repository;

import br.com.coupon.api.cupom.entity.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {
    List<Cupom> findByDeletedFalse();
    Optional<Cupom> findByIdAndDeletedFalse(Long id);
    Optional<Cupom> findByCodeAndDeletedFalse(String code);
}