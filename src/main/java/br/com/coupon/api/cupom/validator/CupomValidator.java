package br.com.coupon.api.cupom.validator;

import br.com.coupon.api.cupom.repository.CupomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CupomValidator {

    private final CupomRepository repository;

    public void validarDataExpiracao(LocalDate data) {
        if (data.isBefore(LocalDate.now())) {
            throw new RuntimeException("Data de expiração não pode ser no passado");
        }
    }

    public void validarCodigoUnico(String codigo) {
        if (repository.findByCodeAndDeletedFalse(codigo).isPresent()) {
            throw new RuntimeException("Código já existe");
        }
    }

    public void validarCupomNaoDeletado(boolean deleted) {
        if (deleted) {
            throw new RuntimeException("Cupom já foi deletado");
        }
    }

    public String processarCodigo(String code) {
        String limpo = code.replaceAll("[^a-zA-Z0-9]", "");

        if (limpo.length() < 6) {
            throw new RuntimeException("Código deve ter pelo menos 6 caracteres alfanuméricos");
        }
        return limpo.substring(0, 6).toUpperCase();
    }
}