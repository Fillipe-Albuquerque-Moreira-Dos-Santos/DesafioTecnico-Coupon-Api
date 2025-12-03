package br.com.coupon.api.cupom.service;

import br.com.coupon.api.dto.CupomDTO;
import br.com.coupon.api.cupom.entity.Cupom;
import br.com.coupon.api.cupom.repository.CupomRepository;
import br.com.coupon.api.cupom.mapper.CupomMapper;
import br.com.coupon.api.cupom.validator.CupomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CupomService {

    private final CupomRepository repository;
    private final CupomMapper mapper;
    private final CupomValidator validator;

    public Cupom criar(CupomDTO dto) {
        validator.validarDataExpiracao(dto.getExpirationDate());
        String codigo = validator.processarCodigo(dto.getCode());
        validator.validarCodigoUnico(codigo);

        Cupom cupom = mapper.toEntity(dto);
        cupom.setCode(codigo);

        return repository.save(cupom);
    }

    public List<Cupom> listarTodos() {
        return repository.findByDeletedFalse();
    }

    public Cupom buscarPorId(Long id) {
        return repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));
    }

    public void deletar(Long id) {
        Cupom cupom = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));

        validator.validarCupomNaoDeletado(cupom.getDeleted());

        cupom.setDeleted(true);
        repository.save(cupom);
    }

    public Cupom atualizar(Long id, CupomDTO dto) {
        Cupom cupom = buscarPorId(id);

        validator.validarDataExpiracao(dto.getExpirationDate());
        mapper.updateEntity(dto, cupom);

        return repository.save(cupom);
    }
}