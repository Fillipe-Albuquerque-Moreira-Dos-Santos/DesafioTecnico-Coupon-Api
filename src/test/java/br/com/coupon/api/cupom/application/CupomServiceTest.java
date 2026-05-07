package br.com.coupon.api.cupom.application;

import br.com.coupon.api.cupom.domain.Cupom;
import br.com.coupon.api.cupom.domain.CupomException;
import br.com.coupon.api.cupom.domain.CupomNaoEncontradoException;
import br.com.coupon.api.cupom.infrastructure.CupomEntity;
import br.com.coupon.api.cupom.infrastructure.CupomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CupomServiceTest {

    private static final BigDecimal DESCONTO = new BigDecimal("10.00");
    private static final LocalDate AMANHA = LocalDate.now().plusDays(1);

    @Mock
    private CupomRepository repository;

    @InjectMocks
    private CupomService service;

    @Test
    void criarSalvaERetornaCupomComIdGerado() {
        when(repository.existsByCodeAndDeletedFalse("ABC123")).thenReturn(false);
        when(repository.save(any(CupomEntity.class)))
                .thenReturn(new CupomEntity(42L, "ABC123", "Promo", DESCONTO, AMANHA, false, false));

        Cupom cupom = service.criar("ABC123", "Promo", DESCONTO, AMANHA, false);

        assertEquals(42L, cupom.getId());
        assertEquals("ABC123", cupom.getCode());
        verify(repository).existsByCodeAndDeletedFalse("ABC123");
    }

    @Test
    void criarFalhaQuandoCodigoJaCadastrado() {
        when(repository.existsByCodeAndDeletedFalse("ABC123")).thenReturn(true);

        CupomException ex = assertThrows(CupomException.class, () ->
                service.criar("ABC123", "Promo", DESCONTO, AMANHA, false));

        assertTrue(ex.getMessage().contains("Código já cadastrado"));
        verify(repository, never()).save(any());
    }

    @Test
    void deletarMarcaFlagDeletedEPersiste() {
        CupomEntity existente = new CupomEntity(1L, "ABC123", "Promo", DESCONTO, AMANHA, false, false);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        service.deletar(1L);

        ArgumentCaptor<CupomEntity> captor = ArgumentCaptor.forClass(CupomEntity.class);
        verify(repository).save(captor.capture());
        assertTrue(captor.getValue().isDeleted());
    }

    @Test
    void deletarFalhaQuandoCupomNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CupomNaoEncontradoException.class, () -> service.deletar(99L));
        verify(repository, never()).save(any());
    }
}
