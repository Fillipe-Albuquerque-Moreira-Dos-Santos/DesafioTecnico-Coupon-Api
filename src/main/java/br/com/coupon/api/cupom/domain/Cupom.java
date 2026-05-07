package br.com.coupon.api.cupom.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cupom {

    private static final int TAMANHO_CODIGO = 6;
    private static final BigDecimal DESCONTO_MINIMO = new BigDecimal("0.5");

    private final Long id;
    private final String code;
    private final String description;
    private final BigDecimal discountValue;
    private final LocalDate expirationDate;
    private final boolean published;
    private boolean deleted;

    public static Cupom criar(String code, String description, BigDecimal discountValue,
                              LocalDate expirationDate, boolean published) {
        if (description == null || description.isBlank()) {
            throw new CupomException("Descrição é obrigatória");
        }
        if (discountValue == null || discountValue.compareTo(DESCONTO_MINIMO) < 0) {
            throw new CupomException("Valor de desconto mínimo é " + DESCONTO_MINIMO);
        }
        if (expirationDate == null || expirationDate.isBefore(LocalDate.now())) {
            throw new CupomException("Data de expiração não pode estar no passado");
        }
        return new Cupom(null, normalizarCodigo(code), description.trim(),
                discountValue, expirationDate, published, false);
    }

    public static Cupom restaurar(Long id, String code, String description, BigDecimal discountValue,
                                  LocalDate expirationDate, boolean published, boolean deleted) {
        return new Cupom(id, code, description, discountValue, expirationDate, published, deleted);
    }

    public void deletar() {
        if (deleted) {
            throw new CupomException("Cupom já está deletado");
        }
        this.deleted = true;
    }

    private static String normalizarCodigo(String raw) {
        if (raw == null) {
            throw new CupomException("Código é obrigatório");
        }
        String alfanumerico = raw.replaceAll("[^A-Za-z0-9]", "");
        if (alfanumerico.length() < TAMANHO_CODIGO) {
            throw new CupomException("Código deve conter ao menos " + TAMANHO_CODIGO + " caracteres alfanuméricos");
        }
        return alfanumerico.substring(0, TAMANHO_CODIGO).toUpperCase();
    }
}
