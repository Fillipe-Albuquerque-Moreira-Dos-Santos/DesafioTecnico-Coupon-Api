package br.com.coupon.api.cupom.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CupomTest {

    private static final BigDecimal DESCONTO = new BigDecimal("10.00");
    private static final LocalDate AMANHA = LocalDate.now().plusDays(1);

    @Test
    void normalizaCodigoRemovendoEspeciaisTruncandoEm6EUppercase() {
        Cupom c = Cupom.criar("ab@c#1!2-3xyz", "Promo", DESCONTO, AMANHA, false);

        assertEquals("ABC123", c.getCode());
    }

    @Test
    void rejeitaCodigoComMenosDe6CaracteresAlfanumericos() {
        assertThrows(CupomException.class, () ->
                Cupom.criar("AB#1@", "Promo", DESCONTO, AMANHA, false));
    }

    @Test
    void rejeitaCamposObrigatoriosNulos() {
        assertThrows(CupomException.class, () -> Cupom.criar(null, "Promo", DESCONTO, AMANHA, false));
        assertThrows(CupomException.class, () -> Cupom.criar("ABC123", null, DESCONTO, AMANHA, false));
        assertThrows(CupomException.class, () -> Cupom.criar("ABC123", "Promo", null, AMANHA, false));
        assertThrows(CupomException.class, () -> Cupom.criar("ABC123", "Promo", DESCONTO, null, false));
    }

    @Test
    void aceitaDescontoMinimoDeMeio() {
        Cupom c = Cupom.criar("ABC123", "Promo", new BigDecimal("0.5"), AMANHA, false);

        assertEquals(new BigDecimal("0.5"), c.getDiscountValue());
    }

    @Test
    void rejeitaDescontoAbaixoDoMinimo() {
        assertThrows(CupomException.class, () ->
                Cupom.criar("ABC123", "Promo", new BigDecimal("0.49"), AMANHA, false));
    }

    @Test
    void rejeitaExpiracaoNoPassado() {
        assertThrows(CupomException.class, () ->
                Cupom.criar("ABC123", "Promo", DESCONTO, LocalDate.now().minusDays(1), false));
    }

    @Test
    void podeNascerPublicado() {
        Cupom c = Cupom.criar("ABC123", "Promo", DESCONTO, AMANHA, true);

        assertTrue(c.isPublished());
    }

    @Test
    void deletaCupomMarcandoFlag() {
        Cupom c = Cupom.criar("ABC123", "Promo", DESCONTO, AMANHA, false);

        c.deletar();

        assertTrue(c.isDeleted());
    }

    @Test
    void naoDeletaCupomJaDeletado() {
        Cupom c = Cupom.criar("ABC123", "Promo", DESCONTO, AMANHA, false);
        c.deletar();

        assertThrows(CupomException.class, c::deletar);
    }
}
