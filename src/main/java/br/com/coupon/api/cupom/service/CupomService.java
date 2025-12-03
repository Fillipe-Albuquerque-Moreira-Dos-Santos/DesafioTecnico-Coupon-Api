package br.com.coupon.api.cupom.service;  // ← MUDA AQUI

import br.com.coupon.api.dto.CupomDTO;
import br.com.coupon.api.cupom.entity.Cupom;
import br.com.coupon.api.cupom.repository.CupomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CupomService {

    private final CupomRepository repository;

    public Cupom criar(CupomDTO dto) {

        if (dto.getExpirationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Data de expiração não pode ser no passado");
        }
        String codigo = processarCodigo(dto.getCode());
        if (repository.findByCodeAndDeletedFalse(codigo).isPresent()) {
            throw new RuntimeException("Código já existe");
        }
        Cupom cupom = new Cupom();
        cupom.setCode(codigo);
        cupom.setDescription(dto.getDescription());
        cupom.setDiscountValue(dto.getDiscountValue());
        cupom.setExpirationDate(dto.getExpirationDate());
        cupom.setPublished(dto.getPublished() != null ? dto.getPublished() : false);
        cupom.setDeleted(false);

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

        if (cupom.getDeleted()) {
            throw new RuntimeException("Cupom já foi deletado");
        }

        cupom.setDeleted(true);
        repository.save(cupom);
    }

    private String processarCodigo(String code) {
        // Remove caracteres especiais, deixa só letras e números
        String limpo = code.replaceAll("[^a-zA-Z0-9]", "");

        if (limpo.length() < 6) {
            throw new RuntimeException("Código deve ter pelo menos 6 caracteres alfanuméricos");
        }

        // Retorna os 6 primeiros caracteres em maiúsculo
        return limpo.substring(0, 6).toUpperCase();
    }

    public Cupom atualizar(Long id, CupomDTO dto) {
        Cupom cupom = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));

        // Validar data de expiração
        if (dto.getExpirationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Data de expiração não pode ser no passado");
        }

        // Validar desconto
        if (dto.getDiscountValue().compareTo(BigDecimal.valueOf(0.5)) < 0) {
            throw new RuntimeException("O valor mínimo de desconto é 0.5.");
        }

        // Atualiza campos permitidos
        cupom.setDescription(dto.getDescription());
        cupom.setDiscountValue(dto.getDiscountValue());
        cupom.setExpirationDate(dto.getExpirationDate());
        cupom.setPublished(dto.getPublished() != null ? dto.getPublished() : false);

        return repository.save(cupom);
    }

}