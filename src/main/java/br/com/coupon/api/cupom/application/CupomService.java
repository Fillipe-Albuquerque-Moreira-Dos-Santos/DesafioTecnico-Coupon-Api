package br.com.coupon.api.cupom.application;

import br.com.coupon.api.cupom.domain.Cupom;
import br.com.coupon.api.cupom.domain.CupomException;
import br.com.coupon.api.cupom.domain.CupomNaoEncontradoException;
import br.com.coupon.api.cupom.infrastructure.CupomEntity;
import br.com.coupon.api.cupom.infrastructure.CupomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CupomService {

    private final CupomRepository repository;

    @Transactional
    public Cupom criar(String code, String description, BigDecimal discountValue, LocalDate expirationDate, boolean published) {

        Cupom cupom = Cupom.criar(code, description, discountValue, expirationDate, published);

        if (repository.existsByCodeAndDeletedFalse(cupom.getCode())) {
            throw new CupomException("Código já cadastrado: " + cupom.getCode());
        }
        return repository.save(CupomEntity.from(cupom)).toDomain();
    }

    @Transactional
    public void deletar(Long id) {
        Cupom cupom = repository.findById(id).map(CupomEntity::toDomain).orElseThrow(() -> new CupomNaoEncontradoException(id));

        cupom.deletar();

        repository.save(CupomEntity.from(cupom));
    }
}
